package org.java.controllers;

import org.java.exceptions.BussinessUnitException;
import org.java.model.DateFlightWeather;

import java.util.List;
import java.util.Map;

public interface DataManagement{

	void storeData(String data) throws BussinessUnitException;
	void deleteData(List<String> keyDates);
	Map<String, DateFlightWeather> getDataGrid();
}
