package org.example;

import com.google.gson.*;
import org.example.model.Location;
import org.example.model.Weather;
import org.example.utils.ApiRestClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OpenWeatherMapProvider implements WeatherProvider{
	String apiURL = "http://api.openweathermap.org/data/2.5/forecast";
	public List<Weather> getWeatherData(Double longitude, Double latitude, String apiKey, Set<String> specificDateTimes){
		List<Weather> forecast = new ArrayList<>();
		ApiRestClient apiRestClient = new ApiRestClient();
		String url = apiURL + "?lat=" + latitude + "&lon=" + longitude + "&units=metric&appid=" + apiKey;
		StringBuilder response = apiRestClient.consumeService(url,"GET");
		JsonObject jsonObject = JsonParser.parseString(String.valueOf(response)).getAsJsonObject();
		JsonArray listTimeForecast = jsonObject.get("list").getAsJsonArray();
		for (JsonElement element : listTimeForecast) {
			JsonObject hourForecast = element.getAsJsonObject();
			String timestampJSON = hourForecast.get("dt").getAsString();
			if (specificDateTimes.contains(timestampJSON)){
				forecast.add(createWeatherObject(hourForecast));
			}
		}
		return forecast;
	}

	private static Weather createWeatherObject(JsonObject hourForecast){
		double temperature = hourForecast.getAsJsonObject("main").get("temp").getAsDouble();
		double feelsLike = hourForecast.getAsJsonObject("main").get("feels_like").getAsDouble();
		double minTemperature = hourForecast.getAsJsonObject("main").get("temp_min").getAsDouble();
		double maxTemperature = hourForecast.getAsJsonObject("main").get("temp_max").getAsDouble();
		int pressure = hourForecast.getAsJsonObject("main").get("pressure").getAsInt();
		int seaLevel = hourForecast.getAsJsonObject("main").get("sea_level").getAsInt();
		int groundLevel = hourForecast.getAsJsonObject("main").get("grnd_level").getAsInt();
		int humidity = hourForecast.getAsJsonObject("main").get("humidity").getAsInt();
		double temperatureKf = hourForecast.getAsJsonObject("main").get("temp_kf").getAsDouble();
		String main = hourForecast.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		String description = hourForecast.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
		String timestamp = hourForecast.get("dt_txt").getAsString();
		Location location = null;
		return new Weather(temperature,feelsLike,minTemperature,maxTemperature,pressure,seaLevel,groundLevel,humidity,temperatureKf,main,description,timestamp,location);
	}

}
