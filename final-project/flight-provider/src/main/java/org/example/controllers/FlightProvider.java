package org.example.controllers;

import org.example.exceptions.CustomException;
import org.example.model.Flight;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface FlightProvider {
	public List<Flight> getFlight(String departureAirport, String arrivalAirport, String islandName, Set<Instant> dateTimes) throws CustomException;
}
