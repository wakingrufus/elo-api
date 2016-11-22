package com.github.wakingrufus.elo.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


@Api(value = "HealthCheck")
@Path("health")
@Singleton
public class HealthResource {

    @GET
    @PermitAll
    @Produces("text/plain")
    @ApiOperation(value = "health", produces = "text/plain")
    public String checkHealth() {
        return "OK";
    }

}
