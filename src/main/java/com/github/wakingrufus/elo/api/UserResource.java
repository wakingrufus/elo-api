package com.github.wakingrufus.elo.api;


import com.github.wakingrufus.elo.api.error.ResourceNotFoundException;
import com.github.wakingrufus.elo.user.CreateUserService;
import com.github.wakingrufus.elo.user.User;
import com.github.wakingrufus.elo.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

@Api(value = "User")
@Path("user")
@Singleton
@Slf4j
public class UserResource {
    private final UserService userService;
    private final CreateUserService createUserService;

    @Inject
    public UserResource(UserService userService, CreateUserService createUserService) {
        log.debug("creating user resource");
        this.userService = userService;
        log.debug("user service = " + userService.toString());
        this.createUserService = createUserService;
    }

    @POST
    @Produces("application/json")
    @PermitAll
    @ApiOperation(value = "create", produces = "application/json")
    public User create(User user, @Context final Response response) {
        log.info("creating user: " + user.getEmail() + " " + user.getName());
        response.setStatus(HttpStatus.CREATED_201);
        return createUserService.createUser(user);
    }

    @GET
    @RolesAllowed("user")
    @Path("{id}")
    @Produces("application/json")
    @ApiOperation(value = "get", produces = "application/json")
    public User getById(@PathParam("id") String id) {
        User user = userService.getById(id);
        if (user == null) {
            throw new ResourceNotFoundException("No user with id " + id);
        }
        return userService.getById(id);
    }

}
