package org.java.controllers;

import com.google.gson.Gson;
import org.java.controllers.schemes.Weather;
import org.java.model.DateFlightWeather;

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
