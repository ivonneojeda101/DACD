package org.example.controllers.schemes;

import org.example.controllers.DataManagement;
import org.example.controllers.DataSource;
import org.example.exceptions.BussinessUnitException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

public class DataLakeDataSource implements DataSource {

	private final String urlDatalake;
	private final List<String> topics;

	public DataLakeDataSource(String urlDatalake, List<String> topics) {
		this.urlDatalake = urlDatalake;
		this.topics = topics;
	}

	@Override
	public void getData(DataManagement dataManagement) throws BussinessUnitException {
		for (String topic : topics) {
			File directory = new File(urlDatalake + topic);
			if (directory.isDirectory()) {
				File[] files = directory.listFiles();
				if (files != null && files.length > 0) {
					Arrays.sort(files);
					File lastFile = files[files.length - 1];
					try (BufferedReader reader = new BufferedReader(new FileReader(lastFile))) {
						String line;
						while ((line = reader.readLine()) != null) {
							if (!line.isEmpty()){
								dataManagement.storeData(line);
							}
						}
					} catch (Exception e) {
						throw new BussinessUnitException(e.getMessage());
					}

				} else {
					System.out.println("No files found.");
				}
			}
		}
	}
}
