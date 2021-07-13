package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventModel {
	private String title;
	private String price;
	private String quantity;

	public EventModel() {

	}

	public EventModel(String title, String price, String quantity) {
		super();
		this.title = title;
		this.price = price;
		this.quantity = quantity;
	}
}
