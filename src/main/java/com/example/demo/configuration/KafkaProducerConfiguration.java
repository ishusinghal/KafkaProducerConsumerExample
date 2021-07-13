package com.example.demo.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "kafka.producer.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaProducerConfiguration extends KafkaConfig{

	
//	@Value("${kafka.topic.creation.enabled}")
//	private boolean topicCreationEnabled;
	
}
