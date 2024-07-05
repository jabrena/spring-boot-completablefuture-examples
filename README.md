# spring-boot-completablefuture-examples

## How to build in local

```bash
sdk env install
./mvnw clean spring-boot:run
./mvnw clean verify
./mvnw clean test -Dtest=MyControllerE2ETest
```

## Spring Boot CLI

```bash
sdk install springboot
spring init -d=web,devtools --build=maven --force ./
```
