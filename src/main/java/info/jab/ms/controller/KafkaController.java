package info.jab.ms.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Value("${mykafka.topic}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public record MyRequest(String message) { }

    @PostMapping("v1/messages")
    public MyRequest publish(@RequestBody MyRequest request){
        var result = kafkaTemplate.send(topicName, request.message);

        try {
            result.get();
            return request;
        } catch (InterruptedException | ExecutionException e) {
            return new MyRequest("Katakroker");
        }
    }
}
