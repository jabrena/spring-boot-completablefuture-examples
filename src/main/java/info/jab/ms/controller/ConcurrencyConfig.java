package info.jab.ms.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.Fallback;

@Configuration
public class ConcurrencyConfig {

    @Primary
    @Bean
    @ConditionalOnProperty(name = "spring.profiles.active", havingValue = "vt")
    public ExecutorService executorVT() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
    

    @Bean
    //@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "!vt") // Not "vt" profile
    public ExecutorService executorPT() {
        int cores = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(cores);
    }

    /*
    //Not available in Spring Boot 3.3.1
    @Bean
	@Fallback
	ExecutorService defaultExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(cores);
	}
    */

}
