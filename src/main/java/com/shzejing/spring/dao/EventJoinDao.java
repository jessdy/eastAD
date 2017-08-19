package com.shzejing.spring.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shzejing.spring.entities.EventJoin;

public interface EventJoinDao extends JpaRepository<EventJoin, String> {

	List<EventJoin> findByEventId(String id);

	List<EventJoin> findByEventIdAndVolunteer(String id, String id2);

	List<EventJoin> findByEventIdAndVolunteerAndStatus(String id, String company, int confirmed);

	List<EventJoin> findByEventIdAndStatus(String id, int confirmed);

	List<EventJoin> findByVolunteer(String id);
	
}
