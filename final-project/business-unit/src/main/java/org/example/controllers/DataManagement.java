package org.example.controllers;

import org.example.exceptions.BussinessUnitException;
import org.example.model.DateFlightWeather;

import java.util.List;
import java.util.Map;

public interface DataManagement{

	public void storeData(String data) throws BussinessUnitException;
	public Map<String, DateFlightWeather> getDataGrid();
}
