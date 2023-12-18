package org.example.controllers;

import org.example.exceptions.BussinessUnitException;

public interface DataStore extends AutoCloseable{

	public void storeData(String data) throws BussinessUnitException;
}
