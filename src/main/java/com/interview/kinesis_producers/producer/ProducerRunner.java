package com.interview.kinesis_producers.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class ProducerRunner {
    private final KinesisProducer kinesisProducer;

    @Autowired
    public ProducerRunner(KinesisProducer kinesisProducer) {
        this.kinesisProducer = kinesisProducer;
    }

    @Async
    public void runProducer(String producerId, String consumidorId, int numEvents) {
        for (int i = 1; i <= numEvents; i++) {
            kinesisProducer.sendEvent(producerId, consumidorId,"Evento " + i + " - " + producerId);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
