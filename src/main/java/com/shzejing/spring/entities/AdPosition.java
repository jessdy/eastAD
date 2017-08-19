package com.shzejing.spring.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "ws_adposition")
public class AdPosition implements Serializable {
	
	public static final int STATUS_ENABLE = 1;
	public static final int STATUS_DISABLE = 0;

	@Id
	private String id;
	
	private String newsstand;
	
	private int position;
	
	private int number;
	
	private String customer;
	
	private String adtitle;
	
	private String adbegin;
	
	private String adend;
	
	private String picture;
	
	private String district;
	
	private int status;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAdtitle() {
		return adtitle;
	}

	public void setAdtitle(String adtitle) {
		this.adtitle = adtitle;
	}

	public String getAdbegin() {
		return adbegin;
	}

	public void setAdbegin(String adbegin) {
		this.adbegin = adbegin;
	}

	public String getAdend() {
		return adend;
	}

	public void setAdend(String adend) {
		this.adend = adend;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
}
