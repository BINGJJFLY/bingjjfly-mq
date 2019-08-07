package com.wjz.activemq.redelivery;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;

public class JmsConsumer_RedeliveryPolicy {
	
	public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.21.131:61616";
	public static final String QUEUE_NAME = "queue_transaction";
	
	public static void main(String[] args) throws JMSException, IOException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
		// 第一次失败之后再重发三次
		redeliveryPolicy.setMaximumRedeliveries(3);
		factory.setRedeliveryPolicy(redeliveryPolicy);
		Connection conn = factory.createConnection();
		conn.start();
		Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageConsumer consumer = session.createConsumer(queue);
		try {
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
			
			System.in.read();
			// 事务不提交
//			session.commit();
		} catch (Exception e) {
			session.rollback();
		} finally {
			consumer.close();
			session.close();
			conn.close();
		}
	}

}
