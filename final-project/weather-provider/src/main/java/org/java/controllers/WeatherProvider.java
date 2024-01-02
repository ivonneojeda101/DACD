package org.java.controllers;

import org.java.model.Location;
import org.java.model.Weather;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface WeatherProvider {
	List<Weather> getWeather(Location location, Set<Instant> dateTimes);
}
