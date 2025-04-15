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
    void sendEventToKinesisWithCorrectPayload() {
        String consumerId = "lambda1";
        String producerId = "p1";

        when(kinesisClient.putRecord(any(PutRecordRequest.class)))
                .thenReturn(PutRecordResponse.builder().build());

        kinesisProducer.sendEvent(consumerId,
                "{\"eventId\":\"123\",\"produtorID\":\"p1\",\"consumidorID\":\"lambda1\",\"dummy_data\":\"test\"}");

        verify(kinesisClient).putRecord(argThat((PutRecordRequest request) ->
                request.streamName().equals("events-stream-test") &&
                        request.partitionKey().equals(consumerId) &&
                        request.data().asUtf8String().contains("\"consumidorID\":\"lambda1\"") &&
                        request.data().asUtf8String().contains("\"dummy_data\":\"test\"") &&
                        request.data().asUtf8String().matches(".*\"eventId\":\"[a-f0-9-]+\".*")
        ));
    }

    @Test
    void sendFinancialEventToKinesisWithCorrectPayload() {
        // Arrange
        String consumerId = "lambda2";
        String producerId = "p1";
        String payload = "{\"eventId\":\"456\",\"type_event\":1,\"produtorID\":\"p1\",\"consumidorID\":\"lambda2\"," +
                "\"origem\":\"conta_1\",\"destino\":\"conta_2\",\"valor\":100.50}";

        when(kinesisClient.putRecord(any(PutRecordRequest.class)))
                .thenReturn(PutRecordResponse.builder().build());

        // Act
        kinesisProducer.sendEvent(consumerId, payload);

        // Assert
        verify(kinesisClient).putRecord(argThat((PutRecordRequest request) ->
                request.streamName().equals("events-stream-test") &&
                        request.partitionKey().equals(consumerId) &&
                        request.data().asUtf8String().contains("\"type_event\":1") &&
                        request.data().asUtf8String().contains("\"origem\":\"conta_1\"") &&
                        request.data().asUtf8String().contains("\"valor\":100.50") &&
                        request.data().asUtf8String().matches(".*\"eventId\":\"[a-f0-9-]+\".*")
        ));
    }

    @Test
    void sendContactEventToKinesisWithCorrectPayload() {
        // Arrange
        String consumerId = "lambda2";
        String producerId = "p1";
        String payload = "{\"eventId\":\"789\",\"type_event\":2,\"produtorID\":\"p1\",\"consumidorID\":\"lambda2\"," +
                "\"nome\":\"João Silva\",\"telefone\":\"+5511999998888\"}";

        when(kinesisClient.putRecord(any(PutRecordRequest.class)))
                .thenReturn(PutRecordResponse.builder().build());

        // Act
        kinesisProducer.sendEvent(consumerId, payload);

        // Assert
        verify(kinesisClient).putRecord(argThat((PutRecordRequest request) ->
                request.streamName().equals("events-stream-test") &&
                        request.partitionKey().equals(consumerId) &&
                        request.data().asUtf8String().contains("\"type_event\":2") &&
                        request.data().asUtf8String().contains("\"nome\":\"João Silva\"") &&
                        request.data().asUtf8String().contains("\"telefone\":\"+5511999998888\"") &&
                        request.data().asUtf8String().matches(".*\"eventId\":\"[a-f0-9-]+\".*")
        ));
    }
}