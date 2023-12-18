package org.example.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.exceptions.CustomException;
import org.example.model.Flight;
import org.example.model.IslandFlightTracker;
import org.example.utils.ApiRestClient;
import java.time.*;
import java.time.format.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AmadeusFlightOffersProvider implements FlightProvider {
	private String apiToken;
	private final String apiURL;
	private final String apiURLToken;
	private final String clientId;
	private final String clientSecret;

	public AmadeusFlightOffersProvider(String apiURL, String apiURLToken, String clientId, String clientSecret) {
		this.apiURL = apiURL;
		this.apiURLToken = apiURLToken;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}
	@Override
	public List<IslandFlightTracker> getFlight(String departureAirport, String arrivalAirport, String islandName, Set<Instant> dateTimes) throws CustomException {
		List<IslandFlightTracker> dailyFlights = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		getToken();
		for (Instant dateTime : dateTimes) {
			LocalDate date = dateTime.atZone(ZoneOffset.UTC).toLocalDate();
			String formattedDate = date.format(formatter);
			String url = apiURL + "?originLocationCode=" + departureAirport + "&destinationLocationCode=" + arrivalAirport + "&departureDate=" + formattedDate + "&adults=1&max=3&nonStop=true";
			List<Flight> flights = consumeService(url);
			if (!flights.isEmpty()){
				LocalDate localDate = LocalDate.parse(formattedDate);
				Instant instantDate = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
				dailyFlights.add(new IslandFlightTracker(islandName, flights, instantDate, "flight-provider"));
			}
		}
		return dailyFlights;
	}

	private List<Flight> consumeService(String apiURL) throws CustomException {
		ApiRestClient apiRestClient = new ApiRestClient();
		StringBuilder response = apiRestClient.consumeService(apiURL,"GET", apiToken, "", "");
		List<Flight> flights = new ArrayList<>();
		if (response != null && !response.isEmpty()) {
			JsonObject jsonObject = JsonParser.parseString(String.valueOf(response)).getAsJsonObject();
			JsonArray listFlightOffers = jsonObject.has("data") ? jsonObject.get("data").getAsJsonArray() : new JsonArray();
			if (!listFlightOffers.isEmpty()) {
				JsonObject carriers = jsonObject.get("dictionaries").getAsJsonObject().get("carriers").getAsJsonObject();
				for (JsonElement element : listFlightOffers) {
					JsonObject flightOffer = element.getAsJsonObject();
					Flight flight = createFlight(flightOffer, carriers);
					flights.add(flight);
				}
			}
			else {
				System.out.println("Flights provider not available");
			}
		}
		else {
			System.out.println("Flights provider not available");
		}
		return flights;
	}

	private Flight createFlight(JsonObject flightOffer, JsonObject carriers) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		JsonObject firstSegment = flightOffer
				.getAsJsonArray("itineraries")
				.get(0).getAsJsonObject().getAsJsonArray("segments")
				.get(0).getAsJsonObject();
		String departureAirport = firstSegment.getAsJsonObject("departure").get("iataCode").getAsString();
		String date = firstSegment
				.getAsJsonObject().getAsJsonObject("departure")
				.get("at").getAsString()
				.replaceAll("^\"|\"$", "");
		Instant departureDatetime = LocalDateTime.parse(date, formatter).toInstant(ZoneOffset.UTC);
		String arrivalAirport = firstSegment.getAsJsonObject("arrival").get("iataCode").getAsString();
		date = firstSegment
				.getAsJsonObject().getAsJsonObject("arrival")
				.get("at").getAsString()
				.replaceAll("^\"|\"$", "");
		Instant arrivalDatetime = LocalDateTime.parse(date, formatter).toInstant(ZoneOffset.UTC);
		String carrierName = carriers.get(firstSegment.get("carrierCode").getAsString()).getAsString();
		String duration = firstSegment.get("duration").getAsString().substring(2);
		Double price = flightOffer.getAsJsonObject("price").get("grandTotal").getAsDouble();
		String currency = flightOffer.getAsJsonObject("price").get("currency").getAsString();
		return new Flight(departureAirport, departureDatetime, arrivalAirport, arrivalDatetime, carrierName, duration, price, currency);
	}

	private void getToken() throws CustomException {
		ApiRestClient apiRestClient = new ApiRestClient();
		String requestBody =  "grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;
		StringBuilder response = apiRestClient.consumeService(apiURLToken,"POST", "", requestBody,"application/x-www-form-urlencoded");
		if (response != null && !response.isEmpty()) {
			JsonObject jsonObject = JsonParser.parseString(String.valueOf(response)).getAsJsonObject();
			apiToken = jsonObject.get("access_token").getAsString();
		}
		else {
			throw new CustomException("Authorization token not available");
		}
	}
}
