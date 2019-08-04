package com.wjz.activemq.spring;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service("springProducerTopic")
public class Spring_Producer_Topic {
	
	@Resource(name = "jmsTemplateTopic")
	private JmsTemplate jmsTemplate;
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-jms.xml");
		Spring_Producer_Topic producer = (Spring_Producer_Topic) context.getBean("springProducerTopic");
		producer.jmsTemplate.send((session) -> {
			return session.createTextMessage("Spring整合ActiveMQ");
		});
		System.out.println("Success.");
	}

}
