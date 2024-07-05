# spring-boot-completablefuture-examples

## How to build in local

```bash
./mvnw clean spring-boot:run
./mvnw clean verify
./mvnw clean test -Dtest=MyControllerE2ETest
```

## Spring Boot CLI

```bash
sdk install springboot
spring init -d=web,devtools --build=maven --force ./
```
