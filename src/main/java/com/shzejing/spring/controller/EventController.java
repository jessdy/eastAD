package com.shzejing.spring.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
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

import com.shzejing.spring.dao.AccountDao;
import com.shzejing.spring.dao.EventDao;
import com.shzejing.spring.dao.EventJoinDao;
import com.shzejing.spring.dao.EventJoinPersonDao;
import com.shzejing.spring.dao.VolunteerCompanyDao;
import com.shzejing.spring.dao.VolunteerDao;
import com.shzejing.spring.entities.Account;
import com.shzejing.spring.entities.Event;
import com.shzejing.spring.entities.EventJoin;
import com.shzejing.spring.entities.EventJoinPerson;
import com.shzejing.spring.entities.Volunteer;
import com.shzejing.spring.entities.VolunteerCompany;

import net.sf.json.JSONObject;

@Controller
@Transactional(readOnly = true)
@RequestMapping("/event")
public class EventController {

	@Autowired
	private EventDao eventDao;

	@Autowired
	private EventJoinDao eventJoinDao;

	@Autowired
	private EventJoinPersonDao eventJoinPersonDao;

	@Autowired
	private VolunteerDao volunteerDao;

	@Autowired
	private VolunteerCompanyDao volunteerCompanyDao;

	@Autowired
	private AccountDao accountDao;

	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Page<Event> find(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows,
			@RequestParam(value = "sort", defaultValue = "uptime") String srt,
			@RequestParam(value = "order", defaultValue = "desc") String order, HttpSession session) {
		Sort sort = new Sort(order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, srt);
		Pageable pageable = new PageRequest(page - 1, rows, sort);
		if ("admin2".equals(session.getAttribute("type"))) {
			Page<Event> news = eventDao.findByNameLike("%%", pageable);
			return news;
		} else {
			Account admin = (Account) session.getAttribute("admin");
			Page<Event> news = eventDao.findByCreatorAndNameLike(admin.getId(), "%%", pageable);
			return news;
		}

	}

	@RequestMapping(value = "/publishlist", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Page<Event> publishlist(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows,
			@RequestParam(value = "sort", defaultValue = "uptime") String srt,
			@RequestParam(value = "order", defaultValue = "desc") String order) {
		Sort sort = new Sort(order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, srt);
		Pageable pageable = new PageRequest(page - 1, rows, sort);
		Page<Event> news = eventDao.findByStatus(Event.PUBLISH, pageable);
		List<Event> nelist = news.getContent();
		for (Event event : nelist) {
			if (event.getCreator() != null) {
				
			}
		}
		return news;
	}

	@RequestMapping(value = "/joinlist/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<EventJoin> joinlist(@PathVariable("id") String id) {
		List<EventJoin> joins = eventJoinDao.findByEventId(id);
		List<Volunteer> companys = new ArrayList<Volunteer>();
		for (EventJoin join : joins) {
			VolunteerCompany company = volunteerCompanyDao.findOne(join.getVolunteer());
			if (company != null) {
				join.setVolunteerCompany(volunteerCompanyDao.findOne(join.getVolunteer()));
			}
			Volunteer company2 = volunteerDao.findOne(join.getVolunteer());
			join.setVolunter(company2);
			company2.setOther1(join.getId());
			companys.add(company2);
		}
		return joins;
	}

	@RequestMapping(value = "/upstatus/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String upstatus(@PathVariable("id") String id) {
		Event event = eventDao.findOne(id);
		event.setStatus(Event.PUBLISH);
		if (event.getCreator() != null) {
			Account account = accountDao.findOne(event.getCreator());
			if (account != null && account.getCompany() != null) {
				Volunteer company = volunteerDao.findOne(account.getCompany());
				if (company != null && event.getEventDate() != null) {
					String[] eventdate = event.getEventDate().split(",");
					for (String ed : eventdate) {
						EventJoin join = new EventJoin();
						join.setEventDate(ed);
						join.setEventId(id);
						join.setId(UUID.randomUUID().toString());
						join.setStatus(1);
						join.setVolunteer(account.getCompany());
						eventJoinDao.save(join);
					}
				}
			}

		}
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/downstatus/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String downstatus(@PathVariable("id") String id) {
		eventDao.findOne(id).setStatus(Event.END);
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String deleteNews(@PathVariable("id") String id) {
		eventDao.delete(id);
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Event findOne(@PathVariable("id") String id, HttpSession session) {
		Event event = eventDao.findOne(id);
		List<EventJoin> joins = eventJoinDao.findByEventIdAndStatus(id, EventJoin.CONFIRMED);
		List<String> companys = new ArrayList<String>();
		for (EventJoin join : joins) {
			if (StringUtils.isNotBlank(join.getVolunteer())) {
				VolunteerCompany vc = volunteerCompanyDao.findOne(join.getVolunteer());
				if (vc != null) {
					companys.add(vc.getName());
				}
			}
		}
		event.setCompanys(companys);
		event.setCanjoin(0); // 默认是不能操作
		Volunteer v = (Volunteer) session.getAttribute("volunteer");
		if (v != null) {
			if (Volunteer.TYPE_COMPANY.equals(v.getType())) {
				// 公司账号，判断是否申领过
				List<EventJoin> js = eventJoinDao.findByEventIdAndVolunteer(id, v.getId());
				if (js.isEmpty()) {
					event.setCanjoin(1); // 未申领过
				} else {
					event.setCanjoin(js.get(0).getStatus() == EventJoin.CONFIRMED ? 3 : 5);
				}
			} else if (Volunteer.TYPE_PERSON.equals(v.getType())) {
				// 个人账号，判断所在公司是否申领过
				List<EventJoin> js = eventJoinDao.findByEventIdAndVolunteerAndStatus(id, v.getCompany(),
						EventJoin.CONFIRMED);
				if (!js.isEmpty()) {
					// 已经申领,判断个人是否加入
					List<EventJoinPerson> jps = eventJoinPersonDao.findByEventIdAndPerson(id, v.getId());
					if (jps.isEmpty()) {
						event.setCanjoin(2); // 未加入过
					} else {
						event.setCanjoin(jps.get(0).getStatus() == EventJoinPerson.CONFIRMED ? 4 : 6);
					}
				}
			}
		}

		if (StringUtils.isNotBlank(event.getCreator())) {
			Account account = accountDao.findOne(event.getCreator());
			if ("manager".equals(account.getUsertype2() + "")) {
				event.setCanjoin(0);
			}
		}
		return event;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String add(@RequestParam("name") String name, @RequestParam("content") String content,
			@RequestParam("pic") String pic, @RequestParam("description") String description,
			@RequestParam("beginDate") String beginDate, @RequestParam("endDate") String endDate,
			@RequestParam("eventDate") String eventDate, HttpSession session) throws ParseException {
		Account admin = (Account) session.getAttribute("admin");
		Event news = new Event();
		news.setName(name);
		news.setPic(pic);
		news.setDescription(description);
		news.setContent(content);
		news.setId(UUID.randomUUID().toString());
		news.setStatus(Event.DRAFT);
		news.setUptime(new Date());
		news.setVisittimes(0);
		news.setBeginDate(beginDate);
		news.setEndDate(endDate);
		news.setEventDate(eventDate);
		news.setCreator(admin.getId());
		eventDao.save(news);
		return admin.getUsertype().equals("admin") ? "redirect:/events-list.html" : "redirect:/huodong-list.html";
	}

	@RequestMapping(value = "/save/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String saveEvent(Event event, HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");
		Event old = eventDao.findOne(event.getId());
		old.setName(event.getName());
		old.setContent(event.getContent());
		old.setPic(event.getPic());
		old.setDescription(event.getDescription());
		old.setBeginDate(event.getBeginDate());
		old.setEndDate(event.getEndDate());
		old.setEventDate(event.getEventDate());
		eventDao.save(old);
		return admin.getUsertype().equals("admin") ? "redirect:/events-list.html" : "redirect:/huodong-list.html";
	}

	@RequestMapping(value = "/companyjoin/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	@Transactional
	public String companyjoin(@PathVariable("id") String id, HttpServletRequest request, HttpSession session) {
		Volunteer v = (Volunteer) session.getAttribute("volunteer");
		List<EventJoin> js = eventJoinDao.findByEventIdAndVolunteer(id, v.getId());
		if (js.isEmpty()) {
			EventJoin join = new EventJoin();
			join.setEventId(id);
			join.setId(UUID.randomUUID().toString());
			join.setStatus(EventJoin.UNCONFIRM);
			join.setVolunteer(v.getId());
			join.setEventDate(request.getParameter("date"));
			eventJoinDao.save(join);
		}
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/personjoin/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String personjoin(@PathVariable("id") String id, HttpSession session) {
		Volunteer v = (Volunteer) session.getAttribute("volunteer");
		List<EventJoinPerson> js = eventJoinPersonDao.findByEventIdAndPerson(id, v.getId());
		if (js.isEmpty()) {
			EventJoinPerson join = new EventJoinPerson();
			join.setEventId(id);
			join.setId(UUID.randomUUID().toString());
			join.setStatus(EventJoin.UNCONFIRM);
			join.setPerson(v.getId());
			eventJoinPersonDao.save(join);
		}
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/pass/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	@Transactional
	public String pass(@PathVariable("id") String id, HttpSession session) {
		EventJoin join = eventJoinDao.findOne(id);
		join.setStatus(EventJoin.CONFIRMED);
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/nopass/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	@Transactional
	public String nopass(@PathVariable("id") String id, HttpSession session) {
		EventJoin join = eventJoinDao.findOne(id);
		join.setStatus(EventJoin.REJECT);
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/companyevent/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	@Transactional
	public List<Event> companyEvents(@PathVariable("id") String id, HttpSession session) {
		List<Event> events = eventDao.findByCreatorAndJoin(id);
		for (Event event : events) {
			event.setCanjoin(eventJoinPersonDao.countByEvent(event.getId()).intValue());
		}
		return events;
	}
}
