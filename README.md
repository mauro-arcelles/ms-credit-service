# Credit Microservice

Spring Boot Webflux microservice that handles credit operations (credits, credit cards).

## Stack
- Java 11
- Spring Boot 2.x
- Spring Webflux
- Spring Cloud Config Client
- Reactive Mongodb
- Openapi contract first
- Swagger ui

## Configuration
Service connects to Config Server using:
```properties
spring.application.name=ms-credit-service
spring.config.import=optional:configserver:http://localhost:8888
```
for properties
```yaml
spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: ms-credit-service

server:
  port: 8093

application:
  config:
    customer-service-url: http://localhost:8090/api/v1/customers
```

## Swagger
http://localhost:8093/swagger-ui.html

![ms-credit-service-2025-02-11-233656](https://github.com/user-attachments/assets/4241a5dd-c87a-413d-8444-9d10136dff06)

