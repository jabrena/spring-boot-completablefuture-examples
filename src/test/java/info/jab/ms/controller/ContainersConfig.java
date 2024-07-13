package info.jab.ms.controller;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.KafkaContainer;
import static org.testcontainers.utility.DockerImageName.parse;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@TestConfiguration(proxyBeanMethods = false)
public class ContainersConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(ContainersConfig.class);

    //https://hub.docker.com/r/confluentinc/cp-kafka/tags
    private static final String kafkaVersion = "confluentinc/cp-kafka:7.5.5";

    @Bean
    @ServiceConnection
    KafkaContainer kafkaContainer() {
        logger.info("Creating Kafka Broker");
        return new KafkaContainer(parse(kafkaVersion));
    }

    @Value("${mykafka.topic}")
    private String topicName;

    @Bean
    String createTopic(KafkaContainer kafkaContainer) {
        logger.info("Creating Topic on Kafka Broker");
        String bootstrapServers = kafkaContainer.getBootstrapServers();
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        
        try (AdminClient adminClient = AdminClient.create(properties)) {
            NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }

        return topicName;
    }
}
