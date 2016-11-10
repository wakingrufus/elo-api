package com.github.wakingrufus.elo.tech.jersey;


import com.github.wakingrufus.elo.auth.AuthorizationService;
import com.github.wakingrufus.elo.user.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;

@Slf4j
public class AuthenticationFilter implements ContainerRequestFilter {
    private final AuthorizationService authorizationService;

    public AuthenticationFilter(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {


        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // throw new NotAuthorizedException("Authorization header must be provided");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {

            User user = authorizationService.authenticate(token);

            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> user.getEmail();
                }

                @Override
                public boolean isUserInRole(String role) {
                    return true;
                }

                @Override
                public boolean isSecure() {
                    return true;
                }

                @Override
                public String getAuthenticationScheme() {
                    return SecurityContext.BASIC_AUTH;
                }
            });
            requestContext.getHeaders().put("userId", Collections.singletonList(user.getId()));

        } catch (Exception e) {
            log.debug(e.getLocalizedMessage(), e);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }

    }
}