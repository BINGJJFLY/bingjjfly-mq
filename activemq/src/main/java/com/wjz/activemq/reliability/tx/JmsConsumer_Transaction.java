package com.wjz.activemq.reliability.tx;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsConsumer_Transaction {

	public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.21.131:61616";
	public static final String QUEUE_NAME = "queue_transaction";
	
	public static void main(String[] args) throws JMSException, IOException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection conn = factory.createConnection();
		conn.start();
		// 开始事务设置为true
		Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageConsumer consumer = session.createConsumer(queue);
		try {
			/* 同步方式消费消息
			while (true) {
				TextMessage message = (TextMessage) consumer.receive(3000L);
				if (message != null) {
					System.out.println(message.getText());
				} else {
					break;
				}
			}
			*/
			
			// 异步方式监听消费消息
			consumer.setMessageListener((message) -> {
				if (message != null && message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					try {
						System.out.println(textMessage.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
				System.out.println("子线程名称：" + Thread.currentThread().getName());
			});
			System.out.println("主线程名称：" + Thread.currentThread().getName());
			
			// 等待消费掉所有消息
			System.in.read();
			// 事务提交
			session.commit();
		} catch (Exception e) {
			// 事务回滚
			session.rollback();
		} finally {
			consumer.close();
			session.close();
			conn.close();
		}
	}
}
