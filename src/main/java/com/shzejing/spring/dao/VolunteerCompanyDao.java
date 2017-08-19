package com.shzejing.spring.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shzejing.spring.entities.VolunteerCompany;

public interface VolunteerCompanyDao extends JpaRepository<VolunteerCompany, String> {

	List<VolunteerCompany> findByStatus(int i);

}
