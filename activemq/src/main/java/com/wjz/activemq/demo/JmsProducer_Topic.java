package com.wjz.activemq.demo;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 一对多，一个消息被多个消费者消费
 * 先启动消费者，再启动生产者生产消息，生产者生产的消息都被消费者消费，人人都有
 * 先启动生产者生产消息，再启动消费者，未收到的消息作废
 * 
 * @author iss002
 *
 */
public class JmsProducer_Topic {
	
	public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.21.131:61616";
	public static final String TOPIC_NAME = "topic";
	
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection conn = factory.createConnection();
		conn.start();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(TOPIC_NAME);
		MessageProducer producer = session.createProducer(topic);
		for (int i = 0; i < 3; i++) {
			TextMessage message = session.createTextMessage("topic_message_" + i);
			producer.send(message);
		}
		producer.close();
		session.close();
		conn.close();
		System.out.println("Success.");
	}

}
