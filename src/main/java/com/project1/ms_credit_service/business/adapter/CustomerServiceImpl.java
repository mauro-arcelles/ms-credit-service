package com.project1.ms_credit_service.business.adapter;

import com.project1.ms_credit_service.exception.CustomerNotFoundException;
import com.project1.ms_credit_service.model.CustomerResponse;
import com.project1.ms_credit_service.model.ResponseBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private WebClient webClient;

    @Override
    public Mono<CustomerResponse> getCustomerById(String id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        response.bodyToMono(ResponseBase.class)
                                .flatMap(error -> Mono.error(new CustomerNotFoundException(error.getMessage()))))
                .bodyToMono(CustomerResponse.class);
    }
}
