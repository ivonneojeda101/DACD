package org.example.controllers.schemes;

public class Location {
	private final String name;
	public Location(String name, double latitude, double longitude) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

}
