package org.java.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.java.model.Location;
import org.java.model.Weather;
import org.java.utils.ApiRestClient;
import java.time.*;
import java.time.format.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OpenWeatherMapProvider implements WeatherProvider {
	private final String apiKey;
	private final String apiURL;
	public OpenWeatherMapProvider(String apiKey, String apiURL) {
		this.apiKey = apiKey;
		this.apiURL = apiURL;
	}

	public List<Weather> getWeather(Location location, Set<Instant> dateTimes){
		List<Weather> forecast = new ArrayList<>();
		ApiRestClient apiRestClient = new ApiRestClient();
		String url = apiURL + "?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&units=metric&appid=" + apiKey;
		StringBuilder response = apiRestClient.consumeService(url,"GET");
		if (response != null && !response.isEmpty()) {
			JsonObject jsonObject = JsonParser.parseString(String.valueOf(response)).getAsJsonObject();
			JsonArray listTimeForecast = jsonObject.get("list").getAsJsonArray();
			for (JsonElement element : listTimeForecast) {
				JsonObject hourForecast = element.getAsJsonObject();
				Instant timestampJSON = Instant.ofEpochSecond(hourForecast.get("dt").getAsLong());
				if (dateTimes.contains(timestampJSON)){
					forecast.add(createWeather(hourForecast, location));
				}
			}
		}
		else {
			System.out.println("Weather service is not available");
		}
		return forecast;
	}

	private Weather createWeather(JsonObject hourForecast, Location location){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		double temperature = hourForecast.getAsJsonObject("main").get("temp").getAsDouble();
		int humidity = hourForecast.getAsJsonObject("main").get("humidity").getAsInt();
		int clouds = hourForecast.getAsJsonObject("clouds").get("all").getAsInt();
		double windSpeed = hourForecast.getAsJsonObject("wind").get("speed").getAsDouble();
		int precipitationProbability = hourForecast.get("pop").getAsInt();
		LocalDateTime localDateTime = LocalDateTime.parse(hourForecast.get("dt_txt").getAsString(), formatter);
		Instant predictionTimestamp = localDateTime.atZone(ZoneId.systemDefault()).toInstant();;
		return new Weather(temperature,humidity,clouds,windSpeed,precipitationProbability, location, predictionTimestamp, "prediction-provider");
	}
}
