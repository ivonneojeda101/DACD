package org.example;

import org.example.model.Location;
import org.example.model.Weather;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

public class Main {
	static SQLiteWeatherStore weatherStore = new SQLiteWeatherStore();
	static OpenWeatherMapProvider weatherProvider = new OpenWeatherMapProvider();

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Please enter your API key: ");
		String apiKey = scanner.nextLine();
		try(Connection connection = weatherStore.connect()) {
			Statement statement = connection.createStatement();
			if (weatherStore.prepareDB(statement)){
				fetchWeather(statement,apiKey);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void fetchWeather(Statement statement, String apiKey){
		try {
			Set<String> timestamps = generateEpochTimestamps();
			List<Location> locationInformation = weatherStore.retrieveLocationData(statement);
			for (Location location : locationInformation) {
				Stream<Weather> weatherLocation = weatherProvider.getWeatherData(location.getLongitude(), location.getLatitude(), apiKey, timestamps).stream();
				for (Weather weatherInformation : weatherLocation.toList()) {
					weatherInformation.setLocation(location);
					weatherStore.insertWeatherInfo(weatherInformation, statement);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Set<String> generateEpochTimestamps() {
		Set<String> timestamps = new HashSet<>();
		LocalDate currentDate = LocalDate.now();
		for (int i = 0; i < 5; i++) {
			LocalDate nextDate = currentDate.plusDays(i+1);
			long timestamp = nextDate.atStartOfDay().toInstant(ZoneOffset.UTC).getEpochSecond();
			timestamps.add(Long.toString(timestamp));
		}
		return timestamps;
	}
}