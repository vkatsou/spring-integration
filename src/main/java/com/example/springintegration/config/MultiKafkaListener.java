package com.example.springintegration.config;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id= "1", topics = "kalispera")
public class MultiKafkaListener {

    @KafkaHandler
    public void handleGreeting(String message) {
        System.out.println("String received: " + message);
    }

//    @KafkaHandler
//    public void handleGreeting(Greeting greeting) {
//        System.out.println("Greeting received: " + greeting);
//    }
}
