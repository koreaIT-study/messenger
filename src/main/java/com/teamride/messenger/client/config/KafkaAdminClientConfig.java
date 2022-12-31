package com.teamride.messenger.client.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class KafkaAdminClientConfig {

    @Bean
    public AdminClient adminClient() {
        // AdminClient 는 객체 생성 후 종료시 닫아줘야 하는 객체
        try (AdminClient adminClient = KafkaAdminClient.create(getAdminClientProps())) {
            DescribeClusterResult clusterResult = adminClient.describeCluster();
            Collection<Node> brokers = clusterResult.nodes()
                .get();

            log.info("brokers:: {}", brokers.toString());

            // topic 생성
            // topicName, numPartitions, replicationFactor
            final NewTopic chatClient = new NewTopic(KafkaConstants.CHAT_CLIENT, 100, (short) 1);
            final NewTopic chatServer = new NewTopic(KafkaConstants.CHAT_SERVER, 100, (short) 1);
            final NewTopic chatInput = new NewTopic(KafkaConstants.CHAT_INPUT, 100, (short) 1);
            CreateTopicsResult createTopicsResult = adminClient.createTopics(Arrays.asList(chatClient, chatServer, chatInput));

            for (Entry<String, KafkaFuture<Void>> entry : createTopicsResult.values()
                .entrySet()) {
                log.info("Create topic :: {}, value :: {}", entry.getKey(), entry.getValue());
            }

            DescribeTopicsResult describeTopicsResult = adminClient
                .describeTopics(Arrays.asList(KafkaConstants.CHAT_CLIENT, KafkaConstants.CHAT_SERVER, KafkaConstants.CHAT_INPUT));

            for (Entry<String, KafkaFuture<TopicDescription>> entry : describeTopicsResult.topicNameValues()
                .entrySet()) {
                TopicDescription description = entry.getValue()
                    .get(); // furue 동기화 
                log.info("topic Name is {}, description is :: {}", description.name(), description.toString());
            }
        } catch (Exception e) {
            log.error("kafka admin client error :: {}", e);
        }

        return null;
    }

    private Properties getAdminClientProps() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKER);

        return props;
    }
}
