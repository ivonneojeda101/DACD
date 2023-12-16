package org.java.controllers;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.java.exceptions.DatalakeBuilderException;

import javax.jms.*;

public class AMQDataProvider implements DataProvider{
	private final String url;
	private final String subject;

	public AMQDataProvider(String url, String subject) {
		this.url = url;
		this.subject = subject;
	}

	@Override
	public void getData(DataStore dataStore) throws DatalakeBuilderException {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
			Connection connection = connectionFactory.createConnection();
			connection.setClientID("DatalakeBuilder");
			connection.start();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Topic destination = session.createTopic(subject);
			MessageConsumer consumer = session.createDurableSubscriber(destination, "DatalakeBuilder");
			consumer.setMessageListener(message -> {
				try {
					dataStore.storeData(((TextMessage)message).getText());
					System.out.println();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});

		}
		catch (Exception e) {
			throw new DatalakeBuilderException(e.getMessage());
		}
	}
}
