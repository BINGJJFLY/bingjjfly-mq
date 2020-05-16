package com.wjz.activemq.unittest;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Rule;
import org.junit.Test;

/**
 * EmbeddedActiveMQBroker默认非持久，唯一可用的传输协议是VM，默认使用JMX
 * 
 * @author iss002
 *
 */
public class EmbeddedActiveMQBrokerJunit {

	@Rule
	public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker() {
		/**
		 * 嵌入式Broker启动（Broker调用start方法）之前自定义一些配置
		 */
		protected void configure() {
			getBrokerService().setUseJmx(false);
		};
	};

	public static final String DEFAULT_BROKER_BIND_URL = "vm://localhost?broker.persistent=false";
	public static final String QUEUE_NAME = "junit_broker";

	ConnectionFactory factory;

	@Test
	public void constructor() {
		factory = new ActiveMQConnectionFactory("vm://embedded-broker?create=false");
		junit();
	}

	@Test
	public void create() {
		factory = broker.createConnectionFactory();
		junit();
	}

	public void junit() {
		try {
			Connection conn = factory.createConnection();
			conn.start();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(QUEUE_NAME);
			MessageProducer producer = session.createProducer(queue);
			for (int i = 0; i < 3; i++) {
				TextMessage message = session.createTextMessage("junit_broker_message_" + i);
				System.out.println("Producer：" + message.getText());
				producer.send(message);
			}
			producer.close();

			MessageConsumer consumer = session.createConsumer(queue);
			while (true) {
				TextMessage message = (TextMessage) consumer.receive(3000L);
				if (message != null) {
					System.out.println("Consumer" + message.getText());
				} else {
					break;
				}
			}
			consumer.close();

			session.close();
			conn.close();
			System.out.println("Success.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
