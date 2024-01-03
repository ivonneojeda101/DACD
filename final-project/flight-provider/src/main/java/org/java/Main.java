package org.java;

import org.java.controllers.*;
import org.java.exceptions.CustomException;

public class Main {
	public static void main(String[] args) throws CustomException {
		FlightProvider flightProvider = new AmadeusFlightOffersProvider(args[0], args[1], args[2], args[3]);
		FlightStore flightStore = new AMQFlightStore(args[4], args[5]);
		FlightController flightcontroller = new FlightController(flightStore, flightProvider, args[6], args[7]);
		try{
			flightcontroller.fetchFlight(args[8]);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}