package com.wjz.activemq.broker;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsConsumer_Broker {
	
	public static final String DEFAULT_BROKER_BIND_URL = "tcp://127.0.0.1:61616";
	public static final String QUEUE_NAME = "queue_broker";

	public static void main(String[] args) throws JMSException, IOException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection conn = factory.createConnection();
		conn.start();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageConsumer consumer = session.createConsumer(queue);
		while (true) {
			TextMessage message = (TextMessage) consumer.receive(3000L);
			if (message != null) {
				System.out.println(message.getText());
			} else {
				break;
			}
		}
		consumer.close();
		session.close();
		conn.close();
	}
}
