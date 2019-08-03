package com.wjz.activemq.message;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageHeaderTest {
	
	public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.21.131:61616";
	public static final String QUEUE_1 = "queue_header_1";
	public static final String QUEUE_2 = "queue_header_2";
	
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection conn = factory.createConnection();
		conn.start();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue_1 = session.createQueue(QUEUE_1);
		Queue queue_2 = session.createQueue(QUEUE_2);
		MessageProducer producer = session.createProducer(queue_1);
		TextMessage message = session.createTextMessage("queue_message");
		
		// 以下信息设置均无效
		message.setJMSDestination(queue_2);
		message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
		message.setJMSExpiration(System.currentTimeMillis() + 1000L);
		message.setJMSMessageID("1001");
		message.setJMSPriority(9);
		System.out.println("发送前：" + message.getJMSDestination());
		System.out.println("发送前：" + message.getJMSDeliveryMode());
		System.out.println("发送前：" + message.getJMSExpiration());
		System.out.println("发送前：" + message.getJMSMessageID());
		System.out.println("发送前：" + message.getJMSPriority());
		producer.send(message);
		System.out.println("发送后：" + message.getJMSDestination());
		System.out.println("发送后：" + message.getJMSDeliveryMode());
		System.out.println("发送后：" + message.getJMSExpiration());
		System.out.println("发送后：" + message.getJMSMessageID());
		System.out.println("发送后：" + message.getJMSPriority());
		
		message.setJMSCorrelationID("1001");
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		producer.setTimeToLive(5000L);
		producer.setPriority(9);
		producer.send(message);
		System.out.println("######################################");
		System.out.println("发送后：" + message.getJMSDestination());
		System.out.println("发送后：" + message.getJMSDeliveryMode());
		System.out.println("发送后：" + message.getJMSExpiration());
		System.out.println("发送后：" + message.getJMSCorrelationID());
		System.out.println("发送后：" + message.getJMSPriority());
		
		producer.close();
		session.close();
		conn.close();
		System.out.println("Success.");
	}

}
