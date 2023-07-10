package com.polarbookshop.dispatcherservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polarbookshop.dispatcherservice.dto.OrderAcceptedMessage;
import com.polarbookshop.dispatcherservice.dto.OrderDispatchedMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.io.IOException;

@SpringBootTest
public class FunctionsStreamIntegrationTests {
    @Autowired
    private InputDestination inputDestination;

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenOrderAcceptedThenDispatch() throws IOException {
        final long orderId = 121;
        final Message<OrderAcceptedMessage> inputMessage = MessageBuilder.withPayload(new OrderAcceptedMessage(orderId))
                .build();
        final OrderDispatchedMessage expectedOutputMessage = new OrderDispatchedMessage(orderId);

        inputDestination.send(inputMessage);
        Assertions.assertThat(objectMapper.readValue(outputDestination.receive().getPayload(), OrderDispatchedMessage.class))
                .isEqualTo(expectedOutputMessage);
    }
}
