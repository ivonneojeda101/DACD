package org.example.controllers;

import com.google.gson.Gson;
import org.example.controllers.schemes.Weather;
import org.example.model.DateFlightWeather;

import java.util.HashMap;

public class WeatherHandler extends AbstractDataHandler{

	@Override
	public void handleData(String jsonData, String keyDate, MemoryDataManagement dataManagement) {
		Gson gson = getGson();
		Weather weather =  gson.fromJson(jsonData, Weather.class);
		String dayKey = keyDate + "-" + weather.getLocation().getName();
		synchronized (dataManagement.getLock()) {
			dataManagement.getDataGrid().computeIfAbsent(dayKey, k -> new DateFlightWeather(new Weather(), new HashMap<>()));
			DateFlightWeather dateFlightWeather = dataManagement.getDataGrid().get(dayKey);
			dateFlightWeather.setWeather(weather);
			dataManagement.getDataGrid().put(dayKey, dateFlightWeather);
		}
	}
}
