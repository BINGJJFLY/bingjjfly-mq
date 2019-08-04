package com.wjz.activemq.spring;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

@Component("textMessageListener")
public class TextMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		if (message != null && message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			try {
				System.out.println("消息监听器接收到消息：" + textMessage.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
