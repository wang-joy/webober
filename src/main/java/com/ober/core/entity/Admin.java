package com.ober.core.entity;

import java.io.Serializable;

public class Admin implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//主键
	private Integer autoid;
	//用户名
	private String account;
	//密码
	private String password;
	//管理员类型：（super）超级管理员、一般管理员（admin），超级管理员只有一个：
	private String type;
	public Integer getAutoid() {
		return autoid;
	}
	public void setAutoid(Integer autoid) {
		this.autoid = autoid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
