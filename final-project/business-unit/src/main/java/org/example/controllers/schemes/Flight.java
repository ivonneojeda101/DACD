package org.example.controllers.schemes;

import java.time.Instant;

public class Flight {

	private final String destination;
	private final String departureAirport;
	private final Instant departureDatetime;
	private final String arrivalAirport;
	private final Instant arrivalDatetime;
	private final String carrierName;
	private final String duration;
	private final Double price;
	private final String currency;
	private final Instant predictionTime;
	private final String key;

	public Flight(String destination, String departureAirport, Instant departureDatetime, String arrivalAirport, Instant arrivalDatetime, String carrierName, String duration, Double price, String currency, Instant predictionTime) {
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
		this.key = (departureAirport + departureDatetime.toString() + arrivalAirport + arrivalDatetime.toString() + carrierName).replaceAll(" ","");
	}

	public String getKey() {
		return key;
	}
}