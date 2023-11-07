package org.example;

import org.example.model.Weather;

public interface WeatherProvider {
	public default Weather getWeatherData(Double longitud, Double latitue){
		return null;
	}
}
