package org.example.model;

public class Location {
	private int Id;
	private String name;
	private double latitude; // Represents the latitude coordinate
	private double longitude; // Represents the longitude coordinate

	public Location(int Id, String name, double latitude, double longitude) {
		this.Id = Id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public int getId() {
		return Id;
	}

	public void setId(int Id) {
		this.Id = Id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
