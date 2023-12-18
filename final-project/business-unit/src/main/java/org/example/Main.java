package org.example;

import org.example.controllers.AMQDataProvider;
import org.example.controllers.DataProvider;
import org.example.controllers.DataStore;
import org.example.controllers.SQLiteDataStore;

import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		try{
			DataStore dataStore = new SQLiteDataStore(args[4]);
			DataProvider dataProvider = new AMQDataProvider(args[0], Arrays.asList((args[1].split(","))));
			dataProvider.getData(dataStore);
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}