package com.polarbookshop.dispatcherservice;

import com.polarbookshop.dispatcherservice.dto.OrderAcceptedMessage;
import com.polarbookshop.dispatcherservice.dto.OrderDispatchedMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@FunctionalSpringBootTest
class DispatchingFunctionsIntegrationTest {
    @Autowired
    private FunctionCatalog functionCatalog;

    @Test
    void packAndLabelOrder() {
        final Function<OrderAcceptedMessage, Flux<OrderDispatchedMessage>> packAndLabel = functionCatalog
                .lookup(Function.class, "pack|label");

        final long orderId = 121;

        StepVerifier.create(packAndLabel.apply(new OrderAcceptedMessage(orderId)))
                .expectNextMatches(dispatchedOrder -> dispatchedOrder.equals(new OrderDispatchedMessage(orderId)))
                .verifyComplete();
    }

    @Test
    void packOrder() {
        final Function<OrderAcceptedMessage, Long> pack = functionCatalog.lookup("pack");
        final long orderId = 121;
        assertThat(pack.apply(new OrderAcceptedMessage(orderId))).isEqualTo(orderId);
    }

    @Test
    void labelOrder() {
        final Function<Flux<Long>, Flux<OrderDispatchedMessage>> label = functionCatalog.lookup("label");
        final Flux<Long> orderId = Flux.just(121L);

        StepVerifier.create(label.apply(orderId))
                .expectNextMatches(dispatchedOrder -> dispatchedOrder.equals(new OrderDispatchedMessage(121L)))
                .verifyComplete();
    }
}
