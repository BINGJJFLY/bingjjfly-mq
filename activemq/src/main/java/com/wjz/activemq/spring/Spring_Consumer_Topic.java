package com.wjz.activemq.spring;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service("springConsumerTopic")
public class Spring_Consumer_Topic {
	
	@Resource(name = "jmsTemplateTopic")
	private JmsTemplate jmsTemplate;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-jms.xml");
		Spring_Consumer_Topic consumer = (Spring_Consumer_Topic) context.getBean("springConsumerTopic");
		String result = (String) consumer.jmsTemplate.receiveAndConvert();
		System.out.println("接收到的消息：" + result);
	}
}
