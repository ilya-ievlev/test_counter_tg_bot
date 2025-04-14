package com.counter.test_counter.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Slf4j
public class ManualEnvironmentLoader {

    public static void loadEnvironmentVariables(String filePath) {
        try {
            Properties properties = new Properties();
            properties.load(Files.newBufferedReader(Paths.get(filePath)));

            properties.forEach((key, value) -> {
                System.setProperty(key.toString(), value.toString());
            });

            log.info("Environment variables loaded!");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
