package info.jab.ms.controller;


import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AsyncControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    static Stream<String> endpoints() {
        return Stream.of(
            "/v1/async-result",
            // "/v1/async-future",
            "/v1/async-future2",
            //"/v1/async-future3",
            //"/v1/async-future4",
            "/v1/async-cf"
        );
    }

    @ParameterizedTest
    @MethodSource("endpoints")
    void shouldWork(String endpoint) {
        //Given
        String url = "http://localhost:" + port + endpoint;
        String expectedMessage = "Async task completed";

        //When
        //Then
        await().atMost(10, TimeUnit.SECONDS).until(() -> {
            ResponseEntity<String> asyncResponse = restTemplate.getForEntity(url, String.class);
            return asyncResponse.getBody().contains(expectedMessage);
        });
    }
}

