package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Value;

public abstract class KafkaConfig {
	@Value("${kafka.offset.reset.config:earliest}")
	protected String offsetResetConfig;

	@Value("${kafka.ssl.protocol:SASL_SSL}")
	protected String sslProtocol;

	@Value("${kafka.security.enabled}")
	protected boolean securityEnabled;

	@Value("${kafka.sasl.jaas.template.uri:org.apache.kafka.common.security.scram.ScramLoginModule}")
	protected String jaasTemplateUri;

	@Value("${kafka.sasl.jaas.config.username}")
	protected String jaasConfigUsername;

	@Value("${kafka.sasl.jaas.config.password}")
	protected String jaasConfigPassword;

	@Value("${kafka.sasl.mechanism:SCRAM-SHA-512}")
	protected String saslMechanism;

	@Value("${kafka.ssl.endpoint.identification.mechanism}")
	protected String sslEndpointIdentificationMechanism;
}
