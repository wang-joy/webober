package com.ober.core.service.impl;

import javax.annotation.Resource;

import com.ober.core.dao.BaseMapper;
import com.ober.core.service.BaseService;

public class BaseServiceImpl<T> implements BaseService<T> {

	private BaseMapper<T> baseMapper;

	@Resource
	public void setBaseMapper(BaseMapper<T> baseMapper) {
		this.baseMapper = baseMapper;
	}

	@Override
	public int delByAutoid(Long autoid) {
		// TODO Auto-generated method stub
		return baseMapper.deleteByPrimaryKey(autoid);
	}

	@Override
	public int add(T record) {
		// TODO Auto-generated method stub
		return baseMapper.insert(record);
	}

	@Override
	public int addSelective(T record) {
		// TODO Auto-generated method stub
		return baseMapper.insertSelective(record);
	}

	@Override
	public T getByAutoid(Long autoid) {
		// TODO Auto-generated method stub
		return baseMapper.selectByPrimaryKey(autoid);
	}

	@Override
	public int updateByAutoidSelective(T record) {
		// TODO Auto-generated method stub
		return baseMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByAutoidKey(T record) {
		// TODO Auto-generated method stub
		return baseMapper.updateByPrimaryKey(record);
	}

}
