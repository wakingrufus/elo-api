package com.github.wakingrufus.elo.game;

import com.github.wakingrufus.elo.elo.AdjustmentCalculator;
import com.github.wakingrufus.elo.elo.AdjustmentCalculatorFactory;
import com.github.wakingrufus.elo.elo.ExpectedScoreCalculator;
import com.github.wakingrufus.elo.elo.ExpectedScoreCalculatorFactory;
import com.github.wakingrufus.elo.elo.KfactorCalculator;
import com.github.wakingrufus.elo.elo.KfactorCalculatorFactory;
import com.github.wakingrufus.elo.league.League;
import com.github.wakingrufus.elo.player.Player;
import com.github.wakingrufus.elo.player.PlayerService;
import com.github.wakingrufus.elo.ratinghistory.RatingHistoryService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.eq;

public class GameServiceTest {
    @Test
    public void calculateKfactor() throws Exception {

    }

    @Test
    public void recalculateLeagueGames() throws Exception {

    }

    @Test
    public void getGamesForLeague() throws Exception {

    }

    @Test
    public void processGame() throws Exception {
        // data
        Player player1 = Player.builder().id(UUID.randomUUID().toString()).gamesPlayed(20).currentRating(1200).build();
        Player player2 = Player.builder().id(UUID.randomUUID().toString()).gamesPlayed(15).currentRating(1600).build();
        League league = League.builder()
                .xi(1000)
                .kfactorBase(32)
                .trialPeriod(10)
                .trialKFactorMultiplier(2)
                .build();
        Game game = Game.builder()
                .team1Score(9)
                .team2Score(1)
                .team1PlayerIds(Collections.singletonList(player1.getId()))
                .team2PlayerIds(Collections.singletonList(player2.getId()))
                .build();
        BigDecimal expectedTeam1ActualScore = new BigDecimal(".9");
        BigDecimal team1ExpectedScoreRatio = new BigDecimal(".4");
        BigDecimal expectedTeam2ActualScore = new BigDecimal(".1");
        BigDecimal team2ExpectedScoreRatio = new BigDecimal(".6");


        int player1Adjustment = 16;
        int player1Kfactor = 32;

        int player2Adjustment = -16;
        int player2Kfactor = 32;

        // mocks
        PlayerService playerService = Mockito.mock(PlayerService.class);
        ExpectedScoreCalculatorFactory scoreCalculatorFactory = Mockito.mock(ExpectedScoreCalculatorFactory.class);
        RatingHistoryService ratingHistoryService = Mockito.mock(RatingHistoryService.class);
        AdjustmentCalculatorFactory adjustmentCalculatorFactory = Mockito.mock(AdjustmentCalculatorFactory.class);
        AdjustmentCalculator adjustmentCalculator = Mockito.mock(AdjustmentCalculator.class);
        ExpectedScoreCalculator expectedScoreCalculator = Mockito.mock(ExpectedScoreCalculator.class);
        KfactorCalculatorFactory kfactorCalculatorFactory = Mockito.mock(KfactorCalculatorFactory.class);
        KfactorCalculator kfactorCalculator = Mockito.mock(KfactorCalculator.class);

        // mock behavior
        Mockito.when(kfactorCalculatorFactory.build()).thenReturn(kfactorCalculator);
        Mockito.when(adjustmentCalculatorFactory.build()).thenReturn(adjustmentCalculator);
        Mockito.when(scoreCalculatorFactory.build(league.getXi())).thenReturn(expectedScoreCalculator);


        Mockito.when(adjustmentCalculator.calculate(eq(player1Kfactor), Mockito.any(BigDecimal.class),
                eq(team1ExpectedScoreRatio)))
                .thenReturn(player1Adjustment);
        ArgumentCaptor<BigDecimal> team2ActualScoreCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        Mockito.when(adjustmentCalculator.calculate(eq(player2Kfactor), team2ActualScoreCaptor.capture(),
                eq(team2ExpectedScoreRatio)))
                .thenReturn(player2Adjustment);

        Mockito.when(expectedScoreCalculator.calculate(player1.getCurrentRating(), player2.getCurrentRating()))
                .thenReturn(team1ExpectedScoreRatio);
        Mockito.when(expectedScoreCalculator.calculate(player2.getCurrentRating(), player1.getCurrentRating()))
                .thenReturn(team2ExpectedScoreRatio);

        Mockito.when(kfactorCalculator.calculateKfactor(league.getKfactorBase(), league.getTrialPeriod(),
                league.getTrialKFactorMultiplier(), player1.getGamesPlayed(), player2.getGamesPlayed()))
                .thenReturn(player1Kfactor);
        Mockito.when(kfactorCalculator.calculateKfactor(league.getKfactorBase(), league.getTrialPeriod(),
                league.getTrialKFactorMultiplier(), player2.getGamesPlayed(), player1.getGamesPlayed()))
                .thenReturn(player2Kfactor);

        Mockito.when(playerService.getPlayer(player1.getId())).thenReturn(player1);
        Mockito.when(playerService.getPlayer(player2.getId())).thenReturn(player2);

        // instance
        GameService instance = new GameService(null, null, null, playerService, scoreCalculatorFactory,
                ratingHistoryService, adjustmentCalculatorFactory, kfactorCalculatorFactory);

        instance.processGame(league, game);

        // assertions
        ArgumentCaptor<BigDecimal> team1ActualScoreCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        Mockito.verify(adjustmentCalculator, Mockito.times(1))
                .calculate(eq(player1Kfactor), team1ActualScoreCaptor.capture(), eq(team1ExpectedScoreRatio));
        Assert.assertEquals("correct team1 score", 0, expectedTeam1ActualScore.compareTo(team1ActualScoreCaptor.getValue()));
        Assert.assertEquals("correct team2 score", 0, expectedTeam2ActualScore.compareTo(team2ActualScoreCaptor.getValue()));
        Mockito.verify(playerService, Mockito.times(1)).update(player1, player1Adjustment, 1, 1, 0);
        Mockito.verify(playerService, Mockito.times(1)).update(player2, player2Adjustment, 1, 0, 1);

    }

    @Test
    public void addGame() throws Exception {

    }

}