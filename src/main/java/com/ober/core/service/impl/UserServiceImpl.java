package com.ober.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ober.core.dao.BaseMapper;
import com.ober.core.dao.UserMapper;
import com.ober.core.entity.User;
import com.ober.core.service.UserService;
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	private UserMapper userMapper;

	@Override
	@Resource
	public void setBaseMapper(BaseMapper<User> baseMapper) {
		this.userMapper = userMapper;
		super.setBaseMapper(baseMapper);
	}

	@Override
	public User getByAccount(String account) {
		// TODO Auto-generated method stub
		return userMapper.selectByAccount(account);
	}

}
