package com.example.springintegration.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.example.springintegration.config.KafkaConsumerConfig.consumerFactory;
import static com.example.springintegration.config.KafkaProducerConfig.producerFactory;

@Component
@RequiredArgsConstructor
public class KafkaMessageConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "kalispera-2", groupId = "my-group")
    public void consume(String message) {
        System.out.println("Eftases stin defteri oura mesw spring-integration" + message);
    }

//    public ConsumerFactory<String, String> consumerFactory() {
//        Map<String, Object> properties = new HashMap<>();
//        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
//        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//        properties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
//        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//
//        return new DefaultKafkaConsumerFactory<>(properties);
//    }

    @Bean
    public IntegrationFlow testIntegrationFlow() {
        return IntegrationFlows
                .from(Kafka.messageDrivenChannelAdapter(
                        consumerFactory(),
                        "kalispera"))
                .transform(message -> {
                    // Process the message here, e.g., send it to another channel or service
                    System.out.println("Received message via Spring Integration: " + message);
                    return message;
                })
                .channel("processMessageChannel")
                .handle( message -> System.out.println("Received message via Spring Integration after switch channel: " + message.getPayload()) )
                .get();
    }

    @Bean
    public IntegrationFlow processMessageFlow() {
        return IntegrationFlows
                .from("processMessageChannel")
                .handle(Kafka.outboundChannelAdapter(producerFactory())
                                .topic("kalispera-2"))
                .get();
    }
}
