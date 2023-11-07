package org.example.interfaces;

import org.example.model.Location;
import org.example.model.Weather;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface WeatherProvider {
	public List<Weather>  getWeather(Location location, Set<Instant> dateTimes);
}
