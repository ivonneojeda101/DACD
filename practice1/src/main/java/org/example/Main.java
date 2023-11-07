package org.example;

import org.example.controllers.WeatherController;
import org.example.interfaces.WeatherProvider;
import org.example.interfaces.WeatherStore;

public class Main {
	// arg [] = [ apiKey apiURL csvFilePath dbPath]

	//apiURL = "http://api.openweathermap.org/data/2.5/forecast";
	//dbPath = "./src/database/weatherData.db"
	//csvFilePath = "./src/files/Location.csv";
	public static void main(String[] args) {
		WeatherProvider weatherProvider = new OpenWeatherMapProvider(args[0], args[1]);
		WeatherStore weatherStore = new SQLiteWeatherStore(args[3]);
		WeatherController weathercontroller = new WeatherController(weatherStore, weatherProvider, args[2]);
		try{
			weathercontroller.fetchWeather();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}




}