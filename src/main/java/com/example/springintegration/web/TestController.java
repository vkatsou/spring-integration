package com.example.springintegration.web;

import com.example.springintegration.config.KafkaMessageConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping
    public String test() {
        try {
            kafkaTemplate.send("kalispera", "test");
            return "Entaksei";
        } catch (Exception e) {
            return "Xalia";
        }
    }
}
