package br.com.incode.orderservice.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;

@Configuration
public class KafkaAdminConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    KafkaAdmin kafkaAdmin() {
        var configurations = new HashMap<String, Object>();
        configurations.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configurations);
    }

    @Bean
    KafkaAdmin.NewTopics newTopics() {
        return new KafkaAdmin.NewTopics(TopicBuilder.name("notification-topic").partitions(2).build());
    }
}
