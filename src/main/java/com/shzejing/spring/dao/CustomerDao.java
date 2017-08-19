package com.shzejing.spring.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shzejing.spring.entities.Customer;

public interface CustomerDao extends JpaRepository<Customer	, String> {

	Page<Customer> findByStatusAndNameLike(int statusEnable, String string, Pageable pageable);

	@Query(value = "select c from Customer c where c.status = ? order by c.name")
	List<Customer> findByStatusByOrderByNameAsc(int statusEnable);

	List<Customer> findByUsernameAndUpasswordAndStatus(String username, String upassword, int statusEnable);

}
