package com.shzejing.spring.controller;

import java.util.List;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shzejing.spring.dao.AdPhotoDao;
import com.shzejing.spring.dao.AdPositionDao;
import com.shzejing.spring.dao.CustomerDao;
import com.shzejing.spring.dao.NewsstandDao;
import com.shzejing.spring.entities.AdPhoto;
import com.shzejing.spring.entities.AdPosition;
import com.shzejing.spring.entities.Customer;
import com.shzejing.spring.entities.Newsstand;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Transactional(readOnly = true)
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private AdPositionDao adpositionDao;

	@Autowired
	private NewsstandDao newsstandDao;

	@Autowired
	private AdPhotoDao adphotoDao;

	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Page<Customer> find(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows,
			@RequestParam(value = "sort", defaultValue = "id") String srt,
			@RequestParam(value = "order", defaultValue = "desc") String order) {
		Sort sort = new Sort(order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, srt);
		Pageable pageable = new PageRequest(page - 1, rows, sort);
		Page<Customer> customers = customerDao.findByStatusAndNameLike(Customer.STATUS_ENABLE, "%%", pageable);
		return customers;
	}

	@RequestMapping(value = "/listall", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<Customer> findAll() {
		List<Customer> customers = customerDao.findByStatusByOrderByNameAsc(Customer.STATUS_ENABLE);
		return customers;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String deleteNews(@PathVariable("id") String id) {
		Customer customer = customerDao.findOne(id);
		customer.setStatus(Customer.STATUS_DISABLE);
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Customer findOne(@PathVariable("id") String id, HttpSession session) {
		Customer customer = customerDao.findOne(id);
		return customer;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String add(Customer customer) {
		customer.setId(UUID.randomUUID().toString());
		customer.setStatus(Customer.STATUS_ENABLE);
		customerDao.save(customer);
		return "redirect:/customer-list.html";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	public String saveEvent(Customer customer) {
		customer.setStatus(Customer.STATUS_ENABLE);
		customerDao.save(customer);
		return "redirect:/customer-list.html";
	}

	@RequestMapping(value = "/{id}/ads", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String findAdsByCustomer(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows,
			@RequestParam(value = "sort", defaultValue = "id") String srt,
			@RequestParam(value = "order", defaultValue = "desc") String order, @PathVariable("id") String id) {
		Sort sort = new Sort(order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, srt);
		Pageable pageable = new PageRequest(page - 1, rows, sort);
		Page<AdPosition> adps = adpositionDao.findByCustomer(id, pageable);
		JSONObject jo = JSONObject.fromObject(adps);
		JSONArray jarr = jo.getJSONArray("content");
		for (int i = 0; i < jarr.size(); i++) {
			JSONObject adjson = jarr.getJSONObject(i);
			Newsstand news = newsstandDao.findOne(adjson.getString("newsstand"));
			adjson.put("stand", JSONObject.fromObject(news));
		}
		return jo.toString();
	}

	@RequestMapping(value = "/photos", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Page<AdPhoto> findPhoto(@RequestBody JSONObject jo) {
		int page = jo.optInt("page", 1);
		int rows = jo.optInt("rows", 10);
		String srt = jo.optString("sort", "updatetime");
		String order = jo.optString("order", "desc");
		String stand = jo.optString("stand", null);
		String code = jo.optString("code", null);
		
		Sort sort = new Sort(order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, srt);
		Pageable pageable = new PageRequest(page - 1, rows, sort);
		Page<AdPhoto> adps;
		if (StringUtils.isEmpty(stand) && StringUtils.isEmpty(code)) {
			adps = adphotoDao.findAll(pageable);
		} else if (StringUtils.isEmpty(stand)) {
			adps = adphotoDao.findByCode(code, pageable);
		} else if (StringUtils.isEmpty(code)) {
			adps = adphotoDao.findByNewsstand(stand, pageable);
		} else {
			adps = adphotoDao.findByNewsstandAndCode(stand, code, pageable);
		}
		
		return adps;
	}
}
