package org.example.controllers;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.example.controllers.schemes.Flight;
import org.example.exceptions.BussinessUnitException;
import org.example.controllers.schemes.Weather;
import org.example.model.DateFlightWeather;
import org.java.exceptions.WeatherException;
import org.java.model.Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.util.*;


public class MemoryDataManagement implements DataManagement {

	private final Gson gson = prepareGson();
	private final String urlDatalake;
	private final String csvFilePath;
	private final List<String> destinations;
	private Map<String, DateFlightWeather> dataGrid;
	private final List<String> topics;

	public MemoryDataManagement(String dataLake, String csvFilePath, List<String> topics) throws BussinessUnitException {
		this.urlDatalake = dataLake;
		this.csvFilePath = csvFilePath;
		this.topics = topics;
		this.destinations = getDestinations();
	}

	@Override
	public void storeData(String jsonData) throws BussinessUnitException {
		try {
			JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
			String sourceStamp = jsonObject.get("ss").getAsString();
			String predictionTime = jsonObject.get("predictionTime").toString();
			predictionTime = predictionTime.replaceAll("^\"|\"$", "");
			Instant instant = Instant.parse(predictionTime);
			String yyyymmdd = instant.atZone(ZoneOffset.UTC).toLocalDate().format(DateTimeFormatter.BASIC_ISO_DATE);

			if (Objects.equals(sourceStamp, "prediction-provider")) {
				Weather weather =  gson.fromJson(jsonData, Weather.class);
				String dayKey = yyyymmdd + "-" + weather.getLocation().getName();
				dataGrid.computeIfAbsent(dayKey, k -> new DateFlightWeather(new Weather(), new HashMap<>()));
				DateFlightWeather dateFlightWeather = dataGrid.get(dayKey);
				dateFlightWeather.setWeather(weather);
				dataGrid.put(dayKey, dateFlightWeather);
			}

			if (Objects.equals(sourceStamp, "flight-provider")) {
				Flight flight =  gson.fromJson(jsonData, Flight.class);
				String destination = jsonObject.get("destination").getAsString();
				String dayKey = yyyymmdd + "-" + destination;
				dataGrid.computeIfAbsent(dayKey, k -> new DateFlightWeather(new Weather(), new HashMap<>()));
				DateFlightWeather dateFlightWeather = dataGrid.get(dayKey);
				Map<String, Flight> flights = dateFlightWeather.getFlights();
				flights.put(flight.getKey(), flight);
				dataGrid.put(dayKey, dateFlightWeather);
			}
		}
		catch (Exception e) {
			throw new BussinessUnitException(e.getMessage());
		}
	}

	public List<DateFlightWeather> getPrediction(double[] weight) throws BussinessUnitException {
		List<String> dates = generateDateTimes();
		Double[] scores = calculateScores(dates, weight);
		int indexMaxScore = 0;

		for (int i = 1; i < scores.length; i++) {
			if (scores[i] > scores[indexMaxScore]) {
				indexMaxScore = i;
			}
		}
		return extractResult(destinations.get(indexMaxScore), dates);
	}

	private Double[] calculateScores(List<String> dates, double[] weight){
		Double[] scores = new Double[destinations.size()];
		Arrays.fill(scores, 0.0);
		for (int i = 0; i < destinations.size(); i++) {
			for (String date : dates) {
				String key = date + "-" + destinations.get(i);
				if (dataGrid.containsKey(key)) {
					Weather weather = dataGrid.get(key).getWeather();
					Double score = (weather.getTemperature() * weight[0]) + (weather.getHumidity() * weight[1]) + (weather.getClouds() * weight[2]) + (weather.getWindSpeed() * weight[3]) + (weather.getPrecipitationProbability() * weight[4]);
					double finalScore = scores[i] + score;
					scores[i] = finalScore;
				}
			}
		}
		return scores;
	}

	private List<DateFlightWeather> extractResult(String destination, List<String> dates){
		List<DateFlightWeather> prediction = new ArrayList<>();
		for (String date : dates) {
			String key = date + "-" + destination;
			if (dataGrid.containsKey(key)) {
				prediction.add(dataGrid.get(key));
			}
		}
		return prediction;
	}

	private static Gson prepareGson() {
		return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Instant.class, new TypeAdapter<Instant>() {
			@Override
			public void write(JsonWriter out, Instant value) throws IOException {
				out.value(value.toString());
			}
			@Override
			public Instant read(JsonReader in) throws IOException {
				return Instant.parse(in.nextString());
			}
		}).create();
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

	private List<String> generateDateTimes() {
		List<String> timestamps = new ArrayList<>();
		LocalDate currentDate = LocalDate.now();
		for (int i = 0; i < 5; i++) {
			LocalDate nextDate = currentDate.plusDays(i+1);
			Instant timestamp = nextDate.atStartOfDay().plusHours(12).toInstant(ZoneOffset.UTC);
			String yyyymmdd = timestamp.atZone(ZoneOffset.UTC).toLocalDate().format(DateTimeFormatter.BASIC_ISO_DATE);
			timestamps.add(yyyymmdd);
		}
		return timestamps;
	}
}
