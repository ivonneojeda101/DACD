package org.example.controllers;

import com.google.gson.JsonObject;

public interface DataHandler {
	void handleData(String jsonData, String keyDate, MemoryDataManagement dataManagement);
}
