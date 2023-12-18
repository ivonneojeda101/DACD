package org.example.model;

import java.time.Instant;

public class Flight {

	private final String departureAirport;
	private final Instant departureDatetime;
	private final String arrivalAirport;
	private final Instant arrivalDatetime;
	private final String carrierName;
	private final String duration;
	private final Double price;
	private final String currency;
	public Flight(String departureAirport, Instant departureDatetime, String arrivalAirport, Instant arrivalDatetime, String carrierName, String duration, Double price, String currency) {
		this.departureAirport = departureAirport;
		this.departureDatetime = departureDatetime;
		this.arrivalAirport = arrivalAirport;
		this.arrivalDatetime = arrivalDatetime;
		this.carrierName = carrierName;
		this.duration = duration;
		this.price = price;
		this.currency = currency;
	}
}