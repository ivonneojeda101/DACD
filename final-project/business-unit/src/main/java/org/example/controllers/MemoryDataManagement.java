package org.example.controllers;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.example.controllers.schemes.Flight;
import org.example.exceptions.BussinessUnitException;
import org.example.controllers.schemes.Weather;
import org.example.model.DateFlightWeather;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.util.*;


public class MemoryDataManagement implements DataManagement {

	private final Gson gson = prepareGson();

	private Map<String, DateFlightWeather> dataGrid;
	private final Object lock = new Object();

	public MemoryDataManagement() {
		dataGrid = new HashMap<>();
	}

	public Map<String, DateFlightWeather> getDataGrid() {
		return dataGrid;
	}

	@Override
	public void storeData(String jsonData) throws BussinessUnitException {
		try {
			JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
			String sourceStamp = jsonObject.get("ss").getAsString();
			String predictionTime = jsonObject.get("predictionTime").toString();
			Instant instant = Instant.parse(predictionTime.replaceAll("^\"|\"$", ""));
			String keyDate = instant.atZone(ZoneOffset.UTC).toLocalDate().format(DateTimeFormatter.BASIC_ISO_DATE);

			if (Objects.equals(sourceStamp, "prediction-provider")) {
				weatherHandler(jsonData, keyDate);
			}

			if (Objects.equals(sourceStamp, "flight-provider")) {
				flightHandler(jsonData, keyDate, jsonObject);
			}
		}
		catch (Exception e) {
			throw new BussinessUnitException(e.getMessage());
		}
	}

	private void weatherHandler(String jsonData, String keyDate) {
		Weather weather =  gson.fromJson(jsonData, Weather.class);
		String dayKey = keyDate + "-" + weather.getLocation().getName();
		synchronized (lock) {
			dataGrid.computeIfAbsent(dayKey, k -> new DateFlightWeather(new Weather(), new HashMap<>()));
			DateFlightWeather dateFlightWeather = dataGrid.get(dayKey);
			dateFlightWeather.setWeather(weather);
			dataGrid.put(dayKey, dateFlightWeather);
		}
	}

	private void flightHandler(String jsonData, String keyDate, JsonObject jsonObject) {
		Flight flight =  gson.fromJson(jsonData, Flight.class);
		String destination = jsonObject.get("destination").getAsString();
		String dayKey = keyDate + "-" + destination;
		synchronized (lock) {
			dataGrid.computeIfAbsent(dayKey, k -> new DateFlightWeather(new Weather(), new HashMap<>()));
			DateFlightWeather dateFlightWeather = dataGrid.get(dayKey);
			Map<String, Flight> flights = dateFlightWeather.getFlights();
			flights.put(flight.getKey(), flight);
			dataGrid.put(dayKey, dateFlightWeather);
		}
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
}
