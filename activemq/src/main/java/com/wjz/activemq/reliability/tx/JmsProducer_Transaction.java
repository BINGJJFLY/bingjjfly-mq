package com.wjz.activemq.reliability.tx;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsProducer_Transaction {

	public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.21.131:61616";
	public static final String QUEUE_NAME = "queue_transaction";
	
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection conn = factory.createConnection();
		conn.start();
		// 开始事务设置为true
		Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageProducer producer = session.createProducer(queue);
		try {
			for (int i = 0; i < 3; i++) {
				TextMessage message = session.createTextMessage("queue_message_transaction_" + i);
				producer.send(message);
			}
			// 事务提交
			session.commit();
			System.out.println("Success.");
		} catch (Exception e) {
			// 事务回滚
			session.rollback();
		} finally {
			producer.close();
			session.close();
			conn.close();
		}
	}

}
