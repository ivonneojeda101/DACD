package org.java.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.java.exceptions.CustomException;
import org.java.model.Flight;

import javax.jms.*;
import java.io.IOException;
import java.time.Instant;

public class AMQFlightStore implements FlightStore {
	private final Gson gson = prepareGson();
	private final Session session;
	private final MessageProducer producer;
	private final Connection connection;
	public AMQFlightStore(String url, String subject) throws CustomException {
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
			throw new CustomException(e.getMessage());
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
	@Override
	public void setFlight(Flight flight) throws CustomException {
		try {
			TextMessage message = session
					.createTextMessage(gson.toJson(flight));
			producer.send(message);
			System.out.println("JCG printing@@ '" + message.getText() + "'");
		}
		catch (Exception e){
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public void close() throws CustomException {
		try {
			connection.close();
		}
		catch (Exception e){
			throw new CustomException(e.getMessage());
		}
	}
}
