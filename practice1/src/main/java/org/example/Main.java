package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.example.SQLiteWeatherStore.*;

public class Main {
	public static void main(String[] args) {
		SQLiteWeatherStore weatherStore = new SQLiteWeatherStore();
		try(Connection connection = weatherStore.connect()) {
			Statement statement = connection.createStatement();
			if (weatherStore.prepareDB(statement)){

			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}


	}

}