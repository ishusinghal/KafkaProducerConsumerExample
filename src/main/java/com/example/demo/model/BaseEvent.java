package com.example.demo.model;

import java.util.Map;

import lombok.Data;

@Data
public class BaseEvent {
	private String type;
	private String topic;
	private Map<String, String> data;
//	private Map<String, String> headers;
	private String description;
//	private String guid;
//	private Long timestamp;

	public BaseEvent() {

	}

	public BaseEvent(String type, String topic, Map<String, String> data, String description) {
		super();
		this.type = type;
		this.topic = topic;
		this.data = data;
		this.description = description;
	}

//	public BaseEvent(String type, String topic, Map<String, String> data, Map<String, String> headers,
//			String description, String guid) {
//		super();
//		this.type = type;
//		this.topic = topic;
//		this.data = data;
////		this.headers = headers;
//		this.description = description;
////		this.guid = guid;
////		this.timestamp = LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
//	}

}
