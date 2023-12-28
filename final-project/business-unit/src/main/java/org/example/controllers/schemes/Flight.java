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

	public String getDestination() {
		return destination;
	}

	public String getDepartureAirport() {
		return departureAirport;
	}

	public Instant getDepartureDatetime() {
		return departureDatetime;
	}

	public String getArrivalAirport() {
		return arrivalAirport;
	}

	public Instant getArrivalDatetime() {
		return arrivalDatetime;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public String getDuration() {
		return duration;
	}

	public Double getPrice() {
		return price;
	}

	public String getCurrency() {
		return currency;
	}

	public Instant getPredictionTime() {
		return predictionTime;
	}
	public String getKey() {
		return key;
	}
}