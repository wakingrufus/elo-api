package com.github.wakingrufus.elo.game;

import com.github.wakingrufus.elo.elo.AdjustmentCalculator;
import com.github.wakingrufus.elo.elo.AdjustmentCalculatorFactory;
import com.github.wakingrufus.elo.elo.ExpectedScoreCalculator;
import com.github.wakingrufus.elo.elo.ExpectedScoreCalculatorFactory;
import com.github.wakingrufus.elo.elo.KfactorCalculator;
import com.github.wakingrufus.elo.elo.KfactorCalculatorFactory;
import com.github.wakingrufus.elo.league.League;
import com.github.wakingrufus.elo.league.LeagueService;
import com.github.wakingrufus.elo.player.Player;
import com.github.wakingrufus.elo.player.PlayerService;
import com.github.wakingrufus.elo.ratinghistory.RatingHistoryItem;
import com.github.wakingrufus.elo.ratinghistory.RatingHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class GameService {

    private final ExpectedScoreCalculatorFactory expectedScoreCalculatorFactory;
    private final GameDao gameDao;
    private final GameLookupDao gameLookupDao;
    private final LeagueService leagueService;
    private final PlayerService playerService;
    private final RatingHistoryService ratingHistoryService;
    private final AdjustmentCalculatorFactory adjustmentCalculatorFactory;
    private final KfactorCalculatorFactory kfactorCalculatorFactory;

    @Inject
    public GameService(GameDao gameDao, GameLookupDao gameLookupDao, LeagueService leagueService,
                       PlayerService playerService,
                       ExpectedScoreCalculatorFactory expectedScoreCalculatorFactory,
                       RatingHistoryService ratingHistoryService,
                       AdjustmentCalculatorFactory adjustmentCalculatorFactory,
                       KfactorCalculatorFactory kfactorCalculatorFactory) {
        this.gameDao = gameDao;
        this.gameLookupDao = gameLookupDao;
        this.leagueService = leagueService;
        this.playerService = playerService;
        this.expectedScoreCalculatorFactory = expectedScoreCalculatorFactory;
        this.ratingHistoryService = ratingHistoryService;
        this.adjustmentCalculatorFactory = adjustmentCalculatorFactory;
        this.kfactorCalculatorFactory = kfactorCalculatorFactory;
    }


    public void recalculateLeagueGames(League league) {
        Collection<Player> playersForLeague = playerService.getPlayersForLeague(league.getId());
        for (Player player : playersForLeague) {
            playerService.reset(player, league.getStartingRating());
            ratingHistoryService.clearHistoryForPlayer(player.getId());
        }
        List<Game> gamesForLeague = getGamesForLeague(league);
        Collections.sort(gamesForLeague, new GameTimeComparator());
        for (Game game : gamesForLeague) {
            processGame(league, game);
        }

    }

    public List<Game> getGamesForLeague(League league) {
        Collection<GameLookup> gameLookups = gameLookupDao.findByPartition(league.getId());
        List<Game> games = new ArrayList<>();
        for (GameLookup lookup : gameLookups) {
            games.add(gameDao.findOne(lookup.getId()).toDto());
        }
        return games;
    }

    void processGame(League league, Game game) {

        ExpectedScoreCalculator expectedScoreCalculator = expectedScoreCalculatorFactory.build(league.getXi());
        AdjustmentCalculator adjustmentCalculator = adjustmentCalculatorFactory.build();
        KfactorCalculator kfactorCalculator = kfactorCalculatorFactory.build();

        List<Player> team1Players = new ArrayList<>();
        int team1StartingRating = 0;
        int team1AvgGamesPlayed = 0;
        for (String playerId : game.getTeam1PlayerIds()) {
            Player player = playerService.getPlayer(playerId);
            team1StartingRating += playerService.getPlayer(playerId).getCurrentRating();
            team1AvgGamesPlayed += player.getGamesPlayed();
            team1Players.add(player);
        }
        team1StartingRating /= game.getTeam1PlayerIds().size();
        team1AvgGamesPlayed /= game.getTeam1PlayerIds().size();

        int team2StartingRating = 0;
        int team2AvgGamesPlayed = 0;
        List<Player> team2Players = new ArrayList<>();
        for (String playerId : game.getTeam2PlayerIds()) {
            Player player = playerService.getPlayer(playerId);
            team2StartingRating += playerService.getPlayer(playerId).getCurrentRating();
            team2AvgGamesPlayed += player.getGamesPlayed();
            team2Players.add(player);
        }
        team2StartingRating /= game.getTeam2PlayerIds().size();
        team2AvgGamesPlayed /= game.getTeam1PlayerIds().size();

        BigDecimal team1ExpectedScoreRatio = expectedScoreCalculator.calculate(team1StartingRating, team2StartingRating);
        BigDecimal team2ExpectedScoreRatio = expectedScoreCalculator.calculate(team2StartingRating, team1StartingRating);

        log.debug("team1ExpectedScoreRatio: " + team1ExpectedScoreRatio.toPlainString());
        log.debug("team2ExpectedScoreRatio: " + team2ExpectedScoreRatio.toPlainString());

        int totalGoals = game.getTeam1Score() + game.getTeam2Score();
        BigDecimal team1ActualScore = new BigDecimal(game.getTeam1Score()).divide(new BigDecimal(totalGoals), MathContext.DECIMAL32);
        BigDecimal team2ActualScore = new BigDecimal(game.getTeam2Score()).divide(new BigDecimal(totalGoals), MathContext.DECIMAL32);

        log.debug("team1ActualScore: " + team1ActualScore.toPlainString());
        log.debug("team2ActualScore: " + team2ActualScore.toPlainString());

        int team1Wins = 0;
        int team1Losses = 0;
        int team2Wins = 0;
        int team2Losses = 0;
        if (game.getTeam1Score() > game.getTeam2Score()) {
            team1Wins = 1;
            team2Losses = 1;
        } else {
            team2Wins = 1;
            team1Losses = 1;
        }


        for (Player player : team1Players) {
            log.debug("applying changes to player: " + player.toString());
            int kFactor = kfactorCalculator.calculateKfactor(league.getKfactorBase(), league.getTrialPeriod(),
                    league.getTrialKFactorMultiplier(), player.getGamesPlayed(), team2AvgGamesPlayed);
            log.debug("using kfactor: " + kFactor);
            int adjustment = adjustmentCalculator.calculate(kFactor, team1ActualScore, team1ExpectedScoreRatio);
            log.debug("adjustment: " + adjustment);
            playerService.update(player.getId(), adjustment, 1, team1Wins, team1Losses);
            RatingHistoryItem toCreate = RatingHistoryItem.builder()
                    .id(UUID.randomUUID().toString())
                    .gameId(game.getId())
                    .playerId(player.getId())
                    .ratingAdjustment(adjustment)
                    .startingRating(player.getCurrentRating())
                    .build();
            ratingHistoryService.create(toCreate);
        }

        for (Player player : team2Players) {
            log.debug("applying changes to player: " + player.toString());
            int kFactor = kfactorCalculator.calculateKfactor(league.getKfactorBase(), league.getTrialPeriod(),
                    league.getTrialKFactorMultiplier(), player.getGamesPlayed(), team1AvgGamesPlayed);
            log.debug("using kfactor: " + kFactor);
            int adjustment = adjustmentCalculator.calculate(kFactor, team2ActualScore, team2ExpectedScoreRatio);
            log.debug("adjustment: " + adjustment);
            playerService.update(player.getId(), adjustment, 1, team2Wins, team2Losses);
            RatingHistoryItem toCreate = RatingHistoryItem.builder()
                    .gameId(game.getId())
                    .playerId(player.getId())
                    .ratingAdjustment(adjustment)
                    .startingRating(player.getCurrentRating())
                    .build();
            ratingHistoryService.create(toCreate);
        }

    }

    public Game addGame(Game game) {

        League league = leagueService.getLeague(game.getLeagueId());
        Game tocreate = game.toBuilder()
                .id(UUID.randomUUID().toString())
                .entryDate(ZonedDateTime.now())
                .build();
        GameRecord createdGameRecord = gameDao.create(tocreate.toRecord());
        GameLookup gameLookupToCreate = GameLookup.builder().bucket(league.getId()).id(createdGameRecord.getId()).build();
        gameLookupDao.create(gameLookupToCreate);
        Game createdGame = createdGameRecord.toDto();
        processGame(league, createdGame);
        return createdGame;

    }
}
