package com.shzejing.spring.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "ws_newsstand")
public class Newsstand implements Serializable {
	
	public static final int STATUS_NORMAL = 1;
	public static final int STATUS_DELETED = 0;

	@Id
	private String id;

	private String name;

	private String area;

	private String position;

	private String code;

	@Transient
	private boolean isParent = false;

	private String master;

	private String mobile;

	private String encode;
	
	private Integer status;
	
	private Integer leftad;
	
	private Integer centerad;
	
	private Integer rightad;
	
	private Integer sidenumber;
	
	private Integer backnumber;

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getLeftad() {
		return leftad;
	}

	public void setLeftad(Integer leftad) {
		this.leftad = leftad;
	}

	public Integer getCenterad() {
		return centerad;
	}

	public void setCenterad(Integer centerad) {
		this.centerad = centerad;
	}

	public Integer getRightad() {
		return rightad;
	}

	public void setRightad(Integer rightad) {
		this.rightad = rightad;
	}

	public Integer getSidenumber() {
		return sidenumber;
	}

	public void setSidenumber(Integer sidenumber) {
		this.sidenumber = sidenumber;
	}

	public Integer getBacknumber() {
		return backnumber;
	}

	public void setBacknumber(Integer backnumber) {
		this.backnumber = backnumber;
	}

}
