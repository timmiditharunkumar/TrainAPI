package com.example.demo.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.controllers.AddResponse;
import com.example.demo.exceptions.MissingFieldException;
import com.example.demo.exceptions.TrainNotFoundException;
import com.example.demo.models.Trains;
import com.example.demo.repositories.TrainRepository;

@Component
@Service
public class TrainService {

	@Autowired
	TrainRepository trainRepository;

	private static final Logger logger = LoggerFactory.getLogger(TrainService.class);

	public List<Trains> getAllTrains() {
		try {
			return trainRepository.findAll();
		} catch (Exception e) {
			logger.error("An error occurred while fetching trains: {}", e.getMessage());
			throw new RuntimeException("An error occurred while fetching train details", e);
		}
	}

	public Trains getTrainByTrainNumber(String id) throws TrainNotFoundException {
		return trainRepository.findById(id)
				.orElseThrow(() -> new TrainNotFoundException("Train number " + id + " not found"));
	}

	public List<Trains> searchTrains(String trainNumber, String source, String destination, LocalDate dateOfJourney,
			LocalDateTime departureTime, LocalDateTime arrivalTime, Integer availableSeats)
			throws TrainNotFoundException {
		try {
			List<Trains> trains = trainRepository.findAll();
			
			LocalDate endDate = dateOfJourney.plusDays(90);

			Predicate<Trains> trainNumberPredicate = train -> trainNumber == null || trainNumber.isEmpty()
					|| train.getTrainNumber().equals(trainNumber);
			Predicate<Trains> sourcePredicate = train -> train.getSourceStation().equalsIgnoreCase(source);
			Predicate<Trains> destinationPredicate = train -> train.getDestinationStation()
					.equalsIgnoreCase(destination);
			Predicate<Trains> dateOfJourneyPredicate = train -> {
				LocalDate trainDate = train.getDepartureTime().toLocalDate();
				return !trainDate.isBefore(dateOfJourney) && !trainDate.isAfter(endDate);
			};
			Predicate<Trains> departureTimePredicate = train -> departureTime == null
					|| !train.getDepartureTime().isBefore(departureTime);
			Predicate<Trains> arrivalTimePredicate = train -> arrivalTime == null
					|| !train.getArrivalTime().isAfter(arrivalTime);
			Predicate<Trains> availableSeatsPredicate = train -> availableSeats == null
					|| train.getAvailableSeats() >= availableSeats;

			trains = trains.stream()
					.filter(trainNumberPredicate.and(sourcePredicate).and(destinationPredicate)
							.and(dateOfJourneyPredicate).and(departureTimePredicate).and(arrivalTimePredicate)
							.and(availableSeatsPredicate))
					.collect(Collectors.toList());

			return trains;

		} catch (Exception e) {
			logger.error("An error occurred while fetching trains: {}", e.getMessage());
			throw new RuntimeException("An error occurred while fetching trains.", e);
		}
	}

	public List<Trains> searchAvailableTrains(String source, String destination, LocalDateTime dateOfJourney)
			throws TrainNotFoundException {
		try {
			List<Trains> trains = trainRepository.findAll();

			if (source == null || source.isEmpty()) {
				throw new MissingFieldException("Source is a mandatory field.");
			}
			if (destination == null || destination.isEmpty()) {
				throw new MissingFieldException("Destination is a mandatory field.");
			}

			Predicate<Trains> sourcePredicate = train -> train.getSourceStation().equalsIgnoreCase(source);
			Predicate<Trains> destinationPredicate = train -> train.getDestinationStation()
					.equalsIgnoreCase(destination);
			Predicate<Trains> dateOfJourneyPredicate = train -> dateOfJourney == null
					|| !train.getDepartureTime().isBefore(dateOfJourney);

			trains = trains.stream().filter(sourcePredicate.and(destinationPredicate).and(dateOfJourneyPredicate))
					.collect(Collectors.toList());

			return trains;

		} catch (Exception e) {
			logger.error("An error occurred while fetching trains: {}", e.getMessage());
			throw new RuntimeException("An error occurred while fetching trains.", e);
		}
	}

	public List<Trains> addTrains(List<Trains> train) {
		return trainRepository.saveAll(train);
	}

	public Trains updateTrain(Trains train) {
		return trainRepository.save(train);
	}

	public AddResponse deleteTrain(String trainNumber) throws TrainNotFoundException {
		getTrainByTrainNumber(trainNumber);
		trainRepository.deleteById(trainNumber);

		AddResponse response = new AddResponse();
		response.setMessage("Deleted train details successfully");
		response.setTrainNumber(trainNumber);

		return response;
	}

	public AddResponse deleteAllTrains() throws TrainNotFoundException {
		List<Trains> trains = trainRepository.findAll();
		if (trains.isEmpty()) {
			throw new TrainNotFoundException("No trains found to delete");
		}
		trainRepository.deleteAll();
		AddResponse response = new AddResponse();
		response.setMessage("Deleted All train details successfully");

		return response;
	}
}
