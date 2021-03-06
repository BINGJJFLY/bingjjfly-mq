package com.wjz.activemq.broker;

import org.apache.activemq.broker.BrokerService;

/**
 * Broker相当于一个ActiveMQ服务实例
 * 
 * @author iss002
 *
 */
public class EmbedBroker {
	
	public static final String DEFAULT_BROKER_BIND_URL = "tcp://127.0.0.1:61616";
	
	public static void main(String[] args) throws Exception {
		BrokerService broker = new BrokerService();
		broker.setPersistent(false);
		broker.setUseJmx(false);
		broker.addConnector(DEFAULT_BROKER_BIND_URL);
		broker.start();
	}

}
