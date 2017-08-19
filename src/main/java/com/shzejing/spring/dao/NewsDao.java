package com.shzejing.spring.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shzejing.spring.entities.News;

public interface NewsDao extends JpaRepository<News, String> {

	Page<News> findByNameLike(String string, Pageable pageable);

	Page<News> findByStatus(int publish, Pageable pageable);

	List<News> findByStatus(int publish);

	List<News> findByStatusOrderByWeightDesc(int publish);

}
