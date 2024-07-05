package info.jab.ms.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class MyController {

    @Value("${greek-gods-api-url}")
    private String greekAddress;

    @Value("${roman-gods-api-url}")
    private String romanAddress;

    @Value("${nordic-gods-api-url}")
    private String nordicAddress;

    @Autowired
    private RestClient restClient;

    @GetMapping("/v1/gods")
    public List<String> getGods() {
        return List.of(
                greekAddress, 
                romanAddress, 
                nordicAddress).stream()
            .flatMap(str -> fetchGods(str).stream())
            .toList();
    }

    private List<String> fetchGods(String address) {
        ResponseEntity<List<String>> gods = restClient
                .get()
                .uri(address)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {});
        return gods.getBody();
    }
    
}
