package info.jab.ms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class AsyncController {

    private static final Logger logger = LoggerFactory.getLogger(AsyncController.class);

    private final int seconds = 5;

    @Async
    @GetMapping("/v1/async")
    public CompletableFuture<String> startAsyncTask() {
        logger.info("Start executing async task");
        return CompletableFuture.supplyAsync(() -> {
            delay(seconds);
            return "Async task completed";
        });
    }

    private void delay(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex) { 
            //Empty on purpose
        }
    }
}
