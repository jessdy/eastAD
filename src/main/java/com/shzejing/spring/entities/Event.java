package com.shzejing.spring.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "ws_event")
public class Event implements Serializable {

	public static final int DRAFT = 0;
	public static final int PUBLISH = 1;
	public static final int END = 2;

	@Id
	private String id;

	private String name;
	
	private String pic;
	
	private String description;
	
	private Date uptime;
	
	private String content;
	
	private String creator;
	
	private long visittimes;
	
	private int status;
	
	@Column(name = "begin_date")
	private String beginDate;
	
	@Column(name = "end_date")
	private String endDate;
	
	private String eventDate;
	
	@Transient
	private List<String> companys = new ArrayList<String>();
	
	@Transient
	private int canjoin;

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

	public Date getUptime() {
		return uptime;
	}

	public void setUptime(Date uptime) {
		this.uptime = uptime;
	}

	public long getVisittimes() {
		return visittimes;
	}

	public void setVisittimes(long visittimes) {
		this.visittimes = visittimes;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getCompanys() {
		return companys;
	}

	public void setCompanys(List<String> companys) {
		this.companys = companys;
	}

	public int getCanjoin() {
		return canjoin;
	}

	public void setCanjoin(int canjoin) {
		this.canjoin = canjoin;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

}
