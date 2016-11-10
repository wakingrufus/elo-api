package com.github.wakingrufus.elo.tech.jersey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wakingrufus.elo.tech.ObjectMapperFactory;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class ObjectMapperResolver implements ContextResolver<ObjectMapper> {
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return new ObjectMapperFactory().buildObjectMapper();
    }
}