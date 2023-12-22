package org.example.controllers.schemes;

import java.time.Instant;

public class Location {
	private final String name;
	private final double latitude;
	private final double longitude;

	public Location(String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public String getName() {
		return name;
	}

}