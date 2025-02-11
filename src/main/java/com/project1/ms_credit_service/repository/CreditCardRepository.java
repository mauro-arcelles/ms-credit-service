package com.project1.ms_credit_service.repository;

import com.project1.ms_credit_service.model.CreditCardResponse;
import com.project1.ms_credit_service.model.CreditResponse;
import com.project1.ms_credit_service.model.entity.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {
    Flux<CreditResponse> findByCustomerId(String customerId);
    Mono<CreditCardResponse> findByCardNumber(String cardNumber);
}
