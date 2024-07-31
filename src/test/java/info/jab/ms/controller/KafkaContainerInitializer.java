package info.jab.ms.controller;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

public class KafkaContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static Logger logger = LoggerFactory.getLogger(KafkaContainerInitializer.class);

    private static String topicName = "example_topic";

    //https://hub.docker.com/r/confluentinc/cp-kafka/tags
    //https://hub.docker.com/r/apache/kafka-native/tags
    private static final String kafkaVersion = "confluentinc/cp-kafka:7.7.0";
    //private static final String kafkaVersion = "apache/kafka-native:3.8.0";

    private static final DockerImageName myImage = DockerImageName.parse(kafkaVersion)
        .asCompatibleSubstituteFor("confluentinc/cp-kafka");

    private static Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(logger);
    private static final KafkaContainer kafkaContainer = 
        new KafkaContainer(myImage).withLogConsumer(logConsumer);

    static {
        kafkaContainer.start();
    }

    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertyValues.of("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers())
                .applyTo(configurableApplicationContext.getEnvironment());

        //createTopic(topicName);
    }

    private static Slf4jLogConsumer getLogConsumer(String containerName) {
        return new Slf4jLogConsumer(LoggerFactory.getLogger("container."+containerName))
                .withRemoveAnsiCodes(false);
    }

    private static String createTopic(String topicName) {
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
