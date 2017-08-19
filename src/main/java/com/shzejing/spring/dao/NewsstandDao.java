package com.shzejing.spring.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shzejing.spring.entities.Newsstand;

public interface NewsstandDao extends JpaRepository<Newsstand, String> {

	List<Newsstand> findByArea(String areaid);

	List<Newsstand> findByMobileAndEncode(String mobile, String encode);

	List<Newsstand> findByAreaAndStatus(String areaid, int statusNormal);

	List<Newsstand> findByMobileAndEncodeAndStatus(String mobile, String encode, int statusNormal);

	List<Newsstand> findByAreaAndStatus(String areaid, int statusNormal, Sort sort);

	@Query(nativeQuery = true, value = "select distinct n.* from ws_newsstand n inner join ws_adposition a on n.id = a.newsstand and a.`status` = 1 where n.`status` = :status and a.customer = :customer")
	List<Newsstand> findByCustomerAndStatus(@Param("customer") String id, @Param("status") int statusEnable);

}
