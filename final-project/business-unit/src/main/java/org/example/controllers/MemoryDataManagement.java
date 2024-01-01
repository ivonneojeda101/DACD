package org.example.controllers;

import com.google.gson.*;
import org.example.exceptions.BussinessUnitException;
import org.example.model.DateFlightWeather;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.controllers.AbstractDataHandler.prepareGson;


public class MemoryDataManagement implements DataManagement {

	private final Gson gson = prepareGson();
	private final Map<String, DateFlightWeather> dataGrid;
	Map<String, DataHandler> handlers;
	private final Object lock = new Object();

	public MemoryDataManagement() {
		dataGrid = new HashMap<>();
		handlers = new HashMap<>();
		handlers.put("prediction-provider", new WeatherHandler());
		handlers.put("flight-provider", new FlightHandler());
	}

	@Override
	public void storeData(String jsonData) throws BussinessUnitException {
		try {
			JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
			String sourceStamp = jsonObject.get("ss").getAsString();
			String predictionTime = jsonObject.get("predictionTime").toString();
			Instant instant = Instant.parse(predictionTime.replaceAll("^\"|\"$", ""));
			String keyDate = instant.atZone(ZoneOffset.UTC).toLocalDate().format(DateTimeFormatter.BASIC_ISO_DATE);

			DataHandler handler = handlers.get(sourceStamp);
			if (handler != null) {
				handler.handleData(jsonData, keyDate, this);
			}
		}
		catch (Exception e) {
			throw new BussinessUnitException(e.getMessage());
		}
	}

	@Override
	public void deleteData(List<String> keyDates) {
		Map<String, Integer> mapDates = IntStream.range(0, keyDates.size())
				.boxed()
				.collect(Collectors.toMap(keyDates::get, i -> i));
		Iterator<Map.Entry<String, DateFlightWeather>> iterator = dataGrid.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, DateFlightWeather> entry = iterator.next();
			String key = entry.getKey();
			if (!mapDates.containsKey(key.split("-")[0])) {
				iterator.remove();
			}
		}
	}

	public Map<String, DateFlightWeather> getDataGrid() {
		return dataGrid;
	}
	public Object getLock() {
		return lock;
	}
}
