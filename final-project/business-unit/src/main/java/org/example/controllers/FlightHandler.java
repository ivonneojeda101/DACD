package org.example.controllers;

import com.google.gson.Gson;
import org.example.controllers.schemes.Flight;
import org.example.controllers.schemes.Weather;
import org.example.model.DateFlightWeather;

import java.util.HashMap;
import java.util.Map;

public class FlightHandler extends AbstractDataHandler {
	@Override
	public void handleData(String jsonData, String keyDate, MemoryDataManagement dataManagement) {
		Gson gson = prepareGson();
		Flight flight =  gson.fromJson(jsonData, Flight.class);
		String dayKey = keyDate + "-" + flight.getDestination();
		synchronized (dataManagement.getLock()) {
			dataManagement.getDataGrid().computeIfAbsent(dayKey, k -> new DateFlightWeather(new Weather(), new HashMap<>()));
			DateFlightWeather dateFlightWeather = dataManagement.getDataGrid().get(dayKey);
			Map<String, Flight> flights = dateFlightWeather.getFlights();
			flights.put(flight.getKey(), flight);
			dataManagement.getDataGrid().put(dayKey, dateFlightWeather);
		}
	}
}
