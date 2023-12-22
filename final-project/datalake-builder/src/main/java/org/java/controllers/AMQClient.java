package org.java.controllers;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.java.exceptions.DatalakeBuilderException;

import javax.jms.*;
import java.util.List;

public class AMQClient implements DataSource {
	private final String url;
	private final List<String> topics;

	public AMQClient(String url, List<String> topics) {
		this.url = url;
		this.topics = topics;

	}

	@Override
	public void getData(DataStore dataStore) throws DatalakeBuilderException {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
			Connection connection = connectionFactory.createConnection();
			connection.setClientID("Datalake-Builder");
			connection.start();
			for (String topic : topics) {
				Session session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				Topic destination = session.createTopic(topic);
				MessageConsumer consumer = session.createDurableSubscriber(destination, topic);
				consumer.setMessageListener(message -> {
					try {
						dataStore.storeData(((TextMessage) message).getText(), topic);
						System.out.println();
					} catch (Exception e) {
						System.err.println(e.getMessage());
					}
				});
			}
		} catch (Exception e) {
			throw new DatalakeBuilderException(e.getMessage());
		}
	}
}
