# spring-boot-completablefuture-examples

## How to build in local

```bash
sdk env install
./mvnw clean spring-boot:run
./mvnw clean verify
./mvnw clean test -Dtest=AsyncControllerE2ETest

//Surefire report
./mvnw clean test -Dtest=BlockingControllerE2ETest surefire-report:report
jwebserver -p 9000 -d "$(pwd)/target/site/"
./mvnw clean test -Dtest=BlockingControllerE2ETest -Dspring.profiles.active=vt surefire-report:report 
jwebserver -p 9000 -d "$(pwd)/target/site/"
sudo lsof -i :9000
```

## Results

**With platform threads:**

```bash
2024-07-12T16:25:52.881Z  INFO 7147 --- [async-examples] [o-auto-1-exec-1] i.jab.ms.controller.BlockinController    : http-nio-auto-1-exec-1
2024-07-12T16:25:53.212Z  INFO 7147 --- [async-examples] [o-auto-1-exec-1] i.jab.ms.controller.BlockinController    : http-nio-auto-1-exec-1
2024-07-12T16:25:53.220Z  INFO 7147 --- [async-examples] [o-auto-1-exec-1] i.jab.ms.controller.BlockinController    : http-nio-auto-1-exec-1
2024-07-12T16:25:53.326Z  INFO 7147 --- [async-examples] [pool-2-thread-1] i.jab.ms.controller.BlockinController    : pool-2-thread-1
2024-07-12T16:25:53.334Z  INFO 7147 --- [async-examples] [pool-2-thread-1] i.jab.ms.controller.BlockinController    : pool-2-thread-1
2024-07-12T16:25:53.340Z  INFO 7147 --- [async-examples] [pool-2-thread-1] i.jab.ms.controller.BlockinController    : pool-2-thread-1
2024-07-12T16:25:53.365Z  INFO 7147 --- [async-examples] [pool-2-thread-1] i.jab.ms.controller.BlockinController    : pool-2-thread-1
2024-07-12T16:25:53.372Z  INFO 7147 --- [async-examples] [pool-2-thread-1] i.jab.ms.controller.BlockinController    : pool-2-thread-1
2024-07-12T16:25:53.378Z  INFO 7147 --- [async-examples] [pool-2-thread-1] i.jab.ms.controller.BlockinController    : pool-2-thread-1
2024-07-12T16:25:53.403Z  INFO 7147 --- [async-examples] [      Thread-34] i.jab.ms.controller.BlockinController    : Thread-34
2024-07-12T16:25:53.410Z  INFO 7147 --- [async-examples] [      Thread-35] i.jab.ms.controller.BlockinController    : Thread-35
2024-07-12T16:25:53.416Z  INFO 7147 --- [async-examples] [      Thread-36] i.jab.ms.controller.BlockinController    : Thread-36
2024-07-12T16:25:53.442Z  INFO 7147 --- [async-examples] [      Thread-46] i.jab.ms.controller.BlockinController    : Thread-46
2024-07-12T16:25:53.442Z  INFO 7147 --- [async-examples] [      Thread-45] i.jab.ms.controller.BlockinController    : Thread-45
2024-07-12T16:25:53.443Z  INFO 7147 --- [async-examples] [      Thread-47] i.jab.ms.controller.BlockinController    : Thread-47
2024-07-12T16:25:53.473Z  INFO 7147 --- [async-examples] [    virtual-107] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:25:53.475Z  INFO 7147 --- [async-examples] [    virtual-109] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:25:53.476Z  INFO 7147 --- [async-examples] [    virtual-110] i.jab.ms.controller.BlockinController    : 

should_work_sequential_execution	1.736 s
should_work_executor	            0.038 s
should_work_completable	            0.036 s
should_work_structural	            0.037 s
should_work_structural_multiple	    0.027 s
should_work_gatherers	            0.030 s
```

**With virutal threads:**

```bash
2024-07-12T16:26:59.800Z  INFO 7603 --- [async-examples] [     virtual-35] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2024-07-12T16:26:59.822Z  INFO 7603 --- [async-examples] [     virtual-35] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.133Z  INFO 7603 --- [async-examples] [     virtual-35] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.142Z  INFO 7603 --- [async-examples] [     virtual-35] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.258Z  INFO 7603 --- [async-examples] [     virtual-52] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.268Z  INFO 7603 --- [async-examples] [     virtual-54] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.273Z  INFO 7603 --- [async-examples] [     virtual-55] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.298Z  INFO 7603 --- [async-examples] [     virtual-65] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.305Z  INFO 7603 --- [async-examples] [     virtual-67] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.309Z  INFO 7603 --- [async-examples] [     virtual-68] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.335Z  INFO 7603 --- [async-examples] [     virtual-78] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.342Z  INFO 7603 --- [async-examples] [     virtual-80] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.347Z  INFO 7603 --- [async-examples] [     virtual-81] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.368Z  INFO 7603 --- [async-examples] [     virtual-91] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.369Z  INFO 7603 --- [async-examples] [     virtual-92] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.370Z  INFO 7603 --- [async-examples] [     virtual-93] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.398Z  INFO 7603 --- [async-examples] [    virtual-108] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.400Z  INFO 7603 --- [async-examples] [    virtual-109] i.jab.ms.controller.BlockinController    : 
2024-07-12T16:27:00.401Z  INFO 7603 --- [async-examples] [    virtual-110] i.jab.ms.controller.BlockinController    : 

should_work_sequential_execution	1.624 s
should_work_executor	            0.040 s
should_work_completable	            0.035 s
should_work_structural	            0.033 s
should_work_structural_multiple	    0.026 s
should_work_gatherers	            0.028 s
```

## Spring Boot CLI

```bash
sdk install springboot
spring init -d=web,devtools --build=maven --force ./
```

## References

- https://github.com/jabrena/latency-problems
- https://hub.docker.com/r/confluentinc/cp-zookeeper/tags
- https://hub.docker.com/r/confluentinc/cp-kafka