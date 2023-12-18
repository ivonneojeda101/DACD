package org.example.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.exceptions.CustomException;
import org.example.model.IslandFlightTracker;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.IOException;
import java.time.Instant;

public class AMQFlightStore implements FlightStore {

	private final String url;
	private final String subject;
	private final Gson gson = prepareGson();

	public AMQFlightStore(String url, String subject) {
		this.url = url;
		this.subject = subject;
	}
	@Override
	public void setFlight(IslandFlightTracker flight) throws CustomException {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
			Connection connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createTopic(subject);
			MessageProducer producer = session.createProducer(destination);
			TextMessage message = session
					.createTextMessage(gson.toJson(flight));
			producer.send(message);
			System.out.println("JCG printing@@ '" + message.getText() + "'");
			connection.close();
		}
		catch (Exception e){
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public void close() {

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
