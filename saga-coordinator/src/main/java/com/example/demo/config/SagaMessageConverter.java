package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.stereotype.Component;

import com.example.demo.utils.MessageEventMapper;

import ch.qos.logback.classic.Logger;

@Component
public class SagaMessageConverter extends SimpleMessageConverter {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(SagaMessageConverter.class);
	
	@Autowired
	private MessageEventMapper eventMapper;
	
	@Override
	public Object fromMessage(Message message) throws JMSException {
		if (message instanceof ActiveMQMapMessage) {
			logger.info("message instance of ActiveMQMapMessage: " + message);
			
			ActiveMQMapMessage aMsg = ((ActiveMQMapMessage) message);
			
			String destination = aMsg.getDestination().getPhysicalName();
			String type = aMsg.getStringProperty("_type");
			Map<String, Object> map = ((ActiveMQMapMessage) message).getContentMap();
			
			
			Object obj = eventMapper.getEventFromMsg(destination, type, map);
			logger.info("obj = " + obj);
			return obj;
		} else if (message instanceof ActiveMQTextMessage) {
			logger.info("message instance of ActiveMQTextMessage: " + message);
			// only reject event will send text message, the text is sagaId
			ActiveMQTextMessage textMsg = (ActiveMQTextMessage) message;
			String destination = textMsg.getDestination().getPhysicalName();
			String type = textMsg.getStringProperty("_type");
			String sagaId = textMsg.getText();
			
			Map<String, Object> map = new HashMap();
			map.put("sagaId", sagaId);
			
			Object obj = eventMapper.getEventFromMsg(destination, type, map);
			logger.info("obj = " + obj);
			return obj;
		} else {
			logger.info("message instance of other types: " + message);
			return super.fromMessage(message);
		}
	}
}
