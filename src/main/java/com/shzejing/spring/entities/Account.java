package com.shzejing.spring.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "ws_account")
public class Account implements Serializable {
	
	public static final String USERTYPE_ADMIN = "admin";
	public static final String USERTYPE_ADMIN2 = "admin2";
	public static final String USERTYPE_MANAGER = "manager";
	public static final String USERTYPE_SUPERVISOR = "visor";
	public static final int ENABLE = 1;
	public static final int DISABLE = 0;

	@Id
	private String id;
	
	private String username;
	
	private String userpass;
	
	private String openid;
	
	private String usertype;
	
	private String usertype2;
	
	private int userstatus;
	
	private String newsstand;
	
	private String company;
	
	public String getNewsstand() {
		return newsstand;
	}

	public void setNewsstand(String newsstand) {
		this.newsstand = newsstand;
	}

	public int getUserstatus() {
		return userstatus;
	}

	public void setUserstatus(int userstatus) {
		this.userstatus = userstatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpass() {
		return userpass;
	}

	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getUsertype2() {
		return usertype2;
	}

	public void setUsertype2(String usertype2) {
		this.usertype2 = usertype2;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
}
