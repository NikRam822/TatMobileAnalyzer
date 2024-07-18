package com.example.TatMobileAnalyzer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TatMobileAnalyzerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mainMethodLoadsContext() {
        String[] args = {};
        ConfigurableApplicationContext context = new SpringApplicationBuilder(TatMobileAnalyzerApplication.class)
                .run(args);
        assertNotNull(context);
        context.close();
    }
}