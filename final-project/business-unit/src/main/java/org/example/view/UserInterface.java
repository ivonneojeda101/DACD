package org.example.view;

import org.example.controllers.BusinessController;
import org.example.controllers.schemes.Flight;
import org.example.exceptions.BussinessUnitException;
import org.example.model.DateFlightWeather;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserInterface {

	public void start(BusinessController businessController) throws BussinessUnitException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to your next trip advisor! \n Let us know what are your weather preferences and we will advice you which destination to visit next week");
		String[] params = {"Temperature", "Humidity", "Clouds", "Wind Speed", "Precipitation Probability"};
		double[] weights = new double[params.length];
		String isFirstTime = "yes";
		String choice = "yes";
		//businessController.startSubscriber();
		while (choice.equalsIgnoreCase("yes")) {
			if(!isFirstTime.equalsIgnoreCase("yes")){
				System.out.println("Do you want to change your preferences (yes/no):");
				isFirstTime = scanner.next();
			}
			if (isFirstTime.equalsIgnoreCase("yes")) {
				for (int i = 0; i < params.length; i++) {
					System.out.println("Please enter the value for " + params[i] + getMessage(i));
					weights[i] = getValidWeight(scanner);
				}
				isFirstTime = "no";
			}
			System.out.println("Wait for the result ... \n");
			printResult(businessController.getRecommendation(weights));
			System.out.println("Do you want to check again? (yes/no): ");
			choice = scanner.next();
		}
		scanner.close();
	}

	private String getMessage(int index) {
		return switch (index) {
			case 0 -> " (from 0-Freezing to 10-Very Hot :";
			case 1 -> " (from 0-Dry to 10-Humid):";
			case 2 -> " (from 0-Clear skies to 10-Overcast):";
			case 3 -> " (from 0-Calm to 10-Windy):";
			case 4 -> " (from 0-Rare to 10-Frequent):";
			default -> "";
		};
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

	private void printResult (List<DateFlightWeather> weatherAndFlights){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
		for (DateFlightWeather dailyData: weatherAndFlights) {
			String date = dailyData.getWeather().getPredictionTime().atZone(ZoneOffset.UTC).toLocalDate().format(DateTimeFormatter.BASIC_ISO_DATE);
			System.out.println("The recommended destination is : " + dailyData.getWeather().getLocation().getName().toUpperCase());
			System.out.println();
			System.out.println("---------------------------------------------------------------------------------------");
			System.out.println("==================================");
			System.out.println("DATE : " + date);
			System.out.println("==================================");
			System.out.println();
			System.out.printf("%-20s", "Temperature");
			System.out.print(": " + dailyData.getWeather().getTemperature() + "°C");
			System.out.println();
			System.out.printf("%-20s", "Humidity");
			System.out.print(": " + dailyData.getWeather().getHumidity() + "%");
			System.out.println();
			System.out.printf("%-20s", "Clouds");
			System.out.print(": " + dailyData.getWeather().getClouds() + "%");
			System.out.println();
			System.out.printf("%-20s", "Wind Speed");
			System.out.print(": " + dailyData.getWeather().getWindSpeed() + "km/h");
			System.out.println();
			System.out.printf("%-20s", "Precipitation Probability");
			System.out.print(": " + (dailyData.getWeather().getPrecipitationProbability()*100) + "%");
			System.out.println();
			if (!dailyData.getFlights().isEmpty()) {
				System.out.println("-----------------------------------------");
				System.out.println("FLIGHT OFFERS" );
				System.out.println("-----------------------------------------");
				Map<String, Flight> flights = dailyData.getFlights();
				flights.forEach((key,flight) -> {
					System.out.println();
					System.out.println();
					System.out.println("FROM :" + flight.getDepartureAirport() + " AT " + formatter.format(flight.getDepartureDatetime()));
					System.out.println("TO :" + flight.getArrivalAirport() + " AT " + flight.getArrivalDatetime());
					System.out.println("Operated by :" + flight.getCarrierName());
					System.out.println("Duration :" + flight.getDuration());
					System.out.println("Price :" + flight.getPrice() + flight.getCurrency());
				});
			}
		}
	}
}
