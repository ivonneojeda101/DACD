package org.java.controllers;

import org.java.exceptions.DatalakeBuilderException;

public interface DataSource {
	void getData(DataStore dataStore) throws DatalakeBuilderException;
}
