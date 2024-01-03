package org.java.controllers;

import org.java.exceptions.BussinessUnitException;

public interface DataSource {
	void getData(DataManagement dataManagement) throws BussinessUnitException;
}
