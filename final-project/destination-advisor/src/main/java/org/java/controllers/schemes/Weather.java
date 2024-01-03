package org.java.controllers.schemes;

import java.time.Instant;

public class Weather {
	private final double temperature;
	private final int humidity;
	private final int clouds;
	private final double windSpeed;
	private final int precipitationProbability;
	private final Location location;
	private final Instant predictionTime;

	public Weather() {
		this.temperature = 0.0;
		this.humidity = 0;
		this.clouds = 0;
		this.windSpeed = 0.0;
		this.precipitationProbability = 0;
		this.location = null;
		this.predictionTime = Instant.now();
	}

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

	public Location getLocation() {
		return location;
	}

	public Instant getPredictionTime() {
		return predictionTime;
	}
}
