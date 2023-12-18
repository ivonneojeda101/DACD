package org.example.controllers;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.example.exceptions.BussinessUnitException;
import org.example.model.Weather;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Objects;


public class SQLiteDataStore implements DataStore{

	private final String dbPath;
	private static Statement statement;
	private Connection connection;
	private final Gson gson = prepareGson();

	public SQLiteDataStore(String dbPath) throws BussinessUnitException {
		try {
			this.dbPath = dbPath;
			connect();
			statement = connection.createStatement();
			createDataGrid();
		}
		catch (Exception e) {
			throw new BussinessUnitException(e.getMessage());
		}
	}

	@Override
	public void storeData(String jsonData) throws BussinessUnitException {
		try {

			JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
			String sourceStamp = jsonObject.get("sourceStamp").getAsString();


			if (Objects.equals(sourceStamp, "prediction-provider")) {
				Weather weather =  gson.fromJson(jsonData, Weather.class);
				setWeather(weather);
			}

			if (Objects.equals(sourceStamp, "flight-provider")) {
				String predictionTimestamp = jsonObject.get("predictionTimestamp").getAsString();
				String islandName = jsonObject.get("islandName").getAsString();
				setFlight(islandName, formatDate(predictionTimestamp), jsonData);
			}
		}
		catch (Exception e) {
			throw new BussinessUnitException(e.getMessage());
		}
	}

	private void setWeather(Weather weather) throws BussinessUnitException {
		try {
			String lastUpdate = formatDate(Instant.now().toString());
			ZonedDateTime zonedDateTime = weather.getPredictionTimestamp().atZone(ZoneOffset.UTC);
			ZonedDateTime dateOnly = zonedDateTime.toLocalDate().atStartOfDay(ZoneOffset.UTC);
			String forecastDate = formatDate(dateOnly.toString());
			String insertSQL = "INSERT OR IGNORE INTO WeatherDataGrid (islandName, forecastDate, temperature, humidity, clouds, windSpeed, precipitationProbability, lastUpdate) " +
					"VALUES ('" +
					weather.getLocation().getName() + "', '" +
					forecastDate + "', " +
					weather.getTemperature() + ", " +
					weather.getHumidity() + ", " +
					weather.getClouds() + ", " +
					weather.getWindSpeed() + ", " +
					weather.getPrecipitationProbability() + ", '" +
					lastUpdate + "');";
			boolean inserted = statement.execute(insertSQL);
			if (!inserted) {
				String updateSQL = "UPDATE WeatherDataGrid SET " +
						"temperature = " + weather.getTemperature() + ", " +
						"humidity = " + weather.getHumidity() + ", " +
						"clouds = " + weather.getClouds() + ", " +
						"windSpeed = " + weather.getWindSpeed() + ", " +
						"precipitationProbability = " + weather.getPrecipitationProbability() + ", " +
						"lastUpdate = '" + lastUpdate + "' " +
						"WHERE islandName = '" + weather.getLocation().getName() + "' " +
						"AND forecastDate = '" + forecastDate + "';";
				statement.execute(updateSQL);
			}
		}
		catch (Exception e){
			throw new BussinessUnitException("Problem inserting the weather data of : " + weather.getLocation().getName());
		}
	}

	private void setFlight(String islandName, String forecastDate, String flightOffers) throws BussinessUnitException {
		try {
			String insertSQL = "INSERT OR IGNORE INTO WeatherDataGrid (islandName, forecastDate, flightOffers) " +
					"VALUES ('" +
					islandName + "', '" +
					forecastDate + "', '" +
					flightOffers + "');";
			boolean inserted = statement.execute(insertSQL);
			if (!inserted) {
				String updateSQL = "UPDATE WeatherDataGrid SET flightOffers = '" +
						flightOffers + "' WHERE islandName = '" +
						islandName + "' AND forecastDate = '" +
						forecastDate + "';";
				statement.execute(updateSQL);
			}
		}
		catch (Exception e){
			throw new BussinessUnitException("Problem inserting the flights data of : " + islandName);
		}
	}

	private static void createDataGrid() throws SQLException {
		statement.execute("CREATE TABLE IF NOT EXISTS WeatherDataGrid ("
				+ "islandName TEXT,"
				+ "forecastDate DATETIME,"
				+ "temperature REAL,"
				+ "humidity INTEGER,"
				+ "clouds INTEGER,"
				+ "windSpeed REAL,"
				+ "precipitationProbability INTEGER,"
				+ "flightOffers TEXT,"
				+ "lastUpdate DATETIME,"
				+ "PRIMARY KEY (islandName, forecastDate)"
				+ ")");
	}

	private void  connect() throws SQLException {
		String url = "jdbc:sqlite:" + dbPath;
		connection = DriverManager.getConnection(url);
		System.out.println("Connection to SQLite has been established.");
	}
	@Override
	public void close() throws SQLException {
		connection.close();
	}

	private static Gson prepareGson() {
		return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Instant.class, new TypeAdapter<Instant>() {
			@Override
			public void write(JsonWriter out, Instant value) throws IOException {
				out.value(value.toString());
			}

			@Override
			public Instant read(JsonReader in) throws IOException {
				return Instant.parse(in.nextString());
			}
		}).create();
	}

	private String formatDate(String date){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter originalFormatter = DateTimeFormatter.ISO_DATE_TIME;
		LocalDateTime dateTime = LocalDateTime.parse(date, originalFormatter);
		return dateTime.format(formatter);
	}
}
