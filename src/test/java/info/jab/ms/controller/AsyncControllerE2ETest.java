package info.jab.ms.controller;


import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Disabled;
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
    public void should_work1() {
        String url = "http://localhost:" + port + "/v1/async-result";
         
        await().atMost(10, TimeUnit.SECONDS).until(() -> {
            ResponseEntity<String> asyncResponse = restTemplate.getForEntity(url, String.class);
            return asyncResponse.getBody().contains("Async task completed");
        });
    }

    @Disabled
    @Test
    public void should_work2() {
        String url = "http://localhost:" + port + "/v1/async-future";
         
        await().atMost(10, TimeUnit.SECONDS).until(() -> {
            ResponseEntity<String> asyncResponse = restTemplate.getForEntity(url, String.class);
            return asyncResponse.getBody().contains("Async task completed");
        });
    }

    @Test
    public void should_work3() {
        String url = "http://localhost:" + port + "/v1/async-future2";
         
        await().atMost(10, TimeUnit.SECONDS).until(() -> {
            ResponseEntity<String> asyncResponse = restTemplate.getForEntity(url, String.class);
            return asyncResponse.getBody().contains("Async task completed");
        });
    }

    @Test
    public void should_work4() {
        String url = "http://localhost:" + port + "/v1/async-cf";
         
        await().atMost(10, TimeUnit.SECONDS).until(() -> {
            ResponseEntity<String> asyncResponse = restTemplate.getForEntity(url, String.class);
            return asyncResponse.getBody().contains("Async task completed");
        });
    }
}

