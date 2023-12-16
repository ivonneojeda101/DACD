package org.java;

import org.java.controllers.DataController;
import org.java.controllers.DataStore;
import org.java.controllers.FileDataStore;

public class Main {
	public static void main(String[] args) {
		DataStore dataStore = new FileDataStore(args[1]);
		DataController dataController = new DataController();
		try{
			dataController.fetchData(args[0], dataStore);
		}
		catch (Exception e) {
			System.out.println(e);
		}

	}
}