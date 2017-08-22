package com.ober.core.dao;

import com.ober.core.entity.User;

public interface UserMapper extends BaseMapper<User>{

	User selectByAccount(String account);

}