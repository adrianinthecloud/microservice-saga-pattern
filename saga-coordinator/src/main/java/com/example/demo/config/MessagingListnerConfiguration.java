package com.example.demo.config;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

@Configuration
@EnableJms
public class MessagingListnerConfiguration {
 
    @Autowired
    ConnectionFactory connectionFactory;
     
//    @Bean
//	public MessageConverter messageConverter(){
//		PersonMessageConverter personalConverter = new PersonMessageConverter();
//		return personalConverter;
//	}
	
//    @Bean
//    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setConcurrency("1-1");
//        factory.setMessageConverter(messageConverter());
//        return factory;
//    }
    
//    @Bean
//    public JmsTemplate getJmsTemplate() {
//		JmsTemplate jmsTemplate = new JmsTemplate();
//		jmsTemplate.setConnectionFactory(connectionFactory);
////		jmsTemplate.setMessageConverter(messageConverter());
//    	
//    	return jmsTemplate;
//    	
//    }
 
}