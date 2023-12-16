package org.java.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class ApiRestClient {
	public StringBuilder consumeService (String apiURLEndpoint, String requestMethod){
		StringBuilder response = new StringBuilder();
		try {
			URL url = new URL(apiURLEndpoint);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(requestMethod);
			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
			} else {
				System.out.println("Failed to get a valid response. Response code: " + responseCode);
			}
			connection.disconnect();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return response;
	}
}
