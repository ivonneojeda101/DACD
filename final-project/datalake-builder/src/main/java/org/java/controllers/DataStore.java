package org.java.controllers;

import org.java.exceptions.DatalakeBuilderException;

public interface DataStore {
	void storeData(String data, String source) throws DatalakeBuilderException;
}
