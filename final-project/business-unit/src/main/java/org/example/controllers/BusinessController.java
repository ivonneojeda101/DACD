package org.example.controllers;

import org.example.controllers.schemes.Weather;
import org.example.exceptions.BussinessUnitException;
import org.example.model.DateFlightWeather;
import org.java.exceptions.WeatherException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BusinessController {
	private final DataSource dataSource;
	private final DataManagement dataManagement;
	private final String csvFilePath;
	private final List<String> destinations;
	public BusinessController(DataSource dataSource, DataManagement dataManagement,String csvFilePath) throws BussinessUnitException {
		this.csvFilePath = csvFilePath;
		this.destinations = getDestinations();
		this.dataSource = dataSource;
		this.dataManagement = dataManagement;
	}

	public List<DateFlightWeather> getRecommendation(double[] weights) {
		List<String> dates = getNextWeekDates();
		Map<String, DateFlightWeather> dataGrid = dataManagement.getDataGrid();
		Double[] scores = calculateScores(dataGrid, dates, weights);
		int indexMaxScore = 0;

		for (int i = 1; i < scores.length; i++) {
			if (scores[i] > scores[indexMaxScore]) {
				indexMaxScore = i;
			}
		}
		if (scores[indexMaxScore] == 0){
			return new ArrayList<>();
		} else {
			return getWeatherAndFlights(dataGrid, destinations.get(indexMaxScore), dates);
		}
	}

	public void startSubscriber() throws BussinessUnitException {
		dataSource.getData(dataManagement);
		cleanData();
	}

	private void cleanData() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		Runnable task = () -> dataManagement.deleteData(BusinessController.this.getNextWeekDates());
		executor.scheduleAtFixedRate(task, 0, 24, TimeUnit.HOURS);
	}
	private Double[] calculateScores(Map<String, DateFlightWeather> dataGrid, List<String> dates, double[] weights){
		Double[] scores = new Double[destinations.size()];
		Arrays.fill(scores, 0.0);
		for (int i = 0; i < destinations.size(); i++) {
			for (String date : dates) {
				String key = date + "-" + destinations.get(i);
				if (dataGrid.containsKey(key)) {
					Weather weather = dataGrid.get(key).getWeather();
					Double score = (weather.getTemperature() * weights[0]) + (weather.getHumidity() * weights[1]) + (weather.getClouds() * weights[2]) + (weather.getWindSpeed() * weights[3]) + (weather.getPrecipitationProbability() * weights[4]);
					double finalScore = scores[i] + score;
					scores[i] = finalScore;
				}
			}
		}
		return scores;
	}

	private List<DateFlightWeather> getWeatherAndFlights(Map<String, DateFlightWeather> dataGrid, String destination, List<String> dates){
		List<DateFlightWeather> information = new ArrayList<>();
		for (String date : dates) {
			String key = date + "-" + destination;
			if (dataGrid.containsKey(key)) {
				information.add(dataGrid.get(key));
			}
		}
		return information;
	}

	private List<String> getDestinations() throws BussinessUnitException {
		List<String> destinations = new ArrayList<>();
		String line;
		String csvDelimiter = ";";
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
			while ((line = br.readLine()) != null) {
				String[] data = line.split(csvDelimiter);
				if (data.length == 4) {
					destinations.add(data[0]);
				} else {
					throw new WeatherException("Skipping invalid data: " + line);
				}
			}
		} catch (Exception e) {
			throw new BussinessUnitException(e.getMessage());
		}
		return destinations;
	}

	private List<String> getNextWeekDates() {
		List<String> timestamps = new ArrayList<>();
		LocalDate currentDate = LocalDate.now();
		for (int i = 0; i < 5; i++) {
			LocalDate nextDate = currentDate.plusDays(i+1);
			Instant timestamp = nextDate.atStartOfDay().plusHours(12).toInstant(ZoneOffset.UTC);
			String finalDate = timestamp.atZone(ZoneOffset.UTC).toLocalDate().format(DateTimeFormatter.BASIC_ISO_DATE);
			timestamps.add(finalDate);
		}
		return timestamps;
	}
}
