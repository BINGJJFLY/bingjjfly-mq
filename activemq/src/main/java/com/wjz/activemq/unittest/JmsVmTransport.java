package com.wjz.activemq.unittest;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * vm://localhost?broker.persistent=false：创建JMS ConnectionFactory，还将自动创建嵌入式Broker
 * 
 * @author iss002
 *
 */
public class JmsVmTransport {
	
	public static final String DEFAULT_BROKER_BIND_URL = "vm://localhost?broker.persistent=false";
	public static final String QUEUE_NAME = "vm_broker";

	public static void main(String[] args) throws JMSException {
		ConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection conn = factory.createConnection();
		conn.start();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageProducer producer = session.createProducer(queue);
		for (int i = 0; i < 3; i++) {
			TextMessage message = session.createTextMessage("vm_broker_message_" + i);
			System.out.println("Producer：" + message.getText());
			producer.send(message);
		}
		producer.close();
		
		MessageConsumer consumer = session.createConsumer(queue);
		while (true) {
			TextMessage message = (TextMessage) consumer.receive(3000L);
			if (message != null) {
				System.out.println("Consumer" + message.getText());
			} else {
				break;
			}
		}
		consumer.close();
		
		session.close();
		conn.close();
		System.out.println("Success.");
	}
}
