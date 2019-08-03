package com.wjz.activemq.message;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageBodyTest {

	public static final String DEFAULT_BROKER_BIND_URL = "tcp://192.168.21.131:61616";
	public static final String QUEUE_NAME = "queue_body";
	
	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
		Connection conn = factory.createConnection();
		conn.start();
		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageProducer producer = session.createProducer(queue);
		
		TextMessage textMessage = session.createTextMessage();
		textMessage.setText("queue_message");
		producer.send(textMessage);
		
		MapMessage mapMessage = session.createMapMessage();
		mapMessage.setString("key", "value");
		mapMessage.setBoolean("flag", true);
		ArrayList<String> list = new ArrayList<String>();
		list.add("hello world");
		mapMessage.setObject("obj", list);
		producer.send(mapMessage);
		
		StreamMessage streamMessage = session.createStreamMessage();
		streamMessage.writeString("stream");
		producer.send(streamMessage);
		
		ObjectMessage objectMessage = session.createObjectMessage();
		objectMessage.setObject(new Person());
		producer.send(objectMessage);
		
		BytesMessage bytesMessage = session.createBytesMessage();
		bytesMessage.writeBytes("byte".getBytes());
		producer.send(bytesMessage);
		
		producer.close();
		session.close();
		conn.close();
		System.out.println("Success.");
	}
	
	static class Person implements Serializable {
		private static final long serialVersionUID = 9079245381539003696L;
		@Override
		public String toString() {
			return "Person [id=7, name=bingjjfly]";
		}
	}
}
