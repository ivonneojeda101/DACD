package org.java.controllers;

import org.java.exceptions.BussinessUnitException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

public class DataLakeDataSource implements DataSource {

	private final String urlDataLake;
	private final List<String> topics;

	public DataLakeDataSource(String urlDataLake, List<String> topics) {
		this.urlDataLake = urlDataLake;
		this.topics = topics;
	}

	@Override
	public void getData(DataManagement dataManagement) throws BussinessUnitException {
		for (String topic : topics) {
			File mainDirectory = new File(urlDataLake + topic);
			if (mainDirectory.isDirectory()) {
				File[] directories = mainDirectory.listFiles();
				if (directories != null) {
					for (File directory: directories) {
						if (directory.isDirectory()) {
							File[] files = directory.listFiles();
							if (files != null && files.length > 0) {
								Arrays.sort(files);
								processFile(files[files.length - 1], dataManagement);
							}
						}
					}
				}
			}
		}
	}

	private void processFile(File lastFile, DataManagement dataManagement) throws BussinessUnitException {
		try (BufferedReader reader = new BufferedReader(new FileReader(lastFile))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty()) {
					dataManagement.storeData(line);
				}
			}
		} catch (Exception e) {
			throw new BussinessUnitException(e.getMessage());
		}
	}
}
