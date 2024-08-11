package com.example.demo.controllers;

import java.util.List;

import com.example.demo.models.Trains;

public class TrainAddResponse {
	private String message;
	private int totalAdded;
	private List<Trains> addedTrains;

	public TrainAddResponse(String message, int totalAdded, List<Trains> addedTrain) {
		this.message = message;
		this.totalAdded = totalAdded;
		this.addedTrains = addedTrains;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTotalAdded() {
		return totalAdded;
	}

	public void setTotalAdded(int totalAdded) {
		this.totalAdded = totalAdded;
	}

	public List<Trains> getAddedTrains() {
		return addedTrains;
	}

	public void setAddedTrains(List<Trains> addedTrains) {
		this.addedTrains = addedTrains;
	}

}
