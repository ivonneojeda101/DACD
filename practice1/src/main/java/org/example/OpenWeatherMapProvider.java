package org.example;

import com.google.gson.*;
import org.example.interfaces.WeatherProvider;
import org.example.model.Location;
import org.example.model.Weather;
import org.example.utils.ApiRestClient;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OpenWeatherMapProvider implements WeatherProvider {
	private String apiKey;
	private String apiURL;

	public OpenWeatherMapProvider(String apiKey, String apiURL) {
		this.apiKey = apiKey;
		this.apiURL = apiURL;
	}

	public List<Weather> getWeather(Location location, Set<Instant> dateTimes){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		List<Weather> forecast = new ArrayList<>();
		ApiRestClient apiRestClient = new ApiRestClient();
		String url = apiURL + "?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&units=metric&appid=" + apiKey;
		StringBuilder response = apiRestClient.consumeService(url,"GET");
		JsonObject jsonObject = JsonParser.parseString(String.valueOf(response)).getAsJsonObject();
		JsonArray listTimeForecast = jsonObject.get("list").getAsJsonArray();
		for (JsonElement element : listTimeForecast) {
			JsonObject hourForecast = element.getAsJsonObject();
			Instant timestampJSON = Instant.parse(hourForecast.get("dt_txt").getAsString());
			if (dateTimes.contains(timestampJSON)){
				forecast.add(createWeather(hourForecast, location));
			}
		}
		return forecast;
	}

	private Weather createWeather(JsonObject hourForecast, Location location){
		double temperature = hourForecast.getAsJsonObject("main").get("temp").getAsDouble();
		int humidity = hourForecast.getAsJsonObject("main").get("pressure").getAsInt();
		int clouds = hourForecast.getAsJsonObject("clouds").get("all").getAsInt();
		double windSpeed = hourForecast.getAsJsonObject("wind").get("speed").getAsDouble();
		int precipitationProbability = hourForecast.get("pop").getAsInt();
		String timestamp = hourForecast.get("dt_txt").getAsString();
		return new Weather(temperature,humidity,clouds,windSpeed,precipitationProbability,timestamp,location);
	}
}
