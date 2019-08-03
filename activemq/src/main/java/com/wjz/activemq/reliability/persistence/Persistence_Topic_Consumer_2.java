package com.wjz.activemq.reliability.persistence;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Persistence_Topic_Consumer_2 {
	
	public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.21.131:61616";
	public static final String TOPIC_NAME = "topic_persistence";
	public static final String CLIENT_ID = "topic_persistence_clientId_2";

	public static void main(String[] args) throws JMSException, IOException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection connection = factory.createConnection();
		connection.setClientID(CLIENT_ID);
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(TOPIC_NAME);
		TopicSubscriber subscriber = session.createDurableSubscriber(topic, "subscriber_2");
		subscriber.setMessageListener((message) -> {
			if (message != null && message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				try {
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		
		System.in.read();
		subscriber.close();
		session.close();
		connection.close();
	}
}
