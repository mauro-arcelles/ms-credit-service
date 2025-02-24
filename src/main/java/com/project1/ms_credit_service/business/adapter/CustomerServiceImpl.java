package com.project1.ms_credit_service.business.adapter;

import com.project1.ms_credit_service.exception.BadRequestException;
import com.project1.ms_credit_service.exception.NotFoundException;
import com.project1.ms_credit_service.model.CustomerResponse;
import com.project1.ms_credit_service.model.ResponseBase;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private WebClient webClient;

    @CircuitBreaker(name = "customerService", fallbackMethod = "getCustomerByIdFallback")
    @TimeLimiter(name = "customerService")
    @Override
    public Mono<CustomerResponse> getCustomerById(String id) {
        return webClient.get()
            .uri("/{id}", id)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, response ->
                response.bodyToMono(ResponseBase.class)
                    .flatMap(error -> Mono.error(
                        response.statusCode().equals(HttpStatus.NOT_FOUND)
                            ? new NotFoundException(error.getMessage())
                            : new BadRequestException(error.getMessage())
                    ))
            )
            .bodyToMono(CustomerResponse.class);
    }

    private Mono<CustomerResponse> getCustomerByIdFallback(String id, Exception e) {
        return Mono.error(new BadRequestException("Customer service unavailable. Retry again later"));
    }
}
