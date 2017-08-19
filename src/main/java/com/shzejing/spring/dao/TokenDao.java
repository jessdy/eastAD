package com.shzejing.spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shzejing.spring.entities.Token;

public interface TokenDao extends JpaRepository<Token, String> {

}
