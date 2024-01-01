package org.example.controllers;

import org.example.exceptions.BussinessUnitException;

public interface DataSource {
	void getData(DataManagement dataManagement) throws BussinessUnitException;
}
