package org.java.controllers;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.java.exceptions.DatalakeBuilderException;

import javax.jms.*;
import java.util.List;

public class AMQDataProvider implements DataProvider{
	private final String url;
	private final List<String> topics;

	public AMQDataProvider(String url, List<String> topics) {
		this.url = url;
		this.topics = topics;

	}

	@Override
	public void getData(DataStore dataStore) throws DatalakeBuilderException {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
			Connection connection = connectionFactory.createConnection();
			connection.setClientID("Data-lakeBuilder");
			connection.start();
			topics.parallelStream().forEach(topic -> {
				try {
					Session session = connection.createSession(false,
							Session.AUTO_ACKNOWLEDGE);
					Topic destination = session.createTopic(topic);
					MessageConsumer consumer = session.createDurableSubscriber(destination, topic);
					consumer.setMessageListener(message -> {
						try {
							dataStore.storeData(((TextMessage)message).getText());
							System.out.println();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					});
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
