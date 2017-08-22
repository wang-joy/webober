package com.ober.web.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ober.core.entity.User;
import com.ober.core.service.UserService;
import com.ober.utils.Md5Util;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map<String, Object> login(HttpServletRequest request, String account, String password) {
		Map<String, Object> map = new HashMap<>();
		User loginUser = userService.getByAccount(account);
		if (loginUser == null) {
			map.put("error", "用户不存在!");
		} else {
			String pwd = loginUser.getPassword();
			String md5Pwd = Md5Util.MD5(password).toUpperCase();
			if (!pwd.equals(md5Pwd)) {
				map.put("error", "密码错误！");
			} else {
				map.put("error", "false");
				String type = loginUser.getType();
				if ("admin".equals(type)) {
					map.put("url", "admin/index");
				} else if ("teacher".equals(type)) {
					map.put("url", "teacher/index");
				} else {
					map.put("url", "student/index");
				}
				HttpSession session = request.getSession(false);
				session.setAttribute("loginUser", loginUser);
			}
		}
		return map;
	}

}
