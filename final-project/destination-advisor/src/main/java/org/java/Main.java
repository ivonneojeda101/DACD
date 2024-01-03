package org.java;

import org.java.controllers.*;
import org.java.view.UserInterface;

import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		try{
			DataManagement dataManagement = new MemoryDataManagement();
			DataSource dataSource = new AMQSubscriber(args[0], Arrays.asList((args[1].split(","))));
			DataSource dataLake = new DataLakeDataSource(args[2], Arrays.asList((args[1].split(","))));
			dataLake.getData(dataManagement);
			BusinessController businessController = new BusinessController(dataSource, dataManagement, args[3]);
			UserInterface userInterface = new UserInterface();
			userInterface.start(businessController);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}