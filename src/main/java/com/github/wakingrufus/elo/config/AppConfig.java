package com.github.wakingrufus.elo.config;

import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

@Service
@Slf4j
public class AppConfig {
    private final static String DB_URL = "DB_URL";

    public String getDbUrl() {
        String fromEnv = System.getenv(DB_URL);
        log.info("db url override: " + fromEnv);
        if (fromEnv != null && fromEnv.trim().isEmpty()) {
            fromEnv = null;
        }
        return fromEnv;
    }
}
