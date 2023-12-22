package org.example;

import org.example.controllers.*;

import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		try{
			BusinessController businessController = new BusinessController();
			DataManagement dataManagement = new MemoryDataManagement(args[2], args[3], Arrays.asList((args[1].split(","))));
			DataSource dataSource = new AMQSuscriber(args[0], Arrays.asList((args[1].split(","))));
			dataSource.getData(dataManagement);
			//businessController.userInterface(dataStore);
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}