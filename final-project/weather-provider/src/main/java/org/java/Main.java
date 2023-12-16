package org.java;

import org.java.controllers.*;
import org.java.exceptions.WeatherException;

public class Main {
	public static void main(String[] args) throws WeatherException {
		WeatherProvider weatherProvider = new OpenWeatherMapProvider(args[0], args[1]);
		WeatherStore weatherStore = new JMSWeatherStore(args[3], args[4]);
		WeatherController weathercontroller = new WeatherController(weatherStore, weatherProvider, args[2], args[5]);
		try{
			weathercontroller.fetchWeather();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}