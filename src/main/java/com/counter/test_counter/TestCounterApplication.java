package com.counter.test_counter;

import com.counter.test_counter.config.ManualEnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestCounterApplication {
    public static void main(String[] args) {
        ManualEnvLoader.loadEnvVariables("C:\\java projects\\tg_bot\\files_for_bot\\file.env");
        SpringApplication.run(TestCounterApplication.class, args);
    }

}
