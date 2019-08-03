package com.wjz.activemq.message;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class MessagePropertyTest {

	public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.21.131:61616";
	public static final String QUEUE_NAME = "queue_property";
	
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection conn = factory.createConnection();
		conn.start();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageProducer producer = session.createProducer(queue);
		
		TextMessage textMessage = session.createTextMessage("queue_message");
		textMessage.setStringProperty("color", "block");
		textMessage.setBooleanProperty("flag", true);
		producer.send(textMessage);
		
		producer.close();
		session.close();
		conn.close();
		System.out.println("Success.");
	}
}
