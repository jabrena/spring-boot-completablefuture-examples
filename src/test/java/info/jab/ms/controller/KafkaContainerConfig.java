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
//import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@TestConfiguration(proxyBeanMethods = false)
public class KafkaContainerConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaContainerConfig.class);

    //https://hub.docker.com/r/confluentinc/cp-kafka/tags
    //https://hub.docker.com/r/apache/kafka-native/tags
    //private static final String kafkaVersion = "confluentinc/cp-kafka:7.7.0";
    private static final String kafkaVersion = "apache/kafka-native:3.8.0";

    private static Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(logger);

    @Bean
    @ServiceConnection(name = "kafka")
    KafkaContainer kafkaContainer() {
        logger.info("Creating Kafka Broker");
        var imageName = DockerImageName.parse(kafkaVersion);//.asCompatibleSubstituteFor("confluentinc/cp-kafka");
        return new KafkaContainer(imageName).withLogConsumer(logConsumer);
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
