package org.example;

import org.example.controllers.*;

public class Main {
	public static void main(String[] args) {
		WeatherProvider weatherProvider = new OpenWeatherMapProvider(args[0], args[1]);
		WeatherStore weatherStore = new SQLiteWeatherStore(args[3]);
		WeatherController weathercontroller = new WeatherController(weatherStore, weatherProvider, args[2], args[4]);
		try{
			weathercontroller.fetchWeather();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}