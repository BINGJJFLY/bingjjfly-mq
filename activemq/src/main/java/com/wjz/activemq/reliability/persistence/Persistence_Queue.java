package com.wjz.activemq.reliability.persistence;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 可靠性-持久化
 * 
 * 生产者默认使用持久化策略
 * 
 * @author iss002
 *
 */
public class Persistence_Queue {

	public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.21.131:61616";
	public static final String QUEUE_NAME = "queue";
	
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection conn = factory.createConnection();
		conn.start();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageProducer producer = session.createProducer(queue);
		
		// 生产者设置是否持久化
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		for (int i = 0; i < 6; i++) {
			TextMessage message = session.createTextMessage("queue_message_" + i);
			producer.send(message);
		}
		producer.close();
		session.close();
		conn.close();
		System.out.println("Success.");
	}
}
