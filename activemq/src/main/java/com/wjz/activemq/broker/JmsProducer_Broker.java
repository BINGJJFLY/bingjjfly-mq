package com.wjz.activemq.broker;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsProducer_Broker {
	
	public static final String DEFAULT_BROKER_BIND_URL = "tcp://127.0.0.1:61616";
	public static final String QUEUE_NAME = "queue_broker";
	
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection conn = factory.createConnection();
		conn.start();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageProducer producer = session.createProducer(queue);
		for (int i = 0; i < 3; i++) {
			TextMessage message = session.createTextMessage("queue_broker_message_" + i);
			System.out.println(message.getJMSCorrelationID());
			producer.send(message);
		}
		producer.close();
		session.close();
		conn.close();
		System.out.println("Success.");
	}

}
