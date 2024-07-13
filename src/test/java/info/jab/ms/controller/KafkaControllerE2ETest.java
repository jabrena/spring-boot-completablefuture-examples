package info.jab.ms.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import info.jab.ms.controller.KafkaController.MyRequest;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ContainersConfig.class)
public class KafkaControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldSendMessageToKafka() throws Exception {
        // Given
        String address = "http://localhost:" + port + "/v1/messages";
        String expectedMessage = "Hello World";

        // When
        MyRequest payload = new MyRequest(expectedMessage);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MyRequest> request = new HttpEntity<>(payload, headers);
        ResponseEntity<MyRequest> result = this.restTemplate.postForEntity(address, request, MyRequest.class);

        // Then
        assertThat(result.getBody().message()).isEqualTo(expectedMessage);
    }
}

