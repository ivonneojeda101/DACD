package org.example.controllers;

import org.example.model.Location;
import org.example.model.Weather;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

public class WeatherController {
	private final WeatherStore weatherStore;
	private final WeatherProvider weatherProvider;
	private final String csvFilePath;
	private Set<Instant> dateTimes;
	private final List<Location> locations;
	private final String hourFrequency;
	public WeatherController(WeatherStore weatherStore, WeatherProvider weatherProvider, String csvFilePath, String hourFrequency) {
		this.weatherStore = weatherStore;
		this.weatherProvider = weatherProvider;
		this.csvFilePath = csvFilePath;
		this.hourFrequency = hourFrequency;
		locations = getLocations();
		weatherStore.setUpStore(locations);
	}
	public void fetchWeather(){
		try {
			Timer timer = new Timer();
			BigDecimal frequency = new BigDecimal(hourFrequency);
			BigDecimal millisecondsFrequency = frequency.multiply(BigDecimal.valueOf(60 * 60 * 1000));
			long milliseconds = millisecondsFrequency.longValue();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					dateTimes = generateDateTimes();
					for (Location location : locations) {
						List<Weather> weatherList = weatherProvider.getWeather(location, dateTimes);
						for (Weather weather : weatherList) {
							weatherStore.setWeather(weather);
						}
					}
				}
			}, 0, milliseconds);
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
					throw new RuntimeException("Skipping invalid data: " + line);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
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
