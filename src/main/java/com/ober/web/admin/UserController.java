package com.ober.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ober.core.service.UserService;

@Controller("adminUserController")
@RequestMapping("admin/user/")
public class UserController {

	@Autowired
	private UserService userService;
}
