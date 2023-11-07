package org.example.controllers;

import org.example.interfaces.WeatherProvider;
import org.example.interfaces.WeatherStore;
import org.example.model.Location;
import org.example.model.Weather;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WeatherController {
	private final WeatherStore weatherStore;
	private final WeatherProvider weatherProvider;
	private final String csvFilePath;

	public WeatherController(WeatherStore weatherStore, WeatherProvider weatherProvider, String csvFilePath) {
		this.weatherStore = weatherStore;
		this.weatherProvider = weatherProvider;
		this.csvFilePath = csvFilePath;
	}
	public void fetchWeather(){
		try {
			Set<Instant> dateTimes = generateDateTimes();
			List<Location> locations = getLocations();
			weatherStore.setUpStore(locations);
			for (Location location : locations) {
				List<Weather> weatherList = weatherProvider.getWeather(location, dateTimes);
				for (Weather weather : weatherList) {
					weatherStore.setWeather(weather);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	private List<Location> getLocations() {
		List<Location> locationList = new ArrayList<>();
		String line;
		String csvDelimiter = ";";
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
			while ((line = br.readLine()) != null) {
				String[] data = line.split(csvDelimiter);
				if (data.length == 3) {
					String name = data[0];
					double latitude = Double.parseDouble(data[1]);
					double longitude = Double.parseDouble(data[2]);
					locationList.add(new Location(name, latitude, longitude));
				} else {
					System.out.println("Skipping invalid data: " + line);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return locationList;
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
