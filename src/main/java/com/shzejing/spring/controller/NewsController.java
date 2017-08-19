package com.shzejing.spring.controller;

import java.util.Date;
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

import com.shzejing.spring.dao.NewsDao;
import com.shzejing.spring.entities.Event;
import com.shzejing.spring.entities.News;

import net.sf.json.JSONObject;

@Controller
@Transactional(readOnly = true)
@RequestMapping("/news")
public class NewsController {

	@Autowired
	private NewsDao newsDao;

	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Page<News> find(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows,
			@RequestParam(value = "sort", defaultValue = "uptime") String srt,
			@RequestParam(value = "order", defaultValue = "desc") String order) {
		Sort sort = new Sort(order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, srt);
		Pageable pageable = new PageRequest(page - 1, rows, sort);
		Page<News> news = newsDao.findByNameLike("%%", pageable);
		return news;
	}

	@RequestMapping(value = "/publishlist", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<News> publishlist() {
		List<News> news = newsDao.findByStatusOrderByWeightDesc(News.PUBLISH);
		return news;
	}

	@RequestMapping(value = "/upstatus/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String upstatus(@PathVariable("id") String id) {
		newsDao.findOne(id).setStatus(Event.PUBLISH);
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/downstatus/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String downstatus(@PathVariable("id") String id) {
		newsDao.findOne(id).setStatus(Event.DRAFT);
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String deleteNews(@PathVariable("id") String id) {
		newsDao.delete(id);
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public News findOne(@PathVariable("id") String id, HttpSession session) {
		return newsDao.findOne(id);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String add(@RequestParam("name") String name, @RequestParam("content") String content,
			@RequestParam("position") String position, @RequestParam("weight") Integer weight) {
		News news = new News();
		news.setName(name);
		news.setPosition(position);
		news.setContent(content);
		news.setId(UUID.randomUUID().toString());
		news.setStatus(Event.DRAFT);
		news.setUptime(new Date());
		news.setWeight(weight);
		newsDao.save(news);
		return "redirect:/news-list.html";
	}

	@RequestMapping(value = "/save/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String saveNews(News news) {
		News old = newsDao.findOne(news.getId());
		old.setName(news.getName());
		old.setContent(news.getContent());
		old.setPosition(news.getPosition());
		old.setWeight(news.getWeight());
		newsDao.save(old);
		return "redirect:/news-list.html";
	}

}
