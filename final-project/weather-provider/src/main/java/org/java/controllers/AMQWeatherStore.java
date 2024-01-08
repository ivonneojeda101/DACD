package org.java.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.java.exceptions.WeatherException;
import org.java.model.Weather;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.IOException;
import java.time.Instant;

public class AMQWeatherStore implements WeatherStore {
	private final Gson gson = prepareGson();

	private final Session session;
	private final MessageProducer producer;
	private final Connection connection;
	public AMQWeatherStore(String url, String subject) throws WeatherException {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createTopic(subject);
			producer = session.createProducer(destination);
		}
		catch (Exception e){
			throw new WeatherException(e.getMessage());
		}
	}

	@Override
	public void setWeather(Weather weather) throws WeatherException {
		try {
			TextMessage message = session
					.createTextMessage(gson.toJson(weather));
			producer.send(message);
			System.out.println("JCG printing@@ '" + message.getText() + "'");
		}
		catch (Exception e){
			throw new WeatherException(e.getMessage());
		}
	}

	@Override
	public void close() throws WeatherException {
		try {
			connection.close();
		}
		catch (Exception e){
			throw new WeatherException(e.getMessage());
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
