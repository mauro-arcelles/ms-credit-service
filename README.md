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
eureka:
  instance:
    hostname: localhost
    instance-id: ${spring.application.name}:${random.int}
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
      
spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: ms-bootcamp-arcelles

server:
  port: ${PORT:0}

application:
  config:
    customer-service-url: http://ms-customer-service/api/v1/customers
    credits:
      credit-cards:
        monthlyPaymentDay: 26
      credit:
        monthlyPaymentDay: 20

management:
  endpoints:
    web:
      exposure:
        include: health,circuitbreakerevents
  endpoint:
    health:
      show-details: always

resilience4j:
  circuitbreaker:
    instances:
      customerService:
        slidingWindowSize: 3
        failureRateThreshold: 100
        waitDurationInOpenState: 10000
        permittedNumberOfCallsInHalfOpenState: 3
        ignoreExceptions:
          - com.project1.ms_credit_service.exception.NotFoundException
          - com.project1.ms_credit_service.exception.BadRequestException
  timelimiter:
    instances:
      customerService:
        timeoutDuration: 2s
        cancelRunningFuture: true

springdoc:
  api-docs:
    path: /credit-docs/v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
  webjars:
    prefix:
```

## Swagger
http://localhost:8093/swagger-ui.html

![ms-credit-service-2025-03-14-155304](https://github.com/user-attachments/assets/d2303ba0-942a-42cc-8dcf-ae1984751ab8)


