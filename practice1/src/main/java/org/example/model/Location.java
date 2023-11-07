package org.example.model;

public class Location {
	private final String name;
	private final double latitude; // Represents the latitude coordinate
	private final double longitude; // Represents the longitude coordinate

	public Location(String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public String getName() {
		return name;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
}
