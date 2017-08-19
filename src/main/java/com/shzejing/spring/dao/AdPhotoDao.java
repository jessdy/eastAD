package com.shzejing.spring.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shzejing.spring.entities.AdPhoto;

public interface AdPhotoDao extends JpaRepository<AdPhoto, String> {

	Page<AdPhoto> findByNewsstand(String newsstand, Pageable pageable);

	Page<AdPhoto> findByCode(String code, Pageable pageable);

	Page<AdPhoto> findByNewsstandAndCode(String stand, String code, Pageable pageable);


}
