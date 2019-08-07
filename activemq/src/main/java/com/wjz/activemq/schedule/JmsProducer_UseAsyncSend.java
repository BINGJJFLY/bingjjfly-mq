package com.wjz.activemq.schedule;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

public class JmsProducer_UseAsyncSend {
	
	public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.21.131:61616";
	public static final String QUEUE_NAME = "queue";
	
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection conn = factory.createConnection();
		conn.start();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageProducer producer = session.createProducer(queue);
		long delay = 3 * 1000;
		long period = 4 * 1000;
		int repeat = 5;
		for (int i = 0; i < 3; i++) {
			TextMessage message = session.createTextMessage("queue_message_" + i);
			message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
			message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period);
			message.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat);
			producer.send(message);
		}
		producer.close();
		session.close();
		conn.close();
		System.out.println("Success.");
	}

}
