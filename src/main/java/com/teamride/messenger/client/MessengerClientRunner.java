package com.teamride.messenger.client;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import com.teamride.messenger.client.config.KafkaConstants;
import com.teamride.messenger.client.dto.ChatMessageDTO;
import com.teamride.messenger.client.resource.ConsumerThreadPool;
import com.teamride.messenger.client.service.StompChatService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessengerClientRunner implements CommandLineRunner {

//	@Autowired
//	ConsumerThread consumerThread;
	
	@Resource(name = "stompChatService")
	private StompChatService stompChatService;
	
	@Autowired
	KafkaConsumer<String, ChatMessageDTO> consumer; 
	
	@PostConstruct
	public void init() {
		System.out.println("");
	}

	@Override
	public void run(String... args) throws Exception {
//		ConsumerThread consumerThread = new ConsumerThread();
		ConsumerThreadPool.getExecutorService().execute(()->{
			// com.teamride.messenger.server.dto.ChatMessageDTO;
			// package com.teamride.messenger.client.dto.ChatMessageDTO;
			// 경로가 다르기 때문에 역직렬화 시 같은 Entity로 인식을 못함
			// package 경로까지 보기 떄문인데 addTrustedPackages를 해줌으로서 해결
//			JsonDeserializer<ChatMessageDTO> deserializer = new JsonDeserializer<>(ChatMessageDTO.class);
//			deserializer.setRemoveTypeHeaders(false);
//			deserializer.addTrustedPackages("*");
//			deserializer.setUseTypeMapperForKey(true);
//
//			Map<String, Object> props = new HashMap<>();
//			props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKER);
//			props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.GROUP_ID);
//			props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//			props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
//			props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
//
//			KafkaConsumer<String, ChatMessageDTO> consumer = new KafkaConsumer<>(props);

			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					consumer.wakeup();
				}
			});
			consumer.subscribe(Arrays.asList(KafkaConstants.CHAT_CLIENT));
			log.info("consumer connected in thread");
			try {
				while (true) {
					ConsumerRecords<String, ChatMessageDTO> consumerRecords = consumer.poll(Duration.ofMillis(500));
					Map<TopicPartition, OffsetAndMetadata> currentOffset = new HashMap<>();

					for (ConsumerRecord<String, ChatMessageDTO> record : consumerRecords) {
						log.info("Received Msg chat-client " + record);

						currentOffset.put(new TopicPartition(record.topic(), record.partition()),
								new OffsetAndMetadata(record.offset() + 1, null));
						// 사용자들이 room id를 구독하고 있어서
						// room id 에대한 user id 조회 logic 필요 없음

						try {
							stompChatService.sendMessage(record.value());
							consumer.commitSync(currentOffset);
						} catch (CommitFailedException e) {
							log.error("commit failed");
						}
					}
				}
			} catch (WakeupException e) {
				log.error("poll() method trigger wakeupException");
			} finally {
				consumer.close();
			}
		});
	}
}
