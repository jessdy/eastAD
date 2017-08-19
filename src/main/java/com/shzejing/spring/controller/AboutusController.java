package com.shzejing.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shzejing.spring.dao.AboutusDao;
import com.shzejing.spring.entities.Aboutus;

import net.sf.json.JSONObject;

@Controller
@Transactional(readOnly = true)
@RequestMapping("/aboutus")
public class AboutusController {
	
	public static final String ABOUTUS_ID = "about_us";
	
	@Autowired
	private AboutusDao aboutusDao;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public Aboutus get() {
		Aboutus news = aboutusDao.findOne(ABOUTUS_ID);
		return news == null ? new Aboutus() : news;
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	@Transactional
	public String save(@RequestParam("content") String content) {
		Aboutus news = new Aboutus();
		news.setContent(content);
		news.setId(ABOUTUS_ID);
		aboutusDao.save(news);
		JSONObject result = new JSONObject();
		result.put("status", 1);
		result.put("info", "成功！");
		return result.toString();
	}

}
