package org.java.model;

import java.time.Instant;

public class Weather {
	private final double temperature;
	private final int humidity;
	private final int clouds;
	private final double windSpeed;
	private final int precipitationProbability;
	private final Instant timestamp = Instant.now();
	private final Location location;
	private final Instant predictionTimestamp;
	private final String sourceStamp;
	// Constructor
	public Weather(double temperature, int humidity, int clouds, double windSpeed, int precipitationProbability, Location location, Instant predictionTimestamp, String sourceStamp) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.clouds = clouds;
		this.windSpeed = windSpeed;
		this.precipitationProbability = precipitationProbability;
		this.location = location;
		this.predictionTimestamp = predictionTimestamp;
		this.sourceStamp = sourceStamp;
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

	public Instant getTimestamp() {
		return timestamp;
	}

	public Location getLocation() {
		return location;
	}

	public Instant getPredictionTimestamp() { return predictionTimestamp; }

	public String getSourceStamp() { return sourceStamp; }
}