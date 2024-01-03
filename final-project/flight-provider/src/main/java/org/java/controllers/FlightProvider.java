package org.java.controllers;

import org.java.exceptions.CustomException;
import org.java.model.Flight;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface FlightProvider {
	List<Flight> getFlight(String departureAirport, String arrivalAirport, String islandName, Set<Instant> dateTimes) throws CustomException;
}
