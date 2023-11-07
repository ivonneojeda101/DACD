package org.example.model;

public class Weather {
	private double temperature;
	private double feelsLike;
	private double minTemperature;
	private double maxTemperature;
	private int pressure;
	private int seaLevel;
	private int groundLevel;
	private int humidity;
	private double temperatureKf;
	private String main;
	private String description;
	private String timestamp;
	private Location location;

	// Constructor
	public Weather(double temperature, double feelsLike, double minTemperature, double maxTemperature, int pressure, int seaLevel, int groundLevel, int humidity, double temperatureKf, String main, String description, String timestamp, Location location) {
		this.temperature = temperature;
		this.feelsLike = feelsLike;
		this.minTemperature = minTemperature;
		this.maxTemperature = maxTemperature;
		this.pressure = pressure;
		this.seaLevel = seaLevel;
		this.groundLevel = groundLevel;
		this.humidity = humidity;
		this.temperatureKf = temperatureKf;
		this.main = main;
		this.description = description;
		this.timestamp = timestamp;
		this.location = location;
	}

	// Getters and Setters
	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getFeelsLike() {
		return feelsLike;
	}

	public void setFeelsLike(double feelsLike) {
		this.feelsLike = feelsLike;
	}

	public double getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(double minTemperature) {
		this.minTemperature = minTemperature;
	}

	public double getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(double maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public int getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public int getSeaLevel() {
		return seaLevel;
	}

	public void setSeaLevel(int seaLevel) {
		this.seaLevel = seaLevel;
	}

	public int getGroundLevel() {
		return groundLevel;
	}

	public void setGroundLevel(int groundLevel) {
		this.groundLevel = groundLevel;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public double getTemperatureKf() {
		return temperatureKf;
	}

	public void setTemperatureKf(double temperatureKf) {
		this.temperatureKf = temperatureKf;
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
