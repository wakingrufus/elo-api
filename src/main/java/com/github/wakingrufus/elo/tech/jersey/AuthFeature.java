package com.github.wakingrufus.elo.tech.jersey;


import com.github.wakingrufus.elo.auth.AuthorizationService;
import com.github.wakingrufus.elo.auth.DefaultAuthorizationService;
import com.github.wakingrufus.elo.auth.DynamoSessionDao;
import com.github.wakingrufus.elo.config.AppConfig;
import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import com.github.wakingrufus.elo.user.DefaultUserService;
import com.github.wakingrufus.elo.user.DynamoUserDao;
import com.github.wakingrufus.elo.user.DynamoUserEmailLookupDao;
import com.github.wakingrufus.elo.user.UserService;

import javax.annotation.security.PermitAll;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;

@Provider
public class AuthFeature implements DynamicFeature {

    private final AuthorizationService authorizationService;
    private final UserService userService;


    public AuthFeature() {
        AppConfig appConfig = new AppConfig();
        DynamoDbClientFactory clientFactory = new DynamoDbClientFactory(appConfig);
        this.userService = new DefaultUserService(new DynamoUserDao(clientFactory), new DynamoUserEmailLookupDao(clientFactory));
        this.authorizationService = new DefaultAuthorizationService(new DynamoSessionDao(clientFactory), userService);


    }

    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        Method method = resourceInfo.getResourceMethod();
        if (!method.isAnnotationPresent(PermitAll.class)
                && !resourceInfo.getResourceClass().equals(io.swagger.jaxrs.listing.ApiListingResource.class)) {
            AuthenticationFilter authenticationFilter = new AuthenticationFilter(authorizationService);
            context.register(authenticationFilter);
        }
    }
}