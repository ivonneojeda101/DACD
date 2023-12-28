package org.example;

import org.example.controllers.*;
import org.example.controllers.schemes.DataLakeDataSource;
import org.example.view.UserInterface;

import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		try{
			DataManagement dataManagement = new MemoryDataManagement();
			DataSource dataSource = new AMQSuscriber(args[0], Arrays.asList((args[1].split(","))));
			DataSource datalake = new DataLakeDataSource(args[2], Arrays.asList((args[1].split(","))));
			datalake.getData(dataManagement);
			BusinessController businessController = new BusinessController(dataSource, dataManagement, args[3]);
			UserInterface userInterface = new UserInterface();
			userInterface.start(businessController);
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}