package com.ober.core.dao;

import com.ober.core.entity.Branch;

public interface BaseMapper<T> {

	int deleteByPrimaryKey(Long autoid);

	int insert(T record);

	int insertSelective(T record);

	T selectByPrimaryKey(Long autoid);

	int updateByPrimaryKeySelective(T record);

	int updateByPrimaryKey(T record);
}
