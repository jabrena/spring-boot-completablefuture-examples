package info.jab.ms.controller;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.github.tomakehurst.wiremock.WireMockServer;

//@TestMethodOrder(MethodOrderer.MethodName.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, 
    properties = {
        //"greek-gods-api-url=http://localhost:8089/greek",
        "roman-gods-api-url=http://localhost:8089/roman",
        "nordic-gods-api-url=http://localhost:8089/nordic"
    })
@Import(MicrocksContainerConfig.class)
public class BlockingControllerE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    WireMockServer wireMockServer;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/greek"))
            .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withBodyFile("greek-gods.json")));

        wireMockServer.stubFor(get(urlEqualTo("/roman"))
            .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withBodyFile("roman-gods.json")));

        wireMockServer.stubFor(get(urlEqualTo("/nordic"))
            .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withBodyFile("nordic-gods.json")));
    }

    @AfterEach
    public void teardown() {
        wireMockServer.stop();
    }

    @Test
    @Order(1)
    void should_work_sequential_execution() throws Exception {
        final String baseUrl = "http://localhost:" + randomServerPort + "/v1/gods-sequential";

        ResponseEntity<List<String>> gods = this.restTemplate.exchange(
            baseUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});
            
        assertThat(gods.getBody()).isNotNull();
        assertThat(gods.getBody().size()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    void should_work_executor() throws Exception {
        final String baseUrl = "http://localhost:" + randomServerPort + "/v1/gods-executor";

        ResponseEntity<List<String>> gods = this.restTemplate.exchange(
            baseUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});
            
        assertThat(gods.getBody()).isNotNull();
        assertThat(gods.getBody().size()).isGreaterThan(0);
    }

    @Test
    @Order(3)
    void should_work_completable() throws Exception {
        final String baseUrl = "http://localhost:" + randomServerPort + "/v1/gods-completable";

        ResponseEntity<List<String>> gods = this.restTemplate.exchange(
            baseUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});
            
        assertThat(gods.getBody()).isNotNull();
        assertThat(gods.getBody().size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    void should_work_structural() throws Exception {
        final String baseUrl = "http://localhost:" + randomServerPort + "/v1/gods-structural";

        ResponseEntity<List<String>> gods = this.restTemplate.exchange(
            baseUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});
            
        assertThat(gods.getBody()).isNotNull();
        assertThat(gods.getBody().size()).isGreaterThan(0);
    }

    @Test
    @Order(5)
    void should_work_structural_multiple() throws Exception {
        final String baseUrl = "http://localhost:" + randomServerPort + "/v1/gods-structural-multiple";

        ResponseEntity<List<String>> gods = this.restTemplate.exchange(
            baseUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});
            
        assertThat(gods.getBody()).isNotNull();
        assertThat(gods.getBody().size()).isGreaterThan(0);
    }

    @Test
    @Order(6)
    void should_work_gatherers() throws Exception {
        final String baseUrl = "http://localhost:" + randomServerPort + "/v1/gods-gatherers";

        ResponseEntity<List<String>> gods = this.restTemplate.exchange(
            baseUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {});
            
        assertThat(gods.getBody()).isNotNull();
        assertThat(gods.getBody().size()).isGreaterThan(0);
    }
}