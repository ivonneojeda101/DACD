package org.example;

import org.example.model.Location;
import org.example.model.Weather;

import java.util.stream.Stream;

public interface WeatherStore extends AutoCloseable {

	default void insertWeatherInfo(Weather weatherData){}
	default Stream<Location> retrieveLocationData(){
		return Stream.empty();
	}
	default void close() {}
}
