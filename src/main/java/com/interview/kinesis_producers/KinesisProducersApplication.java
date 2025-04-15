package com.interview.kinesis_producers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.interview.kinesis_producers.producer.ProducerRunner;

@SpringBootApplication
public class KinesisProducersApplication implements CommandLineRunner {
	private final String[] produtores = {"p1", "p2"};
	private final String[] consumidores = {"lambda1", "lambda2"};
	private final ProducerRunner producerRunner;

	public KinesisProducersApplication(ProducerRunner producerRunner) {
		this.producerRunner = producerRunner;
	}

	public static void main(String[] args) {
		SpringApplication.run(KinesisProducersApplication.class, args);
	}

	@Override
	public void run(String... args) {
		producerRunner.runProducer(produtores[0], consumidores[0],1, null);
		producerRunner.runProducer(produtores[1], consumidores[0], 1, null);
		producerRunner.runProducer(produtores[0], consumidores[1], 1, 1); // financeiro
		producerRunner.runProducer(produtores[1], consumidores[1], 1, 2); // contato
	}
}