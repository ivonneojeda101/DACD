package org.java.controllers;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.java.exceptions.DatalakeBuilderException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class FileDataStore implements DataStore{

	private final String directory;
	private final Gson gson = prepareGson();

	public FileDataStore(String directory) {
		this.directory = directory;
	}

	@Override
	public void storeData(String jsonData, String source) throws DatalakeBuilderException {
		try {
			JsonObject jsonEvent = JsonParser.parseString(String.valueOf(jsonData)).getAsJsonObject();
			String timestamp = jsonEvent.get("ts").toString();
			timestamp = timestamp.replaceAll("^\"|\"$", "");
			System.out.println(timestamp);
			Instant instant = Instant.parse(timestamp);
			String yyyymmdd = instant.atZone(ZoneOffset.UTC).toLocalDate().format(DateTimeFormatter.BASIC_ISO_DATE);
			String filename = directory + source + "/" + yyyymmdd + ".events";
			jsonData = jsonData.replaceAll("\n", "");
			Path filePath = Paths.get(filename);
			if (Files.exists(filePath)) {
				try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
					writer.write("\n" + jsonData);
					System.out.println("Appended to existing file: " + filename);
				}
			} else {
				jsonData = "\n" + jsonData;
				Files.createDirectories(filePath.getParent());
				Files.write(filePath, jsonData.getBytes());
				System.out.println("Created new file: " + filename);
			}
		}
		catch (Exception e) {
			throw new DatalakeBuilderException(e.getMessage());
		}
	}


	private static Gson prepareGson() {
		return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Instant.class, new TypeAdapter<Instant>() {
			@Override
			public void write(JsonWriter out, Instant value) throws IOException {
				out.value(value.toString());
			}

			@Override
			public Instant read(JsonReader in) throws IOException {
				return Instant.parse(in.nextString());
			}
		}).create();
	}
}
