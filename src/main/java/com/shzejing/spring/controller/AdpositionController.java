package com.shzejing.spring.controller;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shzejing.spring.dao.AdPositionDao;
import com.shzejing.spring.dao.NewsstandDao;
import com.shzejing.spring.entities.AdPosition;

import net.sf.json.JSONObject;

@Controller
@Transactional(readOnly = true)
@RequestMapping("/adposition")
public class AdpositionController {

	@Autowired
	private AdPositionDao adpositionDao;
	
	@Autowired
	private NewsstandDao newsstandDao;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@Transactional
	public String save(AdPosition position) {
		if (StringUtils.isEmpty(position.getId())) {
			position.setId(UUID.randomUUID().toString());
		}
		String district = newsstandDao.findOne(position.getNewsstand()).getArea();
		position.setDistrict(district);
		position.setStatus(AdPosition.STATUS_ENABLE);
		adpositionDao.save(position);
		updateOld(position);
		return "redirect:/newsstand-adlist.html?id=" + position.getNewsstand();
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String deleteNews(@PathVariable("id") String id) {
		adpositionDao.delete(id);
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/save2", method = RequestMethod.POST)
	@Transactional
	public String save2(AdPosition position) {
		if (StringUtils.isEmpty(position.getId())) {
			position.setId(UUID.randomUUID().toString());
		}

		adpositionDao.save(position);
		updateOld(position);
		return "redirect:/customer-ads.html?id=" + position.getCustomer();
	}

	private void updateOld(AdPosition position) {
		if (position.getStatus() == AdPosition.STATUS_ENABLE) {
			List<AdPosition> ads = adpositionDao.findByNewsstandAndPositionAndStatusAndIdNot(position.getNewsstand(),
					position.getPosition(), AdPosition.STATUS_ENABLE, position.getId());
			if (!ads.isEmpty()) {
				for (AdPosition ad : ads) {
					ad.setStatus(AdPosition.STATUS_DISABLE);
				}
			}
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public AdPosition find(@PathVariable("id") String id) {
		return adpositionDao.findOne(id);
	}

}
