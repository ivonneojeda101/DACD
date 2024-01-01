package org.java.controllers;

import com.google.gson.*;
import org.java.exceptions.DatalakeBuilderException;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class FileDataStore implements DataStore{

	private final String directory;


	public FileDataStore(String directory) {
		this.directory = directory;
	}

	@Override
	public void storeData(String jsonData, String topic) throws DatalakeBuilderException {
		try {
			JsonObject jsonEvent = JsonParser.parseString(String.valueOf(jsonData)).getAsJsonObject();
			String timestamp = jsonEvent.get("ts").toString();
			String source = jsonEvent.get("ss").toString().replaceAll("^\"|\"$", "");
			timestamp = timestamp.replaceAll("^\"|\"$", "");
			Instant instant = Instant.parse(timestamp);
			String date = instant.atZone(ZoneOffset.UTC).toLocalDate().format(DateTimeFormatter.BASIC_ISO_DATE);
			String filename = directory + topic + "/" + source + "/" + date + ".events";
			jsonData = jsonData.replaceAll("\n", "");
			Path filePath = Paths.get(filename);
			if (Files.exists(filePath)) {
				try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
					writer.write("\n" + jsonData);
				}
			} else {
				jsonData = "\n" + jsonData;
				Files.createDirectories(filePath.getParent());
				Files.write(filePath, jsonData.getBytes());
			}
		}
		catch (Exception e) {
			throw new DatalakeBuilderException(e.getMessage());
		}
	}
}
