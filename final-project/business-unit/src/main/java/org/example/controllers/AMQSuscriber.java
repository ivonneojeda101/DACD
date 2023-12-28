package org.example.controllers;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.example.exceptions.BussinessUnitException;

import javax.jms.*;
import java.util.List;

public class AMQSuscriber implements DataSource {
	private final String url;
	private final List<String> topics;

	public AMQSuscriber(String url, List<String> topics) {
		this.url = url;
		this.topics = topics;
	}

	@Override
	public void getData(DataManagement dataManagement) throws BussinessUnitException {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
			Connection connection = connectionFactory.createConnection();
			connection.setClientID("Business-Unit");
			connection.start();
			for (String topic : topics) {
				Session session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				Topic destination = session.createTopic(topic);
				MessageConsumer consumer = session.createDurableSubscriber(destination, topic);
				consumer.setMessageListener(message -> {
					try {
						dataManagement.storeData(((TextMessage) message).getText());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
			}
		}
		catch (Exception e) {
			throw new BussinessUnitException(e.getMessage());
		}
	}
}
