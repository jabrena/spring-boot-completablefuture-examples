package info.jab.ms.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import info.jab.ms.config.ProfileService;

import java.util.List;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;
import java.util.concurrent.CancellationException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class BlockinController {

    private static final Logger logger = LoggerFactory.getLogger(BlockinController.class);

    @Value("${greek-gods-api-url}/greek")
    private String greekAddress;

    @Value("${roman-gods-api-url}")
    private String romanAddress;

    @Value("${nordic-gods-api-url}")
    private String nordicAddress;

    @Autowired
    private RestClient restClient;

    @Autowired
    private ExecutorService executor;

    @Autowired
    private ProfileService profileService;

    Function<String, Stream<String>> fetchGods = (address) -> {
        logger.info(Thread.currentThread().getName());
        ResponseEntity<List<String>> gods = restClient
                .get()
                .uri(address)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {});
        return gods.getBody().stream();
    };

    Function<String, Future<Stream<String>>> asyncCall = (param) -> {
        Callable<Stream<String>> task = () -> fetchGods.apply(param);
        return executor.submit(task);
    };

    Function<String, CompletableFuture<Stream<String>>> asyncCall2 = (param) -> {
        CompletableFuture<Stream<String>> cf1 = CompletableFuture
                .supplyAsync(() -> fetchGods.apply(param), executor);
        return cf1;
    };

    Function<String, Stream<String>> fetchGodsStructuredPT = param -> {
        ThreadFactory threadFactory = Thread.ofPlatform().factory();
        try (var scope = new StructuredTaskScope<Stream<String>>("jab-pt", threadFactory)) {
            var subtask1 = scope.fork(() -> fetchGods.apply(param));
            scope.join();
            return subtask1.get();
        } catch (InterruptedException ex) {
            logger.warn(ex.getLocalizedMessage(), ex);
            throw new CancellationException(ex.getMessage());
        }
    };

    Function<String, Stream<String>> fetchGodsStructuredVT = param -> {
        ThreadFactory threadFactory = Thread.ofVirtual().factory();
        try (var scope = new StructuredTaskScope<Stream<String>>("jab-vt", threadFactory)) {
            var subtask1 = scope.fork(() -> fetchGods.apply(param));
            scope.join();
            return subtask1.get();
        } catch (InterruptedException ex) {
            logger.warn(ex.getLocalizedMessage(), ex);
            throw new CancellationException(ex.getMessage());
        }
    };

    Function<List<String>, List<String>> fetchGodsStructured2VT = param -> {
        ThreadFactory threadFactory = Thread.ofVirtual().factory();
        try (var scope = new StructuredTaskScope<>("jab-vt", threadFactory)) {
            List<Subtask<Stream<String>>> subtaskList = param.stream()
                .map(sup -> {
                    Subtask<Stream<String>> subTask = scope.fork(() -> fetchGods.apply(sup));
                    return subTask;
                })
                .toList();
            scope.join();
            return subtaskList.stream().flatMap(Subtask::get).toList();
        } catch (InterruptedException ex) {
            logger.warn(ex.getLocalizedMessage(), ex);
            throw new CancellationException(ex.getMessage());
        }
    };

    Function<List<String>, List<String>> fetchGodsStructured2PT = param -> {
        ThreadFactory threadFactory = Thread.ofPlatform().factory();
        try (var scope = new StructuredTaskScope<>("jab-pt", threadFactory)) {
            List<Subtask<Stream<String>>> subtaskList = param.stream()
                .map(sup -> {
                    Subtask<Stream<String>> subTask = scope.fork(() -> fetchGods.apply(sup));
                    return subTask;
                })
                .toList();
            scope.join();
            return subtaskList.stream().flatMap(Subtask::get).toList();
        } catch (InterruptedException ex) {
            logger.warn(ex.getLocalizedMessage(), ex);
            throw new CancellationException(ex.getMessage());
        }
    };

    @GetMapping("/v1/gods-sequential")
    public List<String> getGods1() {
        System.out.println(greekAddress);
        
        try {
            Thread.sleep(1000 * 1000);
        } catch (InterruptedException ex) { 
            //Empty on purpose
        }

        return List.of(greekAddress, romanAddress, nordicAddress).stream()
            .flatMap(fetchGods)
            .toList();
    }

    @GetMapping("/v1/gods-executor")
    public List<String> getGods2() {
        return List.of(greekAddress, romanAddress, nordicAddress).stream()
            .map(asyncCall)
            .flatMap(future -> {
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException ex) {
                    throw new RuntimeException(ex);
                }
            })
            .toList();
    }

    @GetMapping("/v1/gods-completable")
    public List<String> getGods3() {
        return List.of(greekAddress, romanAddress, nordicAddress).stream()
            .map(asyncCall2)
            .flatMap(CompletableFuture::join)
            .toList();
    }

    @GetMapping("/v1/gods-structural")
    public List<String> getGods4() {
        var function = profileService.getActiveProfiles().contains("vt") 
            ? fetchGodsStructuredVT 
            : fetchGodsStructuredPT;

        return List.of(greekAddress, romanAddress, nordicAddress).stream()
            .flatMap(function)
            .toList();
    }

    @GetMapping("/v1/gods-structural-multiple")
    public List<String> getGods5() {
        var function = profileService.getActiveProfiles().contains("vt") 
            ? fetchGodsStructured2VT 
            : fetchGodsStructured2PT;

        var list = List.of(greekAddress, romanAddress, nordicAddress);
        return function.apply(list);
    }

    @GetMapping("/v1/gods-gatherers")
    public List<String> getGods6() {
        return List.of(greekAddress, romanAddress, nordicAddress).stream()
            .gather(Gatherers.mapConcurrent(8, str -> fetchGods.apply(str)))
            .flatMap(Function.identity())
            .toList();
    }
    
}
