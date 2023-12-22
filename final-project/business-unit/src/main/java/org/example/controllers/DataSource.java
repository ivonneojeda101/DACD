package org.example.controllers;

import org.example.exceptions.BussinessUnitException;

public interface DataSource {
	public void getData(DataManagement dataManagement) throws BussinessUnitException;
}
