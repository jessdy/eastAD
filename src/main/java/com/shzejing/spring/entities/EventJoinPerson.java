package com.shzejing.spring.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "ws_event_join_person")
public class EventJoinPerson implements Serializable {
	
	public static final int UNCONFIRM = 0;
	public static final int CONFIRMED = 1;

	@Id
	private String id;
	
	private String eventId;
	
	private String person;
	
	private int status;
	
	private int score;
	
	private String eventDate;
	
	@Transient
	private Volunteer personObj;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
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

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Volunteer getPersonObj() {
		return personObj;
	}

	public void setPersonObj(Volunteer personObj) {
		this.personObj = personObj;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
}
