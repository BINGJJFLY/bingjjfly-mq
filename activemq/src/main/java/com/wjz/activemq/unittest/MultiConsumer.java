package com.wjz.activemq.unittest;

import java.util.concurrent.CyclicBarrier;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class MultiConsumer {

	public static final String DEFAULT_BROKER_BIND_URL = "tcp://127.0.0.1:61616";

	public static final String QUEUE_NAME = "multiConsumer_broker";

	@Test
	public void produce() {
		ConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		try {
			Connection conn = factory.createConnection();
			conn.start();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(QUEUE_NAME);
			MessageProducer producer = session.createProducer(queue);
			for (int i = 0; i < 10; i++) {
				TextMessage message = session.createTextMessage("multiConsumer_broker_message_" + i);
				System.out.println("Producer：" + message.getText());
				producer.send(message);
			}
			CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						cyclicBarrier.await();
					} catch (Exception e) {
						e.printStackTrace();
					}
					consume1();
				}
			}).start();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						cyclicBarrier.await();
					} catch (Exception e) {
						e.printStackTrace();
					}
					consume2();
				}
			}).start();
			System.in.read();
			producer.close();
			session.close();
			conn.close();
			System.out.println("Success.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void consume1() {
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
			Connection conn = factory.createConnection();
			conn.start();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(QUEUE_NAME);
			MessageConsumer consumer = session.createConsumer(queue);
			consumer.setMessageListener(new MessageListener() {
				public void onMessage(Message message) {
					if (message != null && message instanceof TextMessage) {
						try {
							System.out.println("Consumer-1：" + ((TextMessage) message).getText());
							message.acknowledge();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
			System.in.read();
			consumer.close();
			session.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void consume2() {
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
			Connection conn = factory.createConnection();
			conn.start();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(QUEUE_NAME);
			MessageConsumer consumer = session.createConsumer(queue);
			consumer.setMessageListener(new MessageListener() {
				public void onMessage(Message message) {
					if (message != null && message instanceof TextMessage) {
						try {
							System.out.println("Consumer-2：" + ((TextMessage) message).getText());
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
