package com.interview.kinesis_producers;

import com.interview.kinesis_producers.producer.PayloadGenerator;
import com.interview.kinesis_producers.producer.ProducerRunner;
import com.interview.kinesis_producers.producer.KinesisProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProducerRunnerTest {

    @Mock
    private KinesisProducer kinesisProducer;
    @Mock
    private PayloadGenerator payloadGenerator;
    @InjectMocks
    private ProducerRunner producerRunner;

    @Test
    public void shouldSendCorrectNumberOfEvents() {
        String producerId = "p1";
        String consumerId = "lambda1";
        int numEvents = 3;
        String mockPayload = "payload-mock";

        when(payloadGenerator.generatePayload(eq(producerId), eq(consumerId), isNull()))
                .thenReturn(mockPayload);

        producerRunner.runProducer(producerId, consumerId, numEvents, null);

        verify(kinesisProducer, times(numEvents))
                .sendEvent(eq(consumerId), eq(mockPayload)); // Corrigido para verificar consumerId + payload
    }
}