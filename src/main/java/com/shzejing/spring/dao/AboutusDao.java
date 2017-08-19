package com.shzejing.spring.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shzejing.spring.entities.Aboutus;
import com.shzejing.spring.entities.News;

public interface AboutusDao extends JpaRepository<Aboutus, String> {

}
