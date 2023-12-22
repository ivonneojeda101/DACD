package org.java;

import org.java.controllers.*;

import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		DataStore dataStore = new FileDataStore(args[2]);
		DataSource dataSource = new AMQClient(args[0], Arrays.asList((args[1].split(","))));
		try{
			dataSource.getData(dataStore);
		}
		catch (Exception e) {
			System.out.println(e);
		}

	}
}