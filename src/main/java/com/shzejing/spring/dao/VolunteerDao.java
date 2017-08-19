package com.shzejing.spring.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shzejing.spring.entities.Volunteer;

public interface VolunteerDao extends JpaRepository<Volunteer, String> {

	List<Volunteer> findByOpenid(String openid);

	List<Volunteer> findByCompany(String id);

	List<Volunteer> findByType(String typeCompany);

	Page<Volunteer> findByType(String typePerson, Pageable pageable);

}
