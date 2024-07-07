package info.jab.ms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public record MyRequest(String message) { }

    @PostMapping("v1/messages")
    public MyRequest publish(@RequestBody MyRequest request){
        var result = kafkaTemplate.send("example_topic2", request.message);
        result.join();
        return request;
    }
}
