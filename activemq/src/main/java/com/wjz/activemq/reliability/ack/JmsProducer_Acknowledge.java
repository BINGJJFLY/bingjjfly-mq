package com.wjz.activemq.reliability.ack;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 签收偏向消费者（消费者未开启事务演示）
 * 
 * 消费者未开启事务，签收模式为手动的话，消息应告知已签收
 * 消费者开启事务，提交事务，消息全部自动签收
 * 
 * @author iss002
 *
 */
public class JmsProducer_Acknowledge {
	
	public static final String DEFAULT_BROKER_BIND_URL = "tcp://127.0.0.1:61616";
	public static final String QUEUE_NAME = "queue_ack";
	
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection conn = factory.createConnection();
		conn.start();
		// 不开启事务，自动签收（涉及可靠性）
		Session session = conn.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageProducer producer = session.createProducer(queue);
		for (int i = 0; i < 3; i++) {
			TextMessage message = session.createTextMessage("queue_ack_message_" + i);
			producer.send(message);
//			message.acknowledge();
		}
		producer.close();
		session.close();
		conn.close();
		System.out.println("Success.");
	}

}
