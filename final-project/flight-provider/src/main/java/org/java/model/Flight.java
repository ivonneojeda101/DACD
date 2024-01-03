package org.java.model;

import java.time.Instant;

public class Flight {

	private final Instant ts = Instant.now();
	private final String ss;
	private Instant predictionTime;
	private String destination;
	private final String departureAirport;
	private final Instant departureDatetime;
	private final String arrivalAirport;
	private final Instant arrivalDatetime;
	private final String carrierName;
	private final String duration;
	private final Double price;
	private final String currency;

	public Flight(String destination, String departureAirport, Instant departureDatetime, String arrivalAirport, Instant arrivalDatetime, String carrierName, String duration, Double price, String currency, Instant predictionTime, String ss) {
		this.destination = destination;
		this.departureAirport = departureAirport;
		this.departureDatetime = departureDatetime;
		this.arrivalAirport = arrivalAirport;
		this.arrivalDatetime = arrivalDatetime;
		this.carrierName = carrierName;
		this.duration = duration;
		this.price = price;
		this.currency = currency;
		this.predictionTime = predictionTime;
		this.ss = ss;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setPredictionTime(Instant predictionTime) {
		this.predictionTime = predictionTime;
	}
}