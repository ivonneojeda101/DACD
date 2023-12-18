package org.example.model;

import java.time.Instant;
import java.util.List;

public class IslandFlightTracker {
	private final String name;
	private final List<Flight> flights;
	private final Instant timestamp = Instant.now();
	private final Instant predictionTimestamp;
	private final String sourceStamp;

	public IslandFlightTracker(String name, List<Flight> flights, Instant predictionTimestamp, String sourceStamp) {
		this.name = name;
		this.flights = flights;
		this.predictionTimestamp = predictionTimestamp;
		this.sourceStamp = sourceStamp;
	}
}
