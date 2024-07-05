package info.jab.ms.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@ConfigurationProperties(prefix = "my.app.external")  // Define a property prefix
public class MyController {

    public record God(String name) { }

    //https://my-json-server.typicode.com/jabrena/latency-problems/greek
    //https://my-json-server.typicode.com/jabrena/latency-problems/roman
    //https://my-json-server.typicode.com/jabrena/latency-problems/nordic

/*
 * [
  "Zeus",
  "Hera",
  "Poseidon",
  "Demeter",
  "Ares",
  "Athena",
  "Apollo",
  "Artemis",
  "Hephaestus",
  "Aphrodite",
  "Hermes",
  "Dionysus",
  "Hades",
  "Hypnos",
  "Nike",
  "Janus",
  "Nemesis",
  "Iris",
  "Hecate",
  "Tyche"
]
 */

    @Value("${my.app.external.gods-api-url}")   // Inject property with @Value
    private String address;

    @Autowired
    private RestClient restClient;  // Autowire RestClient bean

    @GetMapping("/v1/gods")
    public List<String> getGods() {
        ResponseEntity<List<String>> gods = restClient
                .get()
                .uri(address)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {});
        return gods.getBody();
    }
    
}
