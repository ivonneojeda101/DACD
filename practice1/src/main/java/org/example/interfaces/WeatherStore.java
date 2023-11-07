package org.example.interfaces;

import org.example.model.Location;
import org.example.model.Weather;
import java.util.List;
public interface WeatherStore extends AutoCloseable {
	void setUpStore(List<Location> locations);
	void setWeather(Weather weather);
}
