package com.interview.kinesis_producers.producer;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.text.DecimalFormat;

@Component
public class PayloadGenerator {
    private final Random random = new Random();
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private int eventCounter = 1;

    public String generatePayload(String producerID, String consumerID, Integer eventType) {
        if ("lambda1".equals(consumerID)) {
            return generateLambda1Payload(producerID, consumerID);
        } else if ("lambda2".equals(consumerID)) {
            if (eventType == 1) {
                return generateLambda2Type1Payload(producerID, consumerID);
            } else if (eventType == 2) {
                return generateLambda2Type2Payload(producerID, consumerID);
            }
        }
        throw new IllegalArgumentException("Combinação inválida de consumerID e eventType");
    }

    private String generateLambda1Payload(String producerID, String consumerID) {
        return String.format(
                "{\"produtorID\":\"%s\",\"consumidorID\":\"%s\",\"dummy_data\":\"Evento %d - %s\"}",
                producerID, consumerID, eventCounter++, producerID
        );
    }

    private String generateLambda2Type1Payload(String producerID, String consumerID) {
        int value = Math.abs(random.nextInt() % 1000);
        return String.format(
                "{\"type_event\":1,\"produtorID\":\"%s\",\"consumidorID\":\"%s\"," +
                        "\"origem\":\"conta_%d\",\"destino\":\"conta_%d\",\"valor\":%d}",
                producerID, consumerID,
                eventCounter, eventCounter + 1000,
                value
        );
    }

    private String generateLambda2Type2Payload(String producerID, String consumerID) {
        return String.format(
                "{\"type_event\":2,\"produtorID\":\"%s\",\"consumidorID\":\"%s\"," +
                        "\"nome\":\"Cliente %d\",\"telefone\":\"+55119%07d\"}",
                producerID, consumerID,
                eventCounter,
                1000000 + eventCounter++
        );
    }
}