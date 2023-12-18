package org.example.controllers;

import org.example.exceptions.BussinessUnitException;

public interface DataProvider {
	public void getData(DataStore dataStore) throws BussinessUnitException;
}
