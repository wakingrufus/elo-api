package com.github.wakingrufus.elo.api.error;

import com.github.wakingrufus.elo.user.UsernameExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UsernameExistsExceptionHandler implements ExceptionMapper<UsernameExistsException> {
    private final Logger log = LoggerFactory.getLogger(UsernameExistsExceptionHandler.class);

    @Override
    public Response toResponse(UsernameExistsException exception) {
        log.warn(exception.getLocalizedMessage(), exception);
        return Response.status(Response.Status.CONFLICT)
                .entity(exception.getLocalizedMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
