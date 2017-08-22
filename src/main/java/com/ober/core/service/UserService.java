package com.ober.core.service;

import com.ober.core.entity.User;

public interface UserService extends BaseService<User>{

	User getByAccount(String account);

}
