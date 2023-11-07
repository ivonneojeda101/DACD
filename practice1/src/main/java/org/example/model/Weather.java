package org.example.model;

public class Weather {
	private final double temperature;
	private final int humidity;
	private final int clouds;
	private final double windSpeed;
	private final int precipitationProbability;
	private final String timestamp;
	private final Location location;

	// Constructor
	public Weather(double temperature, int humidity, int clouds, double windSpeed, int precipitationProbability, String timestamp, Location location) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.clouds = clouds;
		this.windSpeed = windSpeed;
		this.precipitationProbability = precipitationProbability;
		this.timestamp = timestamp;
		this.location = location;
	}
	// Getter methods
	public double getTemperature() {
		return temperature;
	}

	public int getHumidity() {
		return humidity;
	}

	public int getClouds() {
		return clouds;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public int getPrecipitationProbability() {
		return precipitationProbability;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public Location getLocation() {
		return location;
	}
}