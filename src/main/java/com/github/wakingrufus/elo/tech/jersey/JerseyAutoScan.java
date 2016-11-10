package com.github.wakingrufus.elo.tech.jersey;


import lombok.extern.slf4j.Slf4j;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.Populator;
import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.annotations.Contract;

import java.io.IOException;

@Slf4j
public class JerseyAutoScan {
    final ServiceLocator serviceLocator;


    public JerseyAutoScan(final ServiceLocator serviceLocatorIn) {
        serviceLocator = serviceLocatorIn;
    }

    public void scan() {
        try {
            DynamicConfigurationService dcs = serviceLocator.getService(DynamicConfigurationService.class);
            Populator populator = dcs.getPopulator();

            populator.populate(new JerseyDescriptorFinder());
            log.info("found " + serviceLocator.getAllServices(Contract.class).size() + " contracts");
        } catch (IOException e) {
            throw new MultiException(e);
        }
    }
}