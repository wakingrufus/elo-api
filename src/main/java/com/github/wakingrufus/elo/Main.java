package com.github.wakingrufus.elo;

import com.github.wakingrufus.elo.tech.IpFinder;
import io.swagger.jaxrs.config.BeanConfig;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.URI;


public class Main {
    public static void main(String[] args) {
        IpFinder ipFinder = new IpFinder();
        String host = ipFinder.findIp() + ":9005";

        String BASE_URI = "http://0.0.0.0:9005/elo-api/";

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("BETA");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("");
        beanConfig.setBasePath("/elo-api");
        beanConfig.setResourcePackage("com.github.wakingrufus.elo.api");
        beanConfig.setTitle("ELO API");
        beanConfig.setScan(true);

        //   ServletContainer sc = new ServletContainer(new JerseyApplication());
        ServiceLocator locator = ServiceLocatorUtilities.createAndPopulateServiceLocator();

        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), new JerseyApplication(), locator);

        try {
            httpServer.start();
            CLStaticHttpHandler staticHttpHandler = new CLStaticHttpHandler(Main.class.getClassLoader(), "swagger-ui/");
            httpServer.getServerConfiguration().addHttpHandler(staticHttpHandler, "/docs");

            System.out.println(String.format("Jersey app started with WADL available at "
                    + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
            System.in.read();
            // httpServer.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
