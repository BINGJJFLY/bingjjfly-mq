package com.wjz.activemq.reliability;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 先运行订阅者，再运行发布者，发送消息，无论订阅者是否还在线，订阅者都会收到
 * 
 * @author iss002
 *
 */
public class Persistence_Topic_Producer {
	
	public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.21.131:61616";
	public static final String TOPIC_NAME = "topic_persistence";
	
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection connection = factory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(TOPIC_NAME);
		MessageProducer producer = session.createProducer(topic);
		
		// 默认持久化
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		
		for (int i = 0; i < 3; i++) {
			TextMessage textMessage = session.createTextMessage("message_topic_persistence_" + i);
			producer.send(textMessage);
		}
		
		producer.close();
		session.close();
		connection.close();
		System.out.println("Success.");
	}

}
