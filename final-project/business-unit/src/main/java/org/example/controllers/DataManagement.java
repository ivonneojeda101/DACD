package org.example.controllers;

import org.example.exceptions.BussinessUnitException;
import org.example.model.DateFlightWeather;

import java.util.List;
import java.util.Map;

public interface DataManagement{

	void storeData(String data) throws BussinessUnitException;
	void deleteData(List<String> keyDates);
	Map<String, DateFlightWeather> getDataGrid();
}
