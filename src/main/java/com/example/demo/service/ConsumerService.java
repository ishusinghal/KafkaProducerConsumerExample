package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.model.BaseEvent;

@Service
public final class ConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.consumerGroupId}")
    public void consume(BaseEvent baseEvent) {
        logger.info(String.format("$$$$ => Consumed message: %s", baseEvent));
    }
}
