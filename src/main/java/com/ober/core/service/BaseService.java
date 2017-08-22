package com.ober.core.service;

public interface BaseService<T> {
	
	int delByAutoid(Long autoid);
	
	int add(T record);
	
	int addSelective(T record);
	
	T getByAutoid(Long autoid);
	
	int updateByAutoidSelective(T record);
	
	int updateByAutoidKey(T record);
	
}
