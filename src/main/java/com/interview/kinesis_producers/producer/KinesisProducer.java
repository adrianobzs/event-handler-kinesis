package com.interview.kinesis_producers.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

@Component
public class KinesisProducer {
    private final String STREAM_NAME = "events-stream-test";
    private final KinesisClient kinesisClient;

    @Autowired
    public KinesisProducer(KinesisClient kinesisClient) {
        this.kinesisClient = kinesisClient;
    }

    public void sendEvent(String consumidorID, String payload) {
        PutRecordRequest request = PutRecordRequest.builder()
                .streamName(STREAM_NAME)
                .partitionKey(consumidorID)
                .data(SdkBytes.fromUtf8String(payload))
                .build();

        kinesisClient.putRecord(request);
        System.out.println("[" + Thread.currentThread().getName() + "] Evento enviado: " + payload);
    }
}