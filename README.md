# spring-boot-completablefuture-examples

## How to build in local

```bash
sdk env install
./mvnw clean spring-boot:run
./mvnw clean verify
./mvnw clean test -Dtest=MyControllerE2ETest

//Surefire report
./mvnw clean test -Dtest=MyControllerE2ETest surefire-report:report
jwebserver -p 9000 -d "$(pwd)/target/site/"
```

## Results

```bash
should_work_sequential_execution    1.643 s
should_work_executor	            0.050 s
should_work_completable	            0.032 s
should_work_gatherers	            0.065 s
should_work_structural	            0.031 s
should_work_structural_multiple	    0.023 s
```

## Spring Boot CLI

```bash
sdk install springboot
spring init -d=web,devtools --build=maven --force ./
```
