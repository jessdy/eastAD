package com.shzejing.spring.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shzejing.spring.entities.EventJoinPerson;

public interface EventJoinPersonDao extends JpaRepository<EventJoinPerson, String> {

	List<EventJoinPerson> findByEventIdAndPerson(String id, String id2);

	List<EventJoinPerson> findByEventId(String id);

	@Query(value = "select sum(score) from ws_event_join_person where person = :person", nativeQuery = true)
	Number sumByPerson(@Param("person") String personId);

	List<EventJoinPerson> findByEventIdAndStatus(String id, int i);

	@Query(value = "select count(*) from ws_event_join_person where eventId = :eventId and status = 1", nativeQuery = true)
	Number countByEvent(@Param("eventId") String eventId);

	@Query(value = "delete from ws_event_join_person where person = :person", nativeQuery = true)
	@Modifying
	void deleteByPerson(@Param("person") String id);

	List<EventJoinPerson> findByEventIdAndPersonAndEventDate(String id, String person, String eventDate);
}
