package org.java.controllers;

import org.java.exceptions.DatalakeBuilderException;

public interface DataProvider {
	public void getData(DataStore dataStore) throws DatalakeBuilderException;
}
