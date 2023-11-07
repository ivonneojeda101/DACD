package org.example;

import org.example.interfaces.WeatherStore;
import org.example.model.Location;
import org.example.model.Weather;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

public class SQLiteWeatherStore implements WeatherStore {

	private final String dbPath;
	private static Statement statement;
	private Connection connection;
	public SQLiteWeatherStore(String dbPath) {this.dbPath = dbPath;}
	@Override
	public void setUpStore(List<Location> locations) {
		try {
			connect();
			statement = connection.createStatement();
			for (Location location : locations) {
				String tableName = location.getName().replace(" ", "");
				dropTable(tableName);
				createWeatherTable(tableName);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error creating database tables");
		}
	}

	public void setWeather(Weather weather){
		try {
			String tableName = weather.getLocation().getName().replace(" ", "");
			String lastUpdate = Instant.now().toString();
			String insertSQL = "INSERT INTO " + tableName + " (temperature, humidity, clouds, windSpeed, precipitationProbability, timestamp, lastUpdate) " +
					"VALUES (" +
					weather.getTemperature() + ", " +
					weather.getHumidity() + ", " +
					weather.getClouds() + ", " +
					weather.getWindSpeed() + ", " +
					weather.getPrecipitationProbability() + ", " +
					weather.getTimestamp() + ", " +
					lastUpdate + ");";
			statement.execute(insertSQL);
		}
		catch (Exception e){
			System.out.println("Problem inserting the weather data of : " + weather.getLocation().getName());
		}
	}

	private static void createWeatherTable(String tableName) throws SQLException {
		statement.execute("CREATE TABLE IF NOT EXISTS " + tableName + " ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "temperature REAL,"
				+ "humidity INTEGER,"
				+ "clouds INTEGER,"
				+ "windSpeed REAL,"
				+ "precipitationProbability INTEGER,"
				+ "timestamp DATETIME,"
				+ "lastUpdate DATETIME"
				+ ")");
	}

	private static void dropTable(String tableName) throws SQLException {
		statement.execute("DROP TABLE IF EXISTS " + tableName + ";\n");
	}

	private void  connect() throws SQLException {
			String url = "jdbc:sqlite:" + dbPath;
			connection = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
	}
	@Override
	public void close() {
	}

}
