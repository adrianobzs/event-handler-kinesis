package com.interview.kinesis_producers.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class ProducerRunner {
    private final KinesisProducer kinesisProducer;
    private final PayloadGenerator payloadGenerator;

    @Autowired
    public ProducerRunner(KinesisProducer kinesisProducer, PayloadGenerator payloadGenerator) {
        this.kinesisProducer = kinesisProducer;
        this.payloadGenerator = payloadGenerator;
    }

    @Async
    public void runProducer(String producerId, String consumerId, int numEvents, Integer eventType) {
        for (int i = 0; i < numEvents; i++) {
            String payload = payloadGenerator.generatePayload(producerId, consumerId, eventType);
            kinesisProducer.sendEvent(consumerId, payload);
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}