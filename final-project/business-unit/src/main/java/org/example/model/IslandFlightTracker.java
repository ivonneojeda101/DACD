package org.example.model;

import java.time.Instant;
import java.util.List;

public class IslandFlightTracker {
	private final String islandName;
	private final List<Flight> flights;
	private final Instant timestamp = Instant.now();
	private final Instant predictionTimestamp;
	private final String sourceStamp;

	public IslandFlightTracker(String islandName, List<Flight> flights, Instant predictionTimestamp, String sourceStamp) {
		this.islandName = islandName;
		this.flights = flights;
		this.predictionTimestamp = predictionTimestamp;
		this.sourceStamp = sourceStamp;
	}
}
