package com.wjz.activemq.async;

import java.util.UUID;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.AsyncCallback;

public class JmsProducer_UseAsyncSend {
	
	public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.21.131:61616";
	public static final String QUEUE_NAME = "queue";
	
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		// 异步发送消息
		factory.setUseAsyncSend(true);
		Connection conn = factory.createConnection();
		conn.start();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		ActiveMQMessageProducer producer = (ActiveMQMessageProducer) session.createProducer(queue);
		for (int i = 0; i < 3; i++) {
			TextMessage message = session.createTextMessage("queue_message_" + i);
			String messageId = UUID.randomUUID().toString();
			message.setJMSMessageID(messageId);
			producer.send(message, new AsyncCallback() {
				@Override
				public void onException(JMSException exception) {
					System.out.println(messageId + "is fail, need to reSend");
				}
				@Override
				public void onSuccess() {
					System.out.println(messageId + "is ok");
				}
			});
		}
		producer.close();
		session.close();
		conn.close();
		System.out.println("Success.");
	}

}
