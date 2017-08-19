package com.shzejing.spring.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shzejing.spring.entities.Event;

public interface EventDao extends JpaRepository<Event, String> {

	Page<Event> findByNameLike(String string, Pageable pageable);

	Page<Event> findByStatus(int publish, Pageable pageable);

	Page<Event> findByCreatorAndNameLike(String id, String string, Pageable pageable);

	List<Event> findByCreator(String id);

	@Query(value = "select distinct e.* from ws_event e inner join ws_event_join j on e.id = j.eventId where  volunteer = :user", nativeQuery = true)
	List<Event> findByCreatorAndJoin(@Param("user") String userid);
}
