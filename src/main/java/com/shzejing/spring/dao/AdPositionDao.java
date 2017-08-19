package com.shzejing.spring.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shzejing.spring.entities.AdPosition;

public interface AdPositionDao extends JpaRepository<AdPosition, String> {

	List<AdPosition> findByNewsstandAndStatus(String newsstand, int statusEnable);

	List<AdPosition> findByNewsstandAndPositionAndStatus(String id, int position, int statusEnable);

	List<AdPosition> findByCustomerAndStatus(String id, int statusEnable);

	Page<AdPosition> findByCustomer(String id, Pageable pageable);

	List<AdPosition> findByNewsstandAndPositionAndStatusAndIdNot(String newsstand, int position, int statusEnable,
			String id);

}
