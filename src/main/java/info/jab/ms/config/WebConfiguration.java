package info.jab.ms.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
public class WebConfiguration {

    @Bean
    public RestClient restClient(RestTemplateBuilder restTemplateBuilder) {
        return RestClient.builder().build();
    }
}
