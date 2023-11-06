package org.example;

import org.example.model.Location;
import org.example.model.Weather;

import java.io.*;
import java.sql.*;
import java.util.Objects;
import java.util.stream.Stream;

public class SQLiteWeatherStore implements WeatherStore{
	static String dbPath = "./src/database/weatherData.db";
	static String csvFilePath = "./src/files/Location.csv";

	public boolean prepareDB(Statement statement){
		try {
			dropTables(statement);
			createTableWeather(statement);
			createTableLocation(statement);
			populatedLocationTable(statement);
			return true;
		}
		catch (Exception e){
			System.out.println("The DB could not be created");
			return false;
		}
	}
	public boolean insertWeatherInfo(Weather weatherData, Statement statement){
		try {
			String insertSQL = "INSERT INTO Weather (temperature, feelsLike, minTemperature, maxTemperature, pressure, seaLevel, groundLevel, humidity, temperatureKf, main, description, timestamp, locationIdentifier)\n" +
					"VALUES (" + weatherData.getTemperature() + ", " +
					weatherData.getFeelsLike() + ", " +
					weatherData.getMinTemperature() + ", " +
					weatherData.getMaxTemperature() + ", " +
					weatherData.getPressure() + ", " +
					weatherData.getSeaLevel() + ", " +
					weatherData.getGroundLevel() + ", " +
					weatherData.getHumidity() + ", " +
					weatherData.getTemperatureKf() + ",\n'" +
					weatherData.getMain() + "', '" +
					weatherData.getDescription() + "', '" +
					weatherData.getTimestamp() + "', " +
					weatherData.getLocationIdentifier() + ");";
			return executeSQLSentence(statement, insertSQL);
		}
		catch (Exception e){
			System.out.println("Problem inserting the weather data of : " + weatherData.getLocationIdentifier());
			return false;
		}
	}
	public Stream<Location> retrieveLocationData(Statement statement) throws SQLException {
		ResultSet rs = statement.executeQuery("SELECT * FROM Location");
		return Stream.generate(() -> {
			try {
				if (rs.next()) {
					int id = rs.getInt("Id");
					String name = rs.getString("name");
					double latitude = rs.getDouble("latitude");
					double longitude = rs.getDouble("longitude");
					return new Location(id, name, latitude, longitude);
				}
			} catch (SQLException e) {
				System.out.println("Problem returning the Locations data");
			}
			return null;
		}).takeWhile(Objects::nonNull);
	}
	public static void populatedLocationTable(Statement statement) {
		String line;
		String csvDelimiter = ";";
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            while ((line = br.readLine()) != null) {
				String[] data = line.split(csvDelimiter);
				if (data.length == 3) {
					String name = data[0];
					double latitude = Double.parseDouble(data[1]);
					double longitude = Double.parseDouble(data[2]);

					// Generate SQL insert statement
					String insertSQL = "INSERT INTO Location (name, latitude, longitude) " +
							"VALUES ('" + name + "', " + latitude + ", " + longitude + ");";

					executeSQLSentence(statement, insertSQL);
				} else {
					System.out.println("Skipping invalid data: " + line);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	private static void createTableWeather(Statement statement) throws SQLException {
		statement.execute("CREATE TABLE IF NOT EXISTS Weather (" +
				"id INTEGER PRIMARY KEY,\n" +
				"temperature REAL,\n" +
				"feelsLike REAL,\n" +
				"minTemperature REAL,\n" +
				"maxTemperature REAL,\n" +
				"pressure INTEGER,\n" +
				"seaLevel INTEGER,\n" +
				"groundLevel INTEGER,\n" +
				"humidity INTEGER,\n" +
				"temperatureKf REAL,\n" +
				"main TEXT,\n" +
				"description TEXT,\n" +
				"timestamp TEXT,\n" +
				"locationIdentifier INTEGER\n" +
				")");
	}

	private static void createTableLocation(Statement statement) throws SQLException {
		statement.execute("CREATE TABLE IF NOT EXISTS Location (" +
				"Id INTEGER PRIMARY KEY,\n" +
				"name TEXT,\n" +
				"latitude REAL,\n" +
				"longitude REAL\n" +
				");");
	}

	private static void dropTables(Statement statement) throws SQLException {
		statement.execute("DROP TABLE IF EXISTS Location;\n");
		statement.execute("DROP TABLE IF EXISTS Weather;\n");
	}

	private static boolean executeSQLSentence(Statement statement, String sqlSentence) throws SQLException {
		return statement.execute(sqlSentence);
	}

	public Connection connect() {
		Connection conn = null;
		try {
			String url = "jdbc:sqlite:" + dbPath;
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
			return conn;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	@Override
	public void close() {

	}
}
