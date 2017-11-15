package com.github.wakingrufus.elo.api.error;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Slf4j
public class ValidstionExceptionHandler implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException ex) {
        log.error("Not Found: " + ex.getLocalizedMessage(), ex);
        int statusCode = 422;
        return Response.status(statusCode)
                .entity(ex.getLocalizedMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
