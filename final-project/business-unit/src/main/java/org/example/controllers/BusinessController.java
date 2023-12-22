package org.example.controllers;

import java.util.Scanner;

public class BusinessController {

	public void userInterface(DataManagement dataManagement) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to your next trip advisor! \n Let us know what are your weather preferences and we will advice you which destination to visit next week");
		String[] params = {"Temperature", "Humidity", "Clouds", "Wind Speed", "Precipitation Probability"};
		double[] weights = new double[params.length];
		for (int i = 0; i < params.length; i++) {
			System.out.println("Please enter the value for " + params[i] + getMessage(i));
			weights[i] = getValidWeight(scanner);
		}

		System.out.println("Wait for the report ... \n");

		System.out.println("\nEntered weights:");
		for (int i = 0; i < params.length; i++) {
			System.out.println(params[i] + ": " + weights[i] + " (0 - Not important to 10 - Very Important)");
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
}
