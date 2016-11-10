package com.github.wakingrufus.elo.api;


import com.github.wakingrufus.elo.auth.AuthorizationService;
import com.github.wakingrufus.elo.auth.LoginRequest;
import com.github.wakingrufus.elo.auth.Session;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;


@Api(value = "Login")
@Path("session")
@Singleton
public class SessionResource {
    private final Logger log = LoggerFactory.getLogger(SessionResource.class);
    private final AuthorizationService authorizationService;

    @Inject
    public SessionResource(AuthorizationService authorizationService) {
        log.debug("creating session resource");
        this.authorizationService = authorizationService;
    }

    @POST
    @Produces("application/json")
    @ApiOperation(value = "login", produces = "application/json")
    @PermitAll
    public Session login(LoginRequest session, @Context final Response response) {
        log.debug("logging in: " + session.getEmail());
        response.setStatus(HttpStatus.CREATED_201);
        return authorizationService.login(session.getEmail(), session.getPassword());
    }

    @DELETE
    @Path("{token}")
    @Produces("application/json")
    @RolesAllowed("user")
    @ApiOperation(value = "logout", produces = "application/json")
    // @Context SecurityContext security
    public void logout(@PathParam("token") String token, @Context final Response response) {
        authorizationService.logout(token);
        response.setStatus(HttpStatus.NO_CONTENT_204);
    }
}
