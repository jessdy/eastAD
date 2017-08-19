package com.shzejing.spring.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shzejing.spring.entities.Account;

public interface AccountDao extends JpaRepository<Account, String> {

	public List<Account> findByUsername(String username);

	public List<Account> findByUsernameAndUserstatus(String username, int userstatus);

	public List<Account> findByUsernameAndUserstatusAndUsertype(String username, int enable, String usertypeAdmin);

	public List<Account> findByOpenidAndUserstatus(String openid, int userstatus);

	public Page<Account> findByUsertypeAndUserstatus(String string, int userstatus, Pageable pageable);

}
