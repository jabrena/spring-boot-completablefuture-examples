package info.jab.ms.config;

import java.util.concurrent.Executors;

import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Better than use: 
 * spring.threads.virtual.enabled=true
 */
@Configuration
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "vt")
public class TomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            ProtocolHandler handler = connector.getProtocolHandler();
            if (handler instanceof Http11NioProtocol) {
                ((AbstractHttp11Protocol<?>) handler).setExecutor(Executors.newVirtualThreadPerTaskExecutor());
            }
        });
    }

}