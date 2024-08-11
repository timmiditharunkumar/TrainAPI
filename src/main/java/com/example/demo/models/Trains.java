package com.example.demo.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Trains")
public class Trains {

	@Id
	@NotBlank(message = "Train number is mandatory")
	@Column(name = "train_number")
	public String trainNumber;

	@NotBlank(message = "Train number is mandatory")
	@Column(name = "train_name")
	public String trainName;

	@NotBlank(message = "Source Station is mandatory")
	@Column(name = "source_station")
	public String sourceStation;

	@NotBlank(message = "Destination station is mandatory")
	@Column(name = "destination_station")
	public String destinationStation;

	@NotNull(message = "depature time is mandatory")
	@Column(name = "departure_time")
	public LocalDateTime departureTime;

	@NotNull(message = "arrival time is mandatory")
	@Column(name = "arrival_time")
	public LocalDateTime arrivalTime;

	@NotNull(message = "Total seats is mandatory")
	@Column(name = "total_seats")
	public int totalSeats;

	@Column(name = "available_seats")
	public int availableSeats;

	public String getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(String trainNumber) {
		this.trainNumber = trainNumber;
	}

	public String getTrainName() {
		return trainName;
	}

	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}

	public String getSourceStation() {
		return sourceStation;
	}

	public void setSourceStation(String sourceStation) {
		this.sourceStation = sourceStation;
	}

	public String getDestinationStation() {
		return destinationStation;
	}

	public void setDestinationStation(String destinationStation) {
		this.destinationStation = destinationStation;
	}

	public LocalDateTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(LocalDateTime departureTime) {
		this.departureTime = departureTime;
	}

	public LocalDateTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalDateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}
	
	
}
