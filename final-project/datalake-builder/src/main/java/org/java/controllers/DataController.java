package org.java.controllers;

import org.java.exceptions.DatalakeBuilderException;

import java.util.*;
public class DataController {

	public void fetchData(String dataSource, DataStore dataStore) throws DatalakeBuilderException {
		try {

			String[] outerParts = dataSource.substring(2, dataSource.length() - 2).split("\\}, \\{");
			List<DataProvider> dataProviders = new ArrayList<>();
			for (String outerPart : outerParts) {
				String[] innerParts = outerPart.split(",");
				String url = innerParts[0];
				String topic = innerParts[1];
				DataProvider provider = new AMQDataProvider(url, topic);
				dataProviders.add(provider);
			}

			//dataProviders.get(0).getData(dataStore);

			dataProviders.parallelStream().forEach(provider -> {
				try {
					provider.getData(dataStore);
				} catch (DatalakeBuilderException e) {
					throw new RuntimeException(e);
				}
			});
		}
		catch (Exception e){
			throw new DatalakeBuilderException(e.getMessage());
		}
	}
}
