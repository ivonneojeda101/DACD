package org.example.controllers;

import org.example.exceptions.CustomException;
import org.example.model.IslandFlightTracker;

public interface FlightStore extends AutoCloseable {
	void setFlight(IslandFlightTracker flight) throws CustomException;
}
