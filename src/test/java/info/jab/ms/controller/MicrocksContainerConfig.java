package info.jab.ms.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;

import io.github.microcks.testcontainers.MicrocksContainer;

@TestConfiguration(proxyBeanMethods = false)
public class MicrocksContainerConfig {

    @Bean
    MicrocksContainer microcksContainer(DynamicPropertyRegistry registry) {
        MicrocksContainer microcks = new MicrocksContainer("quay.io/microcks/microcks-uber:1.9.1-native")
                .withMainArtifacts("inventory-openapi.yaml")
                .withAccessToHost(true);

        registry.add(
                "greek-gods-api-url", () -> microcks.getRestMockEndpoint("gods", "1.0"));

        return microcks;
    }
}
