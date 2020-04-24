package com.example.demo.service;

import java.lang.reflect.Method;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Saga;
import com.example.demo.repository.SagaRepository;

import ch.qos.logback.classic.Logger;

@Service
public class SagaServiceImpl implements SagaService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(SagaServiceImpl.class);
	
	private SagaRepository sagaRepository;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public SagaServiceImpl(SagaRepository sagaRepository) {
		this.sagaRepository = sagaRepository;
	}

	public void save(Saga saga) {
		sagaRepository.save(saga);
	}
	
	@Override
	public void apply(Long sagaId, Object event) throws Exception {
		// TODO Auto-generated method stub
		Optional<Saga> sagaOpt = sagaRepository.findById(sagaId);
		if (sagaOpt.isPresent()) {
			Saga saga = sagaOpt.get();
			Object serviceHandler = applicationContext.getBean(saga.getHandler());
			Method on = serviceHandler.getClass().getMethod("on", event.getClass());
			on.invoke(serviceHandler, event);
		} else {
			logger.error("Cannot find Saga with Id: " + sagaId);
		}
	}

	@Override
	public Saga findById(Long sagaId) {
		// TODO Auto-generated method stub
		
		return sagaRepository.findById(sagaId).get();
	}
}
