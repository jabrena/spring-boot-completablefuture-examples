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

    @GetMapping("/v1/async")
    public CompletableFuture<String> startAsyncTask() {
        logger.info("Request received to start async task");
        CompletableFuture<String> future = executeAsyncTask();
        return future;
    }

    @Async
    public CompletableFuture<String> executeAsyncTask() {
        logger.info("Start executing async task");
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);  // Simulate a long-running task
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Task interrupted", e);
            }
            logger.info("Finished executing async task");
            return "Async task completed";
        });
    }
}
