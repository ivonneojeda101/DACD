package org.java.controllers;

import org.java.exceptions.DatalakeBuilderException;

public interface DataSource {
	public void getData(DataStore dataStore) throws DatalakeBuilderException;
}
