package com.teamride.messenger.client;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessengerClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(MessengerClientApplication.class, args);
	}

}
