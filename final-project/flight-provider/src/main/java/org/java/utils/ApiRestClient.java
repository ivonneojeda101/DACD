package org.java.utils;

import org.java.exceptions.CustomException;
import org.java.utils.schemes.ApiCallParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiRestClient {
	public StringBuilder consumeService (ApiCallParams apiCallParams) throws CustomException {
		StringBuilder response = new StringBuilder();
		try {
			URL url = new URL(apiCallParams.getApiURLEndpoint());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(apiCallParams.getRequestMethod());
			if (!apiCallParams.getAuthToken().isEmpty())
				connection.setRequestProperty("Authorization", "Bearer " + apiCallParams.getAuthToken());
			if (!apiCallParams.getContentType().isEmpty())
				connection.setRequestProperty("Content-Type", apiCallParams.getContentType());
			if (!apiCallParams.getRequestBody().isEmpty()) {
				connection.setDoOutput(true);
				try (OutputStream outputStream = connection.getOutputStream()) {
					outputStream.write(apiCallParams.getRequestBody().getBytes(StandardCharsets.UTF_8));
				}
			}
			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) response.append(line);
				reader.close();
			} else System.out.println("Failed to get a valid response. Response code: " + responseCode);
			connection.disconnect();
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
		return response;
	}
}
