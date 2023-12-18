package org.example.controllers;

import org.example.exceptions.CustomException;
import org.example.model.Flight;
import org.example.model.IslandFlightTracker;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface FlightProvider {

	public List<IslandFlightTracker> getFlight(String departureAirport, String arrivalAirport, Set<Instant> dateTimes) throws CustomException;
}
