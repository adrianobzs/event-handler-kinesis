package com.interview.kinesis_producers.producer;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

@Component
public class KinesisProducer {
    private final String STREAM_NAME = "events-stream-test";
    public void sendEvent(String producerID, String consumidorID, String message) {
        var client = KinesisClient.builder().build();
        String payload = String.format("{\"produtorID\":\"%s\",\"consumidorID\":\"%s\",\"dummy_data\":\"%s\"}",
                producerID, consumidorID, message);

        PutRecordRequest request = PutRecordRequest.builder()
                .streamName(STREAM_NAME)
                .partitionKey(consumidorID)
                .data(SdkBytes.fromUtf8String(payload))
                .build();

        client.putRecord(request);
        System.out.println("[" + Thread.currentThread().getName() + "] Evento enviado: " + payload);
    }
}