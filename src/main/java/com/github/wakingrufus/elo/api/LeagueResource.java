package com.github.wakingrufus.elo.api;


import com.github.wakingrufus.elo.api.error.ResourceNotFoundException;
import com.github.wakingrufus.elo.league.GameType;
import com.github.wakingrufus.elo.league.League;
import com.github.wakingrufus.elo.league.LeagueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.Arrays;
import java.util.List;

@Api(value = "User")
@Path("leagues")
@Singleton
@Slf4j
public class LeagueResource {
    private final LeagueService leagueService;

    @Inject
    public LeagueResource(LeagueService leagueService) {
        log.debug("creating LeagueResource");
        this.leagueService = leagueService;
        log.debug("leagueService = " + leagueService.toString());
    }

    @POST
    @Produces("application/json")
    @RolesAllowed("user")
    @ApiOperation(value = "create", produces = "application/json")
    public League create(League league, @Context final Response response) {
        log.info("creating league: " + league.getName());
        League createdLeague = leagueService.createLeague(league);
        response.setStatus(HttpStatus.CREATED_201);
        return createdLeague;
    }

    @GET
    @RolesAllowed("user")
    @Path("{id}")
    @Produces("application/json")
    @ApiOperation(value = "get", produces = "application/json")
    public League getById(@PathParam("id") String id) {
        League league = leagueService.getLeague(id);
        if (league == null) {
            throw new ResourceNotFoundException("No league with id " + id);
        }
        return league;
    }

    @GET
    @RolesAllowed("user")
    @Produces("application/json")
    @ApiOperation(value = "get", produces = "application/json")
    public List<League> getByType(@QueryParam("typeId") int id) {
        GameType gameType = GameType.forId(id);
        if (gameType == null) {
            throw new ResourceNotFoundException("No league type with id: " + id);
        }
        List<League> leagues = leagueService.getLeaguesForGameType(gameType);
        return leagues;
    }

    @GET
    @RolesAllowed("user")
    @Path("types")
    @Produces("application/json")
    @ApiOperation(value = "getGameTypes", produces = "application/json")
    public List<GameType> getGameTypes() {
        return leagueService.listGameTypes();
    }

}
