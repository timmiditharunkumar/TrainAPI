package com.example.demo.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exceptions.MissingFieldException;
import com.example.demo.exceptions.TrainNotFoundException;
import com.example.demo.models.Trains;
import com.example.demo.services.TrainService;

import jakarta.validation.Valid;

@RestController
public class TrainBookingController {

	@Autowired
	TrainService trainService;

	@GetMapping("/alltrains")
	public ResponseEntity<?> getAllTrains() {
		try {
			List<Trains> trains = trainService.getAllTrains();
			if (trains.isEmpty()) {
				ErrorResponse errorRes = new ErrorResponse(HttpStatus.NOT_FOUND.name(), "No trains available");
				return new ResponseEntity<>(errorRes, HttpStatus.NOT_FOUND);
			}
			return ResponseEntity.ok(trains);
		} catch (Exception ex) {
			throw new RuntimeException("An error occurred while fetching train numbers", ex);
		}
	}

	@GetMapping("/trains/{trainNo}")
	public ResponseEntity<?> getTrainByTrainNumber(@Valid @PathVariable(value = "trainNo") String trainNumber) {
		try {
			Trains trains = trainService.getTrainByTrainNumber(trainNumber);
			return new ResponseEntity<>(trains, HttpStatus.OK);
		} catch (TrainNotFoundException ex) {
			ErrorResponse errorRes = new ErrorResponse(HttpStatus.NOT_FOUND.name(), ex.getMessage());
			return new ResponseEntity<>(errorRes, HttpStatus.NOT_FOUND);
		}
	}

	@SuppressWarnings("unused")
	@GetMapping("/trains/search")
	public ResponseEntity<?> searchTrains(@RequestParam(required = false) String trainNumber,
			@RequestParam(required = false) String source, @RequestParam(required = false) String destination,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfJourney,
			@RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm:ss") LocalDateTime departureTime,
			@RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm:ss") LocalDateTime arrivalTime,
			@RequestParam(required = false) Integer availableSeats) throws TrainNotFoundException {

		try {
			if (dateOfJourney.isBefore(LocalDate.now())) {
				throw new IllegalArgumentException("Date of journey cannot be in the past.");
			}
			if (source == null || source.isEmpty()) {
				throw new MissingFieldException("Source is a mandatory field.");
			}
			if (destination == null || destination.isEmpty()) {
				throw new MissingFieldException("Destination is a mandatory field.");
			}
			if (dateOfJourney == null) {
				throw new MissingFieldException("dateOfJourney is a mandatory field.");
			}

			List<Trains> trains = trainService.searchTrains(trainNumber, source, destination, dateOfJourney,
					departureTime, arrivalTime, availableSeats);
			if (trains.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorResponse("No trains available for the given search criteria."));
			}
			return ResponseEntity.ok(trains);
		} catch (MissingFieldException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), ex.getMessage()));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), ex.getMessage()));
		} catch (TrainNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND.name(), ex.getMessage()));
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), "An unexpected error occurred."));
		}
	}

	@PostMapping("/trains")
	public ResponseEntity<?> addTrains(@Valid @RequestBody List<Trains> trains) {
//		try {
			List<Trains> createdTrains = trainService.addTrains(trains);
			TrainAddResponse response = new TrainAddResponse("Trains added successfully", createdTrains.size(),
					createdTrains);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
//		} catch (Exception ex) {
//
//		}
	}

	@PutMapping("/trains/{trainNo}")
	public ResponseEntity<?> updateTrains(@Valid @PathVariable(value = "trainNo") String trainNumber,
			@RequestBody Trains train) {
		try {
			Trains trainDetails = trainService.getTrainByTrainNumber(trainNumber);
			trainDetails.setSourceStation(train.getSourceStation());
			trainDetails.setDestinationStation(train.getDestinationStation());
			trainDetails.setArrivalTime(train.getArrivalTime());
			trainDetails.setDepartureTime(train.getDepartureTime());
			trainDetails.setAvailableSeats(train.getAvailableSeats());
			trainService.updateTrain(trainDetails);

			return new ResponseEntity<Trains>(HttpStatus.NO_CONTENT);
		} catch (TrainNotFoundException ex) {
			ErrorResponse errorRes = new ErrorResponse(HttpStatus.NOT_FOUND.name(), ex.getMessage());
			return new ResponseEntity<>(errorRes, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/trains/{trainNo}")
	public ResponseEntity<?> deleteTrains(@Valid @PathVariable(value = "trainNo") String trainNumber) {
		try {
			AddResponse response = trainService.deleteTrain(trainNumber);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (TrainNotFoundException ex) {
			ErrorResponse errorRes = new ErrorResponse(HttpStatus.NOT_FOUND.name(), ex.getMessage());
			return new ResponseEntity<>(errorRes, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/trains")
	public ResponseEntity<?> deleteAllTrains() throws TrainNotFoundException {
		try {
			AddResponse response = trainService.deleteAllTrains();
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (TrainNotFoundException ex) {
			ErrorResponse errorRes = new ErrorResponse(HttpStatus.NOT_FOUND.name(), ex.getMessage());
			return new ResponseEntity<>(errorRes, HttpStatus.NOT_FOUND);
		}
	}
}
