package com.wjz.activemq.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service("springConsumerQueue")
public class Spring_Consumer_Queue {
	
	@Autowired
	private JmsTemplate jmsTemplate;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-jms.xml");
		Spring_Consumer_Queue consumer = (Spring_Consumer_Queue) context.getBean("springConsumerQueue");
		String result = (String) consumer.jmsTemplate.receiveAndConvert();
		System.out.println("接收到的消息：" + result);
	}
}
