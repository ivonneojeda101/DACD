package org.example.controllers;

import org.example.exceptions.CustomException;
import org.example.model.IslandFlightTracker;
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
	private final List<String> islandAirports;
	private final String hourFrequency;
	private List<String> islandName;
	public FlightController(FlightStore flightStore, FlightProvider flightProvider, String csvFilePath, String hourFrequency) throws CustomException {
		this.flightStore = flightStore;
		this.flightProvider = flightProvider;
		this.csvFilePath = csvFilePath;
		this.hourFrequency = hourFrequency;
		islandAirports = getIslandAirports();
	}
	public void fetchFlight() throws CustomException {
		try {
			Timer timer = new Timer();
			BigDecimal frequency = new BigDecimal(hourFrequency);
			BigDecimal millisecondsFrequency = frequency.multiply(BigDecimal.valueOf(60 * 60 * 1000));
			long milliseconds = millisecondsFrequency.longValue();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					dateTimes = generateDateTimes();
					for (int i = 0; i < islandAirports.size(); i++) {
						try {
							List<IslandFlightTracker> flightTrackers = flightProvider.getFlight(islandAirports.get(0), islandAirports.get(i), islandName.get(i), dateTimes);
							for (IslandFlightTracker flightOffer : flightTrackers) {
								flightStore.setFlight(flightOffer);
							}
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
	private List<String> getIslandAirports() throws CustomException {
		List<String> airportList = new ArrayList<>();
		islandName =  new ArrayList<>();
		String line;
		String csvDelimiter = ";";
		System.out.println(System.getProperty("user.dir"));
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
			while ((line = br.readLine()) != null) {
				String[] data = line.split(csvDelimiter);
				if (data.length == 4) {
					islandName.add(data[0]);
					airportList.add(data[3]);
				} else {
					throw new CustomException("Skipping invalid data: " + line);
				}
			}
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		return airportList;
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
