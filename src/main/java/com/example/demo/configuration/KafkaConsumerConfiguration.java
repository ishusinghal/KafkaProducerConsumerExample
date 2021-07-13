package com.example.demo.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.demo.model.BaseEvent;

@Configuration
@EnableKafka
@ConditionalOnProperty(value = "kafka.consumer.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaConsumerConfiguration extends KafkaConfig{
    @Value("${kafka.server}")
    private String server;

    @Value("${kafka.consumerGroupId}")
    private String consumerGroupId;

    @Value("${kafka.concurrency.level}")
	private Integer concurrencyLevel;

	@Value("${kafka.pollTimeout:10000}")
	private Long pollTimeout;

	@Value("${kafka.idleBetweenPolls:5000}")
	private long idleBetweenPolls;

	private static final String SSL_ENDPOINT_IDENTIFICATION_ALGORITHM = "ssl.endpoint.identification.algorithm";
	private static final String SASL_MECHANISM = "sasl.mechanism";
	private static final String SASL_JAAS_CONFIG = "sasl.jaas.config";

	private static final String ASTERIC = "*";
	
	
    @Bean
    public ProducerFactory<String, String> producerFactoryString() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateString() {
        return new KafkaTemplate<>(producerFactoryString());
    }

    @Bean
    public ConsumerFactory<String, BaseEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), new JsonDeserializer<>(BaseEvent.class));
    }

    @Bean
	public Map<String, Object> consumerConfig() {
		Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetResetConfig);
        
        if(securityEnabled) {
        	configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, sslProtocol);
        	
        	StringBuilder sbJaas= new StringBuilder();
        	sbJaas.append(jaasTemplateUri);
        	sbJaas.append(" required username=");
        	sbJaas.append(jaasConfigUsername);
        	sbJaas.append(" password=");
        	sbJaas.append(jaasConfigPassword);
        	sbJaas.append(";");
        	
        	configProps.put(SASL_JAAS_CONFIG, sbJaas.toString());
        	configProps.put(SSL_ENDPOINT_IDENTIFICATION_ALGORITHM, sslEndpointIdentificationMechanism);
        	configProps.put(SASL_MECHANISM, saslMechanism);
        }
        
    	configProps.put(JsonDeserializer.REMOVE_TYPE_INFO_HEADERS, true);
    	configProps.put(JsonDeserializer.TRUSTED_PACKAGES, ASTERIC);
    	configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		return configProps;
	}

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BaseEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BaseEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setErrorHandler(new ErrorHandler());
        factory.setMissingTopicsFatal(false);

        factory.setConcurrency(concurrencyLevel);
        factory.getContainerProperties().setPollTimeout(pollTimeout);
        factory.getContainerProperties().setIdleBetweenPolls(idleBetweenPolls);
        factory.getContainerProperties().setAckMode(AckMode.BATCH);
        return factory;
    }
}
