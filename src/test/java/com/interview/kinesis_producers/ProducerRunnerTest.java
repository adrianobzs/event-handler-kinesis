package com.interview.kinesis_producers;

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

    @InjectMocks
    private ProducerRunner producerRunner;

    @Test
    public void shouldSendCorrectNumberOfEvents() throws InterruptedException {
        String producerId = "p1";
        String consumerId = "lambda1";
        int numEvents = 3;

        producerRunner.runProducer(producerId, consumerId, numEvents);

        verify(kinesisProducer, times(numEvents))
                .sendEvent(eq(producerId), eq(consumerId), anyString());
    }

    @Test
    public void shouldSendEventsWithCorrectFormat() {
        String producerId = "p1";
        String consumerId = "lambda1";
        int numEvents = 2;

        producerRunner.runProducer(producerId, consumerId, numEvents);

        verify(kinesisProducer, times(numEvents)).sendEvent(
                eq(producerId),
                eq(consumerId),
                argThat(message -> message.matches("Evento \\d+ - " + producerId))
        );
    }
}