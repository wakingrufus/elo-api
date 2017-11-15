package com.github.wakingrufus.elo.api;


import com.github.wakingrufus.elo.api.error.ForbiddenException;
import com.github.wakingrufus.elo.api.error.ResourceNotFoundException;
import com.github.wakingrufus.elo.api.error.ValidationException;
import com.github.wakingrufus.elo.game.Game;
import com.github.wakingrufus.elo.game.GameService;
import com.github.wakingrufus.elo.league.League;
import com.github.wakingrufus.elo.league.LeagueService;
import com.github.wakingrufus.elo.player.Player;
import com.github.wakingrufus.elo.player.PlayerService;
import com.github.wakingrufus.elo.user.User;
import com.github.wakingrufus.elo.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.List;

@Api(value = "Game")
@Path("games")
@Singleton
@Slf4j
public class GamesResource {
    private final GameService gameService;
    private final LeagueService leagueService;
    private final UserService userService;
    private final PlayerService playerService;

    @Inject
    public GamesResource(GameService gameService, LeagueService leagueService, UserService userService,
                         PlayerService playerService) {
        log.debug("creating GamesResource");
        this.gameService = gameService;
        this.leagueService = leagueService;
        this.userService = userService;
        this.playerService = playerService;
    }

    @POST
    @Produces("application/json")
    @RolesAllowed("user")
    @ApiOperation(value = "create", produces = "application/json")
    public League create(Game game, @Context final Response response, @Context SecurityContext sc) {

        League createdLeague = leagueService.getLeague(game.getLeagueId());
        if (createdLeague == null) {
            throw new ValidationException("League " + game.getLeagueId() + " does not exist");
        } else {
            boolean canAddGame = false;
            String userEmail = sc.getUserPrincipal().getName();
            User user = userService.getByEmail(userEmail);
            Collection<Player> playersForLeague = playerService.getPlayersForLeague(createdLeague.getId());
            for (Player player : playersForLeague) {
                if (player.isAdmin() && player.getUserId().equals(user.getId())) {
                    canAddGame = true;
                }
            }
            if (canAddGame) {
                gameService.addGame(game);
            } else {
                throw new ForbiddenException("only admin may create game");
            }
        }

        response.setStatus(HttpStatus.CREATED_201);
        return createdLeague;
    }

    @GET
    @RolesAllowed("user")
    @Path("{id}")
    @Produces("application/json")
    @ApiOperation(value = "get", produces = "application/json")
    public Game getById(@PathParam("id") String id) {
        Game game = gameService.getById(id);
        if (game == null) {
            throw new ResourceNotFoundException("No game with id " + id);
        }
        return game;
    }

    @GET
    @RolesAllowed("user")
    @Produces("application/json")
    @ApiOperation(value = "get", produces = "application/json")
    public List<Game> getByLeague(@QueryParam("leagueId") String id) {
        List<Game> games = gameService.getGamesForLeague(id);
        return games;
    }
}
