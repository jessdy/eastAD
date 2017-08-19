package com.shzejing.spring.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ws_voluteer_company")
public class VolunteerCompany {
	
	@Id
	private String id;
	
	private String name;
	
	private String code;
	
	private String telephone;
	
	private String email;
	
	private String website;
	
	private String faren;
	
	private String contactor;
	
	private String contactjob;
	
	private String contactphone;
	
	private String contactid;
	
	private String teamname;
	
	private String pic1;
	
	private String pic2;
	
	private int status;
	
	private Date regtime;
	
	@Transient
	private String wxuser;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFaren() {
		return faren;
	}

	public void setFaren(String faren) {
		this.faren = faren;
	}

	public String getContactor() {
		return contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public String getContactjob() {
		return contactjob;
	}

	public void setContactjob(String contactjob) {
		this.contactjob = contactjob;
	}

	public String getContactphone() {
		return contactphone;
	}

	public void setContactphone(String contactphone) {
		this.contactphone = contactphone;
	}

	public String getContactid() {
		return contactid;
	}

	public void setContactid(String contactid) {
		this.contactid = contactid;
	}

	public String getTeamname() {
		return teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	public String getPic1() {
		return pic1;
	}

	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}

	public String getPic2() {
		return pic2;
	}

	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Date getRegtime() {
		return regtime;
	}

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getWxuser() {
		return wxuser;
	}

	public void setWxuser(String wxuser) {
		this.wxuser = wxuser;
	}

}
