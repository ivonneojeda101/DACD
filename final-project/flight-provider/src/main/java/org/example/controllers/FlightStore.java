package org.example.controllers;

import org.example.exceptions.CustomException;
import org.example.model.Flight;

public interface FlightStore extends AutoCloseable {
	void setFlight(Flight flight) throws CustomException;
}
