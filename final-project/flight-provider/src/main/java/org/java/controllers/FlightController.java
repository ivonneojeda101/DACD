package org.java.controllers;

import org.java.exceptions.CustomException;
import org.java.model.Flight;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

public class FlightController {
	private final FlightStore flightStore;
	private final FlightProvider flightProvider;
	private final String csvFilePath;
	private Set<Instant> dateTimes;
	private final String hourFrequency;
	private Map<String, String> destinations;
	public FlightController(FlightStore flightStore, FlightProvider flightProvider, String csvFilePath, String hourFrequency) throws CustomException {
		this.flightStore = flightStore;
		this.flightProvider = flightProvider;
		this.csvFilePath = csvFilePath;
		this.hourFrequency = hourFrequency;
		getDestinationAirports();
	}

	private void getDestinationAirports() throws CustomException {
		destinations = new HashMap<>();
		String line;
		String csvDelimiter = ";";
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
			while ((line = br.readLine()) != null) {
				String[] data = line.split(csvDelimiter);
				if (data.length == 4) {
					destinations.put(data[0], data[3]);
				} else {
					throw new CustomException("Skipping invalid data: " + line);
				}
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	public void fetchFlight(String departureAirport) throws CustomException {
		try {
			Timer timer = new Timer();
			BigDecimal frequency = new BigDecimal(hourFrequency);
			BigDecimal millisecondsFrequency = frequency.multiply(BigDecimal.valueOf(60 * 60 * 1000));
			long milliseconds = millisecondsFrequency.longValue();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					dateTimes = generateDateTimes();
					for (String key : destinations.keySet()) {
						try {
							String arrivalAirport = destinations.get(key);
							List<Flight> flightTrackers = flightProvider.getFlight(departureAirport, arrivalAirport, key , dateTimes);
							for (Flight flightOffer : flightTrackers) flightStore.setFlight(flightOffer);
						} catch (CustomException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}, 0, milliseconds);
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}
	private Set<Instant> generateDateTimes() {
		Set<Instant> timestamps = new HashSet<>();
		LocalDate currentDate = LocalDate.now();
		for (int i = 0; i < 5; i++) {
			LocalDate nextDate = currentDate.plusDays(i+1);
			Instant timestamp = nextDate.atStartOfDay().plusHours(12).toInstant(ZoneOffset.UTC);
			timestamps.add(timestamp);
		}
		return timestamps;
	}
}
