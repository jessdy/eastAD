package com.shzejing.spring.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shzejing.spring.common.MD5Util;
import com.shzejing.spring.dao.AccountDao;
import com.shzejing.spring.dao.EventDao;
import com.shzejing.spring.dao.EventJoinDao;
import com.shzejing.spring.dao.EventJoinPersonDao;
import com.shzejing.spring.dao.VolunteerCompanyDao;
import com.shzejing.spring.dao.VolunteerDao;
import com.shzejing.spring.entities.Account;
import com.shzejing.spring.entities.Event;
import com.shzejing.spring.entities.EventJoin;
import com.shzejing.spring.entities.Volunteer;
import com.shzejing.spring.entities.VolunteerCompany;

import net.sf.json.JSONObject;

@Controller
@Transactional(readOnly = true)
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private VolunteerCompanyDao volunteerCompanyDao;

	@Autowired
	private VolunteerDao volunteerDao;

	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private EventDao eventDao;
	
	@Autowired
	private EventJoinDao eventJoinDao;
	
	@Autowired
	private EventJoinPersonDao eventJoinPersonDao;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String admin(HttpSession session) {
		String r = (String) session.getAttribute("type");
		return r;
	}

	@RequestMapping(value = "/volunteercompany/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Page<VolunteerCompany> find(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows,
			@RequestParam(value = "sort", defaultValue = "regtime") String srt,
			@RequestParam(value = "order", defaultValue = "desc") String order) {
		Sort sort = new Sort(order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, srt);
		Pageable pageable = new PageRequest(page - 1, rows, sort);
		Page<VolunteerCompany> news = volunteerCompanyDao.findAll(pageable);
		return news;
	}

	@RequestMapping(value = "/volunteercompany/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public VolunteerCompany findOne(@PathVariable("id") String id) {
		VolunteerCompany v = volunteerCompanyDao.findOne(id);
		Volunteer vv = volunteerDao.findOne(id);
		v.setWxuser(vv.getOpenid());
		return v;
	}

	@RequestMapping(value = "/volunteercompany/shenhe", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String shenhe(VolunteerCompany vc) {
		VolunteerCompany old = volunteerCompanyDao.findOne(vc.getId());
		old.setStatus(vc.getStatus());
		return "redirect:/company-shenhe.html";
	}

	@RequestMapping(value = "/volunteercompany/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String add(VolunteerCompany vc) {
		String id = UUID.randomUUID().toString();
		Volunteer v = new Volunteer();
		v.setId(id);
		v.setOpenid(vc.getWxuser());
		volunteerDao.save(v);
		vc.setId(id);
		vc.setStatus(0);
		volunteerCompanyDao.save(vc);
		return "redirect:/company-shenhe.html";
	}

	@RequestMapping(value = "/volunteercompany/edit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String edit(VolunteerCompany vc) {
		String id = vc.getId();
		Volunteer main = volunteerDao.findOne(id);
		main.setOpenid(vc.getWxuser());
		volunteerDao.save(main);
		volunteerCompanyDao.save(vc);
		return "redirect:/company-shenhe.html";
	}
	
	@RequestMapping(value = "/volunteercompany/{id}/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String delete(@PathVariable("id") String id) {
		volunteerDao.delete(id);
		volunteerCompanyDao.delete(id);
		List<Event> events = eventDao.findByCreator(id);
		for (Event event : events) {
			List<EventJoin> joins = eventJoinDao.findByEventId(event.getId());
			for (EventJoin eventJoin : joins) {
				eventJoinDao.delete(eventJoin);
			}
			eventDao.delete(event);
		}
		return "{}";
	}

	@RequestMapping(value = "/volunteer/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Page<Volunteer> volunteers(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows,
			@RequestParam(value = "order", defaultValue = "desc") String order) {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Volunteer> vs = volunteerDao.findByType(Volunteer.TYPE_PERSON, pageable);
		List<Volunteer> list = vs.getContent();
		for (Volunteer volunteer : list) {
			try {
				volunteer.setVcompany(volunteerCompanyDao.findOne(volunteer.getCompany()));
				Number s = eventJoinPersonDao.sumByPerson(volunteer.getId());
				volunteer.setScore(s == null ? 0 : s.intValue());
			} catch (Exception e) {
			}
		}
		return vs;
	}

	@RequestMapping(value = "/volunteer/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String addvolunteer(Volunteer v) {
		String id = UUID.randomUUID().toString();
		v.setId(id);
		v.setType(Volunteer.TYPE_PERSON);
		v.setStatus(0);
		volunteerDao.save(v);
		return "redirect:/volunteers-list.html";
	}

	@RequestMapping(value = "/volunteer/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Volunteer volunteer(@PathVariable("id") String id) {
		Volunteer v = volunteerDao.findOne(id);
		v.setVcompany(volunteerCompanyDao.findOne(v.getCompany()));
		Number s = eventJoinPersonDao.sumByPerson(v.getId());
		v.setScore(s == null ? 0 : s.intValue());
		return v;
	}

	@RequestMapping(value = "/volunteer/edit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String editVolunteer(Volunteer vc) {
		vc.setType(Volunteer.TYPE_PERSON);
		vc.setStatus(0);
		volunteerDao.save(vc);
		return "redirect:/volunteers-list.html";
	}

	@RequestMapping(value = "/admin/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Page<Account> adminList(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows,
			@RequestParam(value = "order", defaultValue = "desc") String order) {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Account> vs = accountDao.findByUsertypeAndUserstatus(Account.USERTYPE_ADMIN2, Account.ENABLE, pageable);
		return vs;
	}

	@RequestMapping(value = "/admin/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String addvolunteer(Account v) {
		String id = UUID.randomUUID().toString();
		v.setId(id);
		v.setUsertype(Account.USERTYPE_ADMIN2);
		v.setUserstatus(Account.ENABLE);
		v.setUserpass(MD5Util.MD5Encode(v.getUserpass(), "utf-8"));
		accountDao.save(v);
		return "redirect:/admin-list.html";
	}

	@RequestMapping(value = "/admin/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Account admin(@PathVariable("id") String id) {
		return accountDao.findOne(id);
	}

	@RequestMapping(value = "/adminname/{username}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Account> username(@PathVariable("username") String username) {
		return accountDao.findByUsername(username);
	}

	@RequestMapping(value = "/admin/delete/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String deleteAdmin(@PathVariable("id") String id) {
		Account account = accountDao.findOne(id);
		account.setUserstatus(Account.DISABLE);
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/admin/edit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String edit(Account vc) {
		String id = vc.getId();
		Account main = accountDao.findOne(id);
		main.setUserpass(MD5Util.MD5Encode(vc.getUserpass(), "utf-8"));
		accountDao.save(main);
		return "redirect:/admin-list.html";
	}
	
	@RequestMapping(value = "/volunteer/{id}/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	@Transactional
	public String delvolunteer(@PathVariable("id") String id) {
		volunteerDao.delete(id);
		eventJoinPersonDao.deleteByPerson(id);
		return new JSONObject().toString();
	}

}
