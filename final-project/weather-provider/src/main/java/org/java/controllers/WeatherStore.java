package org.java.controllers;

import org.java.exceptions.WeatherException;
import org.java.model.Weather;

public interface WeatherStore extends AutoCloseable {
	void setWeather(Weather weather) throws WeatherException;
}
