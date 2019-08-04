package com.wjz.activemq.reliability.ack;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsConsumer_Acknowledge {
	
	public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.21.131:61616";
	public static final String QUEUE_NAME = "queue_ack";

	public static void main(String[] args) throws JMSException, IOException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection conn = factory.createConnection();
		conn.start();
		// 自动签收调整为手动签收
		Session session = conn.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new MessageListener() {
			public void onMessage(Message message) {
				if (message != null && message instanceof TextMessage) {
					try {
						System.out.println(((TextMessage) message).getText());
						// 消息告知已手动签收
						message.acknowledge();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		});
		System.in.read();
		consumer.close();
		session.close();
		conn.close();
	}

}
