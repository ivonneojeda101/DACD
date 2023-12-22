package org.example.controllers;

import org.example.exceptions.BussinessUnitException;
import org.example.controllers.schemes.Weather;
import org.example.model.DateFlightWeather;

import java.time.Instant;
import java.util.List;

public interface DataManagement{

	public void storeData(String data) throws BussinessUnitException;

	public List<DateFlightWeather> getPrediction(double[] weight) throws BussinessUnitException;
}
