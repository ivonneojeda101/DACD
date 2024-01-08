package org.java.view;

import org.java.controllers.BusinessController;
import org.java.controllers.schemes.Flight;
import org.java.exceptions.BussinessUnitException;
import org.java.model.DateFlightWeather;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserInterface {

	public void start(BusinessController businessController) throws BussinessUnitException {
		businessController.startSubscriber();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to your next trip advisor! \nLet us know what are your weather preferences and we will advice you which destination to visit next week.");
		System.out.println("Introduce values from 1-Low Importance to 10-High Importance for each parameter.\nIf you indicate 0 at any point will mean that this characteristic should not contribute to the calculation.");
		System.out.println("The location with the highest calculated score based on the weighted parameters is chosen as the preferred location.");
		String[] params = {"Temperature", "Humidity", "Clouds", "Wind Speed", "Precipitation Probability"};
		Double[] weights = new Double[params.length];
		String isFirstTime = "yes";
		String choice = "yes";
		while (choice.equalsIgnoreCase("yes")) {
			if(!isFirstTime.equalsIgnoreCase("yes")){
				System.out.println("Do you want to change your preferences (yes/no):");
				isFirstTime = scanner.next();
			}
			if (isFirstTime.equalsIgnoreCase("yes")) {
				for (int i = 0; i < params.length; i++) {
					System.out.println("Please enter the value for " + params[i]);
					weights[i] = getValidWeight(scanner);
				}
				isFirstTime = "no";
			}
			System.out.println("\nWait for the result ... \n");
			printResult(businessController.getRecommendation(weights));
			System.out.println("\nDo you want to check again? (yes/no): ");
			choice = scanner.next();
		}
		scanner.close();
	}

	private double getValidWeight(Scanner scanner) {
		double weight;
		do {
			while (!scanner.hasNextDouble()) {
				System.out.println("Invalid input. Please enter a valid number between 0 and 10:");
				scanner.next();
			}
			weight = scanner.nextDouble();
		} while (weight < 0 || weight > 10);
		return weight/100;
	}

	private void printResult(List<DateFlightWeather> weatherAndFlights){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
		if (!weatherAndFlights.isEmpty()) {
			for (int index = 0; index < weatherAndFlights.size(); index++) {
				DateFlightWeather dailyData = weatherAndFlights.get(index);
				String date = dailyData.getWeather().getPredictionTime().atZone(ZoneOffset.UTC).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				if (index == 0) {
					System.out.println("The recommended destination is : " + dailyData.getWeather().getLocation().getName().toUpperCase());
					System.out.println();
					System.out.println("Following you have the weather conditions by day and available flights if there are any.");
					System.out.println("---------------------------------------------------------------------------------------");
				}
				System.out.println("==================================");
				System.out.println("DATE : " + date);
				System.out.println("==================================");
				System.out.println();
				System.out.printf("%-20s", "Temperature");
				System.out.print(": " + dailyData.getWeather().getTemperature() + "°C");
				System.out.println();
				System.out.printf("%-20s", "Humidity");
				System.out.print(": " + dailyData.getWeather().getHumidity() + " %");
				System.out.println();
				System.out.printf("%-20s", "Clouds");
				System.out.print(": " + dailyData.getWeather().getClouds() + " %");
				System.out.println();
				System.out.printf("%-20s", "Wind Speed");
				System.out.print(": " + dailyData.getWeather().getWindSpeed() + " meter/sec");
				System.out.println();
				System.out.printf("%-20s", "Precipitation Probability");
				System.out.print(": " + (dailyData.getWeather().getPrecipitationProbability() * 100) + " %");
				System.out.println();
				if (!dailyData.getFlights().isEmpty()) {
					System.out.println("-----------------------------------------");
					System.out.println("FLIGHT OFFERS");
					System.out.println("-----------------------------------------");
					Map<String, Flight> flights = dailyData.getFlights();
					flights.forEach((key, flight) -> {
						System.out.println();
						System.out.println("FROM :" + flight.getDepartureAirport() + " AT " + formatter.format(flight.getDepartureDatetime()));
						System.out.println("TO :" + flight.getArrivalAirport() + " AT " + formatter.format(flight.getArrivalDatetime()));
						System.out.println("Operated by :" + flight.getCarrierName());
						System.out.println("Duration :" + flight.getDuration());
						System.out.println("Price :" + flight.getPrice() + flight.getCurrency());
						System.out.println();
					});
				}
			}
		} else {
			System.out.println("There are not locations that fulfilled your preferences");
		}
	}
}
