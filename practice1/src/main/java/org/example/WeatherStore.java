package org.example;

import org.example.model.Location;
import org.example.model.Weather;

import java.util.stream.Stream;

public interface WeatherStore extends AutoCloseable {

	public static void insertWeatherInfo(Weather weatherData){

	}
	public static Stream<Location> retrieveLocationData(){
		return Stream.empty();
	}
	default void close() {

	}
}
