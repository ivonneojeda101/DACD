package org.java.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.java.exceptions.CustomException;
import org.java.model.Flight;
import org.java.utils.ApiRestClient;
import org.java.utils.schemes.ApiCallParams;

import java.time.*;
import java.time.format.*;
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
	public List<Flight> getFlight(String departureAirport, String arrivalAirport, String destination, Set<Instant> dateTimes) throws CustomException {
		List<Flight> dailyFlights = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		getToken();
		for (Instant dateTime : dateTimes) {
			String date = dateTime.atZone(ZoneOffset.UTC).toLocalDate().format(formatter);
			String url = apiURL + "?originLocationCode=" + departureAirport + "&destinationLocationCode=" + arrivalAirport + "&departureDate=" + date + "&adults=1&max=3&nonStop=true";
			Instant predictionTime = LocalDate.parse(date).atStartOfDay(ZoneId.systemDefault()).toInstant();
			List<Flight> flights = retrieveFlight(url, destination, predictionTime);
			if (!flights.isEmpty()) dailyFlights.addAll(flights);
		}
		return dailyFlights;
	}

	private void getToken() throws CustomException {
		ApiRestClient apiRestClient = new ApiRestClient();
		String requestBody =  "grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;
		StringBuilder response = apiRestClient.consumeService(new ApiCallParams(apiURLToken,"POST", "", requestBody,"application/x-www-form-urlencoded"));
		if (response != null && !response.isEmpty()) {
			JsonObject jsonObject = JsonParser.parseString(String.valueOf(response)).getAsJsonObject();
			apiToken = jsonObject.get("access_token").getAsString();
		}
		else throw new CustomException("Authorization token not available");
	}

	private List<Flight> retrieveFlight(String apiURL, String destination, Instant predictionTime) throws CustomException {
		ApiRestClient apiRestClient = new ApiRestClient();
		StringBuilder response = apiRestClient.consumeService(new ApiCallParams(apiURL,"GET", apiToken, "", ""));
		List<Flight> flights = new ArrayList<>();
		if (response != null && !response.isEmpty()) {
			JsonObject jsonObject = JsonParser.parseString(String.valueOf(response)).getAsJsonObject();
			JsonArray listFlightOffers = jsonObject.has("data") ? jsonObject.get("data").getAsJsonArray() : new JsonArray();
			if (!listFlightOffers.isEmpty()) {
				JsonObject carriers = jsonObject.get("dictionaries").getAsJsonObject().get("carriers").getAsJsonObject();
				for (JsonElement element : listFlightOffers) {
					Flight flight = createFlight(element.getAsJsonObject(), carriers);
					flight.setDestination(destination);
					flight.setPredictionTime(predictionTime);
					flights.add(flight);
				}
			}
		}
		return flights;
	}

	private Flight createFlight(JsonObject flightOffer, JsonObject carriers) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		JsonObject firstSegment = flightOffer.getAsJsonArray("itineraries").get(0).getAsJsonObject().getAsJsonArray("segments").get(0).getAsJsonObject();
		String departureAirport = firstSegment.getAsJsonObject("departure").get("iataCode").getAsString();
		Instant departureDatetime = LocalDateTime.parse(firstSegment.getAsJsonObject().getAsJsonObject("departure").get("at").getAsString().replaceAll("^\"|\"$", ""), formatter).toInstant(ZoneOffset.UTC);
		String arrivalAirport = firstSegment.getAsJsonObject("arrival").get("iataCode").getAsString();
		Instant arrivalDatetime = LocalDateTime.parse(firstSegment.getAsJsonObject().getAsJsonObject("arrival").get("at").getAsString().replaceAll("^\"|\"$", ""), formatter).toInstant(ZoneOffset.UTC);
		String carrierName = carriers.get(firstSegment.get("carrierCode").getAsString()).getAsString();
		String duration = firstSegment.get("duration").getAsString().substring(2);
		Double price = flightOffer.getAsJsonObject("price").get("grandTotal").getAsDouble();
		String currency = flightOffer.getAsJsonObject("price").get("currency").getAsString();
		return new Flight("", departureAirport, departureDatetime, arrivalAirport, arrivalDatetime, carrierName, duration, price, currency, null, "flight-provider");
	}
}
