package com.wjz.activemq.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service("springProducerQueue")
public class Spring_Producer_Queue {
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-jms.xml");
		Spring_Producer_Queue producer = (Spring_Producer_Queue) context.getBean("springProducerQueue");
		producer.jmsTemplate.send((session) -> {
			return session.createTextMessage("Spring整合ActiveMQ");
		});
		System.out.println("Success.");
	}

}
