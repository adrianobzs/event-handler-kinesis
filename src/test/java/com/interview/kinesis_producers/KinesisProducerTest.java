package com.interview.kinesis_producers;

import com.interview.kinesis_producers.producer.KinesisProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KinesisProducerTest {

    @Mock
    private KinesisClient kinesisClient;

    @InjectMocks
    private KinesisProducer kinesisProducer;

    @Test
    void shouldSendEventToKinesisWithCorrectPayload() {
        String producerId = "p1";
        String consumerId = "lambda1";
        String message = "test-message";
        String expectedPayload = String.format(
                "{\"produtorID\":\"%s\",\"consumidorID\":\"%s\",\"dummy_data\":\"%s\"}",
                producerId, consumerId, message
        );

        when(kinesisClient.putRecord(any(PutRecordRequest.class)))
                .thenReturn(PutRecordResponse.builder().build());

        kinesisProducer.sendEvent(producerId, consumerId, message);

        verify(kinesisClient).putRecord(argThat((PutRecordRequest request) ->
                request.streamName().equals("events-stream-test") &&
                        request.partitionKey().equals(consumerId) &&
                        request.data().asUtf8String().equals(expectedPayload)
        ));
    }

    @Test
    void shouldUseConsumerIdAsPartitionKey() {
        String consumerId = "specific-partition-key";
        when(kinesisClient.putRecord(any(PutRecordRequest.class)))
                .thenReturn(PutRecordResponse.builder().build());

        kinesisProducer.sendEvent("p1", consumerId, "msg");

        verify(kinesisClient).putRecord(argThat((PutRecordRequest request) ->
                request.partitionKey().equals(consumerId)
        ));
    }
}