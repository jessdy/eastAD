package com.shzejing.spring.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shzejing.spring.dao.AdPositionDao;
import com.shzejing.spring.dao.NewsstandDao;
import com.shzejing.spring.entities.AdPosition;
import com.shzejing.spring.entities.Newsstand;

import net.sf.json.JSONObject;

@Controller
@Transactional(readOnly = true)
@RequestMapping("/newsstand")
public class NewsstandController {

	@Autowired
	private NewsstandDao newsstandDao;
	
	@Autowired
	private AdPositionDao adpositionDao;

	@RequestMapping(value = "/area", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Newsstand> findByArea(@RequestParam("id") String areaid) {
		Sort sort = new Sort(Direction.ASC, "name");
		List<Newsstand> newsstands = newsstandDao.findByAreaAndStatus(areaid, Newsstand.STATUS_NORMAL, sort);
		return newsstands;
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	@Transactional
	public String deleteNewsstand(@PathVariable("id") String id) {
		newsstandDao.findOne(id).setStatus(Newsstand.STATUS_DELETED);
		return new JSONObject().toString();
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String addNewsstand(Newsstand newsstand) {
		newsstand.setId(UUID.randomUUID().toString());
		newsstand.setStatus(Newsstand.STATUS_NORMAL);
		newsstandDao.save(newsstand);
		return "redirect:/newsstand-list.html";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String updateNewsstand(Newsstand newsstand) {
		Newsstand stand = newsstandDao.findOne(newsstand.getId());
		stand.setName(newsstand.getName());
		stand.setArea(newsstand.getArea());
		stand.setCode(newsstand.getArea());
		stand.setEncode(newsstand.getEncode());
		stand.setMaster(newsstand.getMaster());
		stand.setMobile(newsstand.getMobile());
		stand.setPosition(newsstand.getPosition());
		stand.setLeftad(newsstand.getLeftad());
		stand.setRightad(newsstand.getRightad());
		stand.setCenterad(newsstand.getCenterad());
		stand.setBacknumber(newsstand.getBacknumber());
		stand.setSidenumber(newsstand.getSidenumber());
		newsstandDao.save(stand);
		return "redirect:/newsstand-list.html";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Newsstand findOne(@PathVariable("id") String id, HttpSession session) {
		return newsstandDao.findOne(id);
	}
	
	@RequestMapping(value = "/{id}/ads", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<AdPosition> getAds(@PathVariable("id") String id) {
		return adpositionDao.findByNewsstandAndStatus(id, AdPosition.STATUS_ENABLE);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Page<Newsstand> find(@RequestBody JSONObject json) {
		int page = json.optInt("page", 1);
		int pagesize = json.optInt("pagesize", 10);
		String srt = json.optString("sort", "encode");
		String order = json.optString("order", "desc");
		Sort sort = new Sort(order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, srt);
		Pageable pageable = new PageRequest(page - 1, pagesize, sort);
		Page<Newsstand> stands = newsstandDao.findAll(pageable);
		return stands;
	}
}
