# spring-boot-completablefuture-examples

## How to build in local

```bash
sdk env install
./mvnw clean spring-boot:run
./mvnw clean verify
./mvnw clean test -Dtest=MyControllerE2ETest

//Surefire report
./mvnw clean test -Dtest=MyControllerE2ETest surefire-report:report
./mvnw clean test -Dtest=MyControllerE2ETest -Dspring.profiles.active=vt surefire-report:report 
jwebserver -p 9000 -d "$(pwd)/target/site/"
sudo lsof -i :9000
```

## Results

**With platform threads:**

```bash
should_work_sequential_execution    1.775 s
should_work_executor	            0.036 s
should_work_completable	            0.032 s
should_work_gatherers	            0.028 s
should_work_structural	            0.033 s
should_work_structural_multiple	    0.026 s
```

**With virutal threads:**

```bash
PENDING
```

## Spring Boot CLI

```bash
sdk install springboot
spring init -d=web,devtools --build=maven --force ./
```
