package org.example.utils;

import org.example.exceptions.CustomException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiRestClient {
	public StringBuilder consumeService (String apiURLEndpoint, String requestMethod, String authToken, String requestBody, String contentType) throws CustomException {
		StringBuilder response = new StringBuilder();
		try {
			URL url = new URL(apiURLEndpoint);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(requestMethod);
			if (!authToken.isEmpty()) {
				connection.setRequestProperty("Authorization", "Bearer " + authToken);
			}

			if (!contentType.isEmpty()) {
				connection.setRequestProperty("Content-Type", contentType);
			}
			if (!requestBody.isEmpty()) {
				connection.setDoOutput(true);
				try (OutputStream outputStream = connection.getOutputStream()) {
					outputStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
				}
			}

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
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		return response;
	}
}
