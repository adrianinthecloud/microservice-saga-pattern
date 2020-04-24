package com.example.demo.service;

import java.util.Map;

import com.example.demo.model.entity.Saga;

public interface SagaService {
	void apply(Long sagaId, Object event) throws Exception;
	Saga findById(Long sagaId);
}
