package com.shzejing.spring.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@SuppressWarnings("serial")
@Entity
@Table(name = "ws_adphoto")
public class AdPhoto implements Serializable {
	
	@Id
	private String id;
	
	private String newsstand;
	
	private String photo1;
	
	private String photo2;
	
	private String photo3;
	
	private String photo4;
	
	private String photo5;
	
	private String photo6;
	
	private Date updatetime;
	
	@Formula("(select s.code from ws_newsstand s where s.id = newsstand)")
	private String code;
	
	@Formula("(select s.name from ws_newsstand s where s.id = newsstand)")
	private String standname;
	
	@Formula("(select s.area from ws_newsstand s where s.id = newsstand)")
	private String standarea;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNewsstand() {
		return newsstand;
	}

	public void setNewsstand(String newsstand) {
		this.newsstand = newsstand;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStandname() {
		return standname;
	}

	public void setStandname(String standname) {
		this.standname = standname;
	}

	public String getStandarea() {
		return standarea;
	}

	public void setStandarea(String standarea) {
		this.standarea = standarea;
	}

	public String getPhoto1() {
		return photo1;
	}

	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}

	public String getPhoto2() {
		return photo2;
	}

	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}

	public String getPhoto3() {
		return photo3;
	}

	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}

	public String getPhoto4() {
		return photo4;
	}

	public void setPhoto4(String photo4) {
		this.photo4 = photo4;
	}

	public String getPhoto5() {
		return photo5;
	}

	public void setPhoto5(String photo5) {
		this.photo5 = photo5;
	}

	public String getPhoto6() {
		return photo6;
	}

	public void setPhoto6(String photo6) {
		this.photo6 = photo6;
	}
	
	
	
}
