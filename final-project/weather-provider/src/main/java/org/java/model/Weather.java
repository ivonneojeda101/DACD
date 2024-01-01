package org.java.model;

import java.time.Instant;

public class Weather {

	private final Instant ts = Instant.now();
	private final String ss;
	private final Instant predictionTime;
	private final Location location;
	private final double temperature;
	private final int humidity;
	private final int clouds;
	private final double windSpeed;
	private final int precipitationProbability;

	public Weather(double temperature, int humidity, int clouds, double windSpeed, int precipitationProbability, Location location, Instant predictionTime, String sourceStamp) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.clouds = clouds;
		this.windSpeed = windSpeed;
		this.precipitationProbability = precipitationProbability;
		this.location = location;
		this.predictionTime = predictionTime;
		this.ss = sourceStamp;
	}
}
