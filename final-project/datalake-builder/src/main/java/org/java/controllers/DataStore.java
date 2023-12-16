package org.java.controllers;

import org.java.exceptions.DatalakeBuilderException;

public interface DataStore {

	public void storeData(String data) throws DatalakeBuilderException;
}
