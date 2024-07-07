package info.jab.ms.controller;


import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
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

    @Test
    public void testAsyncEndpoint() {
        String url = "http://localhost:" + port + "/v1/async";
        
        // Make the initial call
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        // Await the completion of the async task
        await().atMost(10, TimeUnit.SECONDS).until(() -> {
            ResponseEntity<String> asyncResponse = restTemplate.getForEntity(url, String.class);
            return asyncResponse.getBody().contains("Async task completed");
        });
    }
}

