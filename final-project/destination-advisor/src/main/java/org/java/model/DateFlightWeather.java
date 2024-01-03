package org.java.model;

import org.java.controllers.schemes.Flight;
import org.java.controllers.schemes.Weather;

import java.util.HashMap;
import java.util.Map;

public class DateFlightWeather {
	private Weather weather;
	private final Map<String, Flight> flights;

	public DateFlightWeather(Weather weather, Map<String, Flight> flights) {
		this.weather = weather;
		this.flights = new HashMap<>(flights);
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}

	public Weather getWeather() {
		return weather;
	}

	public Map<String, Flight> getFlights() {
		return flights;
	}
}
