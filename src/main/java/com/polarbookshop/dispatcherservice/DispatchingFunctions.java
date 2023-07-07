package com.polarbookshop.dispatcherservice;

import com.polarbookshop.dispatcherservice.dto.OrderAcceptedMessage;
import com.polarbookshop.dispatcherservice.dto.OrderDispatchedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Slf4j
@Configuration
public class DispatchingFunctions {

    @Bean
    public Function<OrderAcceptedMessage, Long> pack() {
        return orderAcceptedMessage -> {
            final Long orderId = orderAcceptedMessage.orderId();
            log.info("Order packed. [order-id={}]", orderId);
            return orderId;
        };
    }

    @Bean
    public Function<Flux<Long>, Flux<OrderDispatchedMessage>> label() {
        return orderFlux -> orderFlux.map(orderId -> {
            log.info("Order labeled. [order-id={}]", orderId);
            return new OrderDispatchedMessage(orderId);
        });
    }
}
