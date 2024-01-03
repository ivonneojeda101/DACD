package org.java.utils.schemes;

public class ApiCallParams {
	private final String apiURLEndpoint;
	private final String requestMethod;
	private final String authToken;
	private final String requestBody;
	private final String contentType;

	public ApiCallParams(String apiURLEndpoint, String requestMethod, String authToken, String requestBody, String contentType) {
		this.apiURLEndpoint = apiURLEndpoint;
		this.requestMethod = requestMethod;
		this.authToken = authToken;
		this.requestBody = requestBody;
		this.contentType = contentType;
	}

	public String getApiURLEndpoint() {
		return apiURLEndpoint;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public String getAuthToken() {
		return authToken;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public String getContentType() {
		return contentType;
	}
}
