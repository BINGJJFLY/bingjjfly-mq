package com.wjz.activemq.demo;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 点对点，一个消息只能被一个消费者消费
 * 
 * 先生产，启动1号消费者再启动2号消费者，1号消费完消息，2号无感知
 * 先消费，启动1号消费者再启动2号消费者，启动生产者，消费者一人消费一半消息
 *
 * @author iss002
 *
 */
public class JmsProducer_Queue {
	
	public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.21.131:61616";
	public static final String QUEUE_NAME = "queue";
	
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection conn = factory.createConnection();
		conn.start();
		// 自动签收
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageProducer producer = session.createProducer(queue);
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
