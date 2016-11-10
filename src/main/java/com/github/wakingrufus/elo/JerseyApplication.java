package com.github.wakingrufus.elo;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.github.wakingrufus.elo.tech.ObjectMapperFactory;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

@Slf4j
public class JerseyApplication extends ResourceConfig {

    public JerseyApplication() {
        //   ServiceLocator locator = ServiceLocatorUtilities.createAndPopulateServiceLocator();

        log.info("setting up hk2");
        //    new JerseyAutoScan(locator).scan();
        packages("com.github.wakingrufus.elo", "com.github.wakingrufus.elo.tech.jersey", "com.wordnik.swagger.jaxrs.listing");

        JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJaxbJsonProvider.setMapper(new ObjectMapperFactory().buildObjectMapper());
        register(jacksonJaxbJsonProvider);

        register(ApiListingResource.class);
        register(SwaggerSerializers.class);
        // Place to manually bind objects, in the case that the Jersey Auto-scan isn't working
        // e.g. bind(x.class).to(y.class);
        //
        // note: if the object is generic, use TypeLiteral
        // e.g. bind(x.class).to(new TypeLiteral&lt;InjectionResolver&gt;(){});
        //
        register(new AbstractBinder() {
            @Override
            protected void configure() {

                //      bind(DefaultUserService.class).to(UserService.class).in(Singleton.class);
                //    bind(DynamoUserDao.class).to(UserDao.class).in(Singleton.class);
                //    bind(InMemoryRolesDao.class).to(RolesDao.class).in(Singleton.class);
                //    bind(DefaultAuthorizationService.class).to(AuthorizationService.class).in(Singleton.class);
                //    bind(InMemorySessionDao.class).to(SessionDao.class).in(Singleton.class);
                //    bind(AppConfig.class).to(AppConfig.class).in(Singleton.class);
                //    bind(DynamoDbClientFactory.class).to(DynamoDbClientFactory.class).in(Singleton.class);
            }
        });
    }

}
