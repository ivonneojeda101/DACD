package org.java.controllers;

import org.java.exceptions.CustomException;
import org.java.model.Flight;

public interface FlightStore extends AutoCloseable {
	void setFlight(Flight flight) throws CustomException;
}
