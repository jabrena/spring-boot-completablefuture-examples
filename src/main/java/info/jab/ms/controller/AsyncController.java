package info.jab.ms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@RestController
public class AsyncController {

    private static final Logger logger = LoggerFactory.getLogger(AsyncController.class);

    private final int seconds = 5;

    private void delay(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex) { 
            //Empty on purpose
        }
    }

    private static final String asyncMessage = "Async task completed";

    @Autowired
    private ExecutorService executor;

    @GetMapping("/v1/async-result")
    public Future<String> asyncResult() {
        logger.info("Using AsyncResult (Deprecated)");
        delay(seconds);
        return new AsyncResult<String>(asyncMessage);
    }

    public Future<String> asyncProcessF() {        
        return executor.submit(() -> {
            delay(seconds);
            return asyncMessage;
        });
    }

    //Not working
    @GetMapping("/v1/async-future")
    public Future<String> asyncFuture() {
        logger.info("Future Example");
        return asyncProcessF();
    }

    @Async
    private CompletableFuture<String> asyncProcessCF() {
        return CompletableFuture.supplyAsync(() -> {
            delay(seconds);
            return asyncMessage;
        });
    }

    @GetMapping("/v1/async-future2")
    public Future<String> asyncFuture2() {
        logger.info("Future from CompletableFuture Example");
        return asyncProcessCF();
    }

    //Not working
    @GetMapping("/v1/async-future3")
    public Future<String> asyncFuture3() {
        logger.info("Future from FutureTask Example");
        return new FutureTask<String>(() -> {
            delay(seconds);
            return asyncMessage;
        });
    }

    @GetMapping("/v1/async-cf")
    public CompletableFuture<String> asyncCF() {
        logger.info("CompletableFuture Example");
        return asyncProcessCF();
    }
}
