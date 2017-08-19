package com.shzejing.spring.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "ws_event_join")
public class EventJoin implements Serializable {
	
	public static final int UNCONFIRM = 0;
	public static final int CONFIRMED = 1;
	public static final int REJECT = -1;

	@Id
	private String id;
	
	private String eventId;
	
	private String volunteer;
	
	private int status;
	
	private String eventDate;
	
	@Transient
	private Volunteer volunter;
	
	@Transient
	private VolunteerCompany volunteerCompany;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(String volunteer) {
		this.volunteer = volunteer;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getEventId() {
		return eventId;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Volunteer getVolunter() {
		return volunter;
	}

	public void setVolunter(Volunteer volunter) {
		this.volunter = volunter;
	}

	public VolunteerCompany getVolunteerCompany() {
		return volunteerCompany;
	}

	public void setVolunteerCompany(VolunteerCompany volunteerCompany) {
		this.volunteerCompany = volunteerCompany;
	}
}
