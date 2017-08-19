package com.shzejing.spring.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shzejing.spring.common.Common;
import com.shzejing.spring.dao.AccountDao;
import com.shzejing.spring.dao.AdPhotoDao;
import com.shzejing.spring.dao.AdPositionDao;
import com.shzejing.spring.dao.CustomerDao;
import com.shzejing.spring.dao.EventDao;
import com.shzejing.spring.dao.EventJoinDao;
import com.shzejing.spring.dao.EventJoinPersonDao;
import com.shzejing.spring.dao.NewsstandDao;
import com.shzejing.spring.dao.TokenDao;
import com.shzejing.spring.dao.VolunteerCompanyDao;
import com.shzejing.spring.dao.VolunteerDao;
import com.shzejing.spring.entities.Account;
import com.shzejing.spring.entities.AdPhoto;
import com.shzejing.spring.entities.AdPosition;
import com.shzejing.spring.entities.Customer;
import com.shzejing.spring.entities.Event;
import com.shzejing.spring.entities.EventJoin;
import com.shzejing.spring.entities.EventJoinPerson;
import com.shzejing.spring.entities.Newsstand;
import com.shzejing.spring.entities.Token;
import com.shzejing.spring.entities.Volunteer;
import com.shzejing.spring.entities.VolunteerCompany;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Transactional(readOnly = true)
@RequestMapping("/services")
public class WeixinSupportController {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(WeixinSupportController.class);

	@Autowired
	private Environment env;

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private NewsstandDao newsstandDao;

	@Autowired
	private AdPositionDao adpositionDao;

	@Autowired
	private VolunteerDao volunteerDao;

	@Autowired
	private VolunteerCompanyDao volunteerCompanyDao;

	@Autowired
	private TokenDao tokenDao;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private AdPhotoDao adphotoDao;

	@Autowired
	private EventJoinDao eventJoinDao;

	@Autowired
	private EventDao eventDao;

	@Autowired
	private EventJoinPersonDao eventJoinPersonDao;

	@RequestMapping(value = "/customer/adpositions", method = RequestMethod.GET)
	@Transactional
	@ResponseBody
	public List<Newsstand> positions(HttpSession session) {
		Customer customer = (Customer) session.getAttribute("account");
		return newsstandDao.findByCustomerAndStatus(customer.getId(), AdPosition.STATUS_ENABLE);
	}

	/**
	 * 志愿者微信登录接口
	 * 
	 * @param model
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/weixin/login2", method = RequestMethod.GET)
	@Transactional
	public String login2(Model model, HttpSession session, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String appid = env.getProperty("wechat.appid2");
		return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri="
				+ URLEncoder.encode(env.getProperty("wechat.loginok2"), "utf8")
				+ "&response_type=code&scope=snsapi_userinfo#wechat_redirect";
	}

	@RequestMapping(value = "/weixin/app2/info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public Volunteer myinfo(Model model, HttpSession session) {
		Volunteer v = (Volunteer) session.getAttribute("volunteer");
		v = volunteerDao.findOne(v.getId());
		if (Volunteer.TYPE_COMPANY.equals(v.getType())) {
			v.setVcompany(volunteerCompanyDao.findOne(v.getId()));
		} else if (Volunteer.TYPE_PERSON.equals(v.getType())) {
			v.setVcompany(volunteerCompanyDao.findOne(v.getCompany()));
		}
		return v;
	}

	/**
	 * 志愿者微信登录授权成功后的跳转
	 * 
	 * @param code
	 * @param state
	 * @param model
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/weixin/loginok2", method = RequestMethod.GET)
	@Transactional
	public String loginok2(@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "state", required = false) String state, Model model, HttpSession session,
			HttpServletResponse response) throws IOException {
		String appid = env.getProperty("wechat.appid2");
		String appsecret = env.getProperty("wechat.appsecret2");
		String result = Common.httpGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret="
				+ appsecret + "&code=" + code + "&grant_type=authorization_code");
		System.out.println(result);
		JSONObject json = JSONObject.fromObject(result);
		String openid = json.getString("openid");

		List<Volunteer> users = volunteerDao.findByOpenid(openid);
		if (users.isEmpty()) {
			// 未注册过
			Volunteer v = new Volunteer();
			v.setOpenid(openid);
			v.setId(UUID.randomUUID().toString());
			volunteerDao.save(v);
			session.setAttribute("volunteer", v);
		} else {
			if (users.size() > 1) {
				for (Volunteer usr : users) {
					if (usr.getType().equals(Volunteer.TYPE_COMPANY)) {
						session.setAttribute("volunteer", usr);
						break;
					}
				}
			} else {
				session.setAttribute("volunteer", users.get(0));
			}
		}
		return "redirect:" + env.getProperty("wechat.approot2");
	}

	@RequestMapping(value = "/volunteer/save", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public String saveVolunteer(Volunteer volunteer, Model model, HttpSession session, HttpServletResponse response)
			throws IOException {
		Volunteer user = (Volunteer) session.getAttribute("volunteer");
		Volunteer old = volunteerDao.findOne(user.getId());
		old.setOpenid(user.getOpenid());
		old.setBirth(volunteer.getBirth());
		old.setCompany(volunteer.getCompany());
		old.setDepartment(volunteer.getDepartment());
		old.setEmail(volunteer.getEmail());
		old.setGender(volunteer.getGender());
		old.setIdno(volunteer.getIdno());
		old.setJob(volunteer.getJob());
		old.setMobile(volunteer.getMobile());
		old.setName(volunteer.getName());
		old.setQq(volunteer.getQq());
		old.setType(Volunteer.TYPE_PERSON);
		old.setVnum(volunteer.getVnum());
		old.setWechat(volunteer.getWechat());
		volunteerDao.save(old);
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/volunteer/save2", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public String saveVolunteer2(Volunteer volunteer, Model model, HttpSession session, HttpServletResponse response)
			throws IOException {
		Volunteer user = (Volunteer) session.getAttribute("volunteer");
		Volunteer old = volunteerDao.findOne(user.getId());
		old.setBirth(volunteer.getBirth());
		old.setCompany(old.getId());
		old.setDepartment(volunteer.getDepartment());
		old.setEmail(volunteer.getEmail());
		old.setGender(volunteer.getGender());
		old.setIdno(volunteer.getIdno());
		old.setJob(volunteer.getJob());
		old.setMobile(volunteer.getMobile());
		old.setName(volunteer.getName());
		old.setQq(volunteer.getQq());
		old.setVnum(volunteer.getVnum());
		old.setWechat(volunteer.getWechat());
		old.setStatus(1);
		old.setIsmanager(1);
		volunteerDao.save(old);
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/volunteercompany/save", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public String saveVolunteer(VolunteerCompany volunteer, Model model, HttpSession session,
			HttpServletResponse response) throws IOException {
		Volunteer user = (Volunteer) session.getAttribute("volunteer");
		user.setType(Volunteer.TYPE_COMPANY);
		volunteerDao.save(user);
		if (volunteer.getId() == null) {
			volunteer.setId(user.getId());
		}
		volunteer.setWxuser(user.getOpenid());
		volunteerCompanyDao.save(volunteer);
		return new JSONObject().toString();
	}

	/**
	 * 微信登录接口
	 * 
	 * @param model
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/weixin/login", method = RequestMethod.GET)
	@Transactional
	public String login(Model model, HttpSession session, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String appid = env.getProperty("wechat.appid");
		return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri="
				+ URLEncoder.encode(env.getProperty("wechat.loginok"), "utf8")
				+ "&response_type=code&scope=snsapi_userinfo#wechat_redirect";
	}

	/**
	 * 巡视员登录接口
	 * 
	 * @param model
	 * @param session
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/weixin/xlogin", method = RequestMethod.GET)
	@Transactional
	public String xlogin(Model model, HttpSession session, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String appid = env.getProperty("wechat.appid");
		return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri="
				+ URLEncoder.encode(env.getProperty("wechat.xloginok"), "utf8")
				+ "&response_type=code&scope=snsapi_userinfo#wechat_redirect";
	}

	/**
	 * 巡视员登录授权成功后的跳转
	 * 
	 * @param code
	 * @param state
	 * @param model
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/weixin/xloginok", method = RequestMethod.GET)
	@Transactional
	public String xloginok(@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "state", required = false) String state, Model model, HttpSession session,
			HttpServletResponse response) throws IOException {
		String appid = env.getProperty("wechat.appid");
		String appsecret = env.getProperty("wechat.appsecret");
		String result = Common.httpGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret="
				+ appsecret + "&code=" + code + "&grant_type=authorization_code");
		System.out.println(result);
		JSONObject json = JSONObject.fromObject(result);
		String openid = json.getString("openid");
		String accessToken = getAccessToken();

		String userinfo = Common.httpGet("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken
				+ "&openid=" + openid + "&lang=zh_CN");

		JSONObject user = JSONObject.fromObject(userinfo);
		if ("100".equals(user.getString("groupid"))) {
			session.setAttribute("supervisor", openid);
			return "redirect:" + env.getProperty("wechat.approot") + "/index_supervisor.html";
		}

		return "redirect:" + env.getProperty("wechat.approot") + "/login_index.html";
	}

	/**
	 * 微信登录授权成功后的跳转
	 * 
	 * @param code
	 * @param state
	 * @param model
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/weixin/loginok", method = RequestMethod.GET)
	@Transactional
	public String loginok(@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "state", required = false) String state, Model model, HttpSession session,
			HttpServletResponse response) throws IOException {
		String appid = env.getProperty("wechat.appid");
		String appsecret = env.getProperty("wechat.appsecret");
		String result = Common.httpGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret="
				+ appsecret + "&code=" + code + "&grant_type=authorization_code");
		System.out.println(result);
		JSONObject json = JSONObject.fromObject(result);
		String openid = json.getString("openid");

		List<Account> accounts = accountDao.findByOpenidAndUserstatus(openid, Account.ENABLE);
		if (accounts.isEmpty()) {
			// 跳转到核对报亭主
			session.setAttribute("openid", openid);
			return "redirect:" + env.getProperty("wechat.approot") + "/login_manager.html";
		} else {
			// 已注册用户
			Account account = accounts.get(0);
			session.setAttribute("account", account);
			if (account.getUsertype().equals(Account.USERTYPE_MANAGER)) {
				return "redirect:" + env.getProperty("wechat.approot") + "/index_manager.html";
			} else if (account.getUsertype().equals(Account.USERTYPE_SUPERVISOR)) {
				return "redirect:" + env.getProperty("wechat.approot") + "/index_supervisor.html";
			}
		}
		return "redirect:" + env.getProperty("wechat.approot") + "/login_index.html";
	}

	@RequestMapping(value = "/weixin/app/manager/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String managerValid(@RequestParam("mobile") String mobile, @RequestParam("encode") String encode,
			Model model, HttpSession session, HttpServletResponse response) {
		String openid = (String) session.getAttribute("openid");
		session.removeAttribute("openid");
		List<Newsstand> stands = newsstandDao.findByMobileAndEncodeAndStatus(mobile, encode, Newsstand.STATUS_NORMAL);
		if (stands.size() != 1) {
			JSONObject result = new JSONObject();
			result.put("status", 0);
			result.put("info", "验证不通过！");
			return result.toString();
		} else {
			Account account = new Account();
			account.setId(UUID.randomUUID().toString());
			account.setOpenid(openid);
			account.setUsername(mobile);
			account.setUsertype(Account.USERTYPE_MANAGER);
			account.setNewsstand(stands.get(0).getId());
			account.setUserstatus(Account.ENABLE);
			accountDao.save(account);
			session.setAttribute("account", account);

			JSONObject result = new JSONObject();
			result.put("status", 1);
			result.put("info", "验证通过！");
			return result.toString();
		}
	}

	@RequestMapping(value = "/weixin/app/customer/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String customerValid(@RequestParam("username") String username, @RequestParam("upassword") String upassword,
			HttpSession session) {
		JSONObject result = new JSONObject();
		List<Customer> customers = customerDao.findByUsernameAndUpasswordAndStatus(username, upassword,
				Customer.STATUS_ENABLE);
		if (customers.isEmpty()) {
			result.put("status", 0);
			result.put("info", "验证失败！");
			return result.toString();
		} else {
			Customer customer = customers.get(0);
			session.setAttribute("account", customer);
			result.put("status", 1);
			result.put("info", "验证通过！");
			return result.toString();
		}
	}

	@RequestMapping(value = "/weixin/app/adposition", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public AdPhoto newsstandAD(Model model, HttpSession session, HttpServletResponse response) {
		Account account = (Account) session.getAttribute("account");
		Sort sort = new Sort(Sort.Direction.DESC, "updatetime");
		Pageable pageable = new PageRequest(0, 1, sort);
		Page<AdPhoto> adphotos = adphotoDao.findByNewsstand(account.getNewsstand(), pageable);
		List<AdPhoto> photos = adphotos.getContent();
		return photos.isEmpty() ? new AdPhoto() : photos.get(0);
	}

	@RequestMapping(value = "/weixin/app/adposition/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public AdPhoto newsstandADs(@PathVariable("id") String newsstandid, Model model, HttpServletResponse response) {
		Sort sort = new Sort(Sort.Direction.DESC, "updatetime");
		Pageable pageable = new PageRequest(0, 1, sort);
		Page<AdPhoto> adphotos = adphotoDao.findByNewsstand(newsstandid, pageable);
		List<AdPhoto> photos = adphotos.getContent();
		return photos.isEmpty() ? new AdPhoto() : photos.get(0);
	}

	@RequestMapping(value = "/weixin/app/config", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String config(HttpServletRequest request) {
		String url = request.getParameter("url");
		long time = new Date().getTime() / 1000;
		String ticket = getTicket();
		String noncestr = random(16);
		String str = "jsapi_ticket=" + ticket + "&noncestr=" + noncestr + "&timestamp=" + time + "&url=" + url;
		String sign = DigestUtils.sha1Hex(str);
		JSONObject json = new JSONObject();
		json.put("debug", true);
		json.put("timestamp", time);
		json.put("appId", env.getProperty("wechat.appid"));
		json.put("nonceStr", noncestr);
		json.put("signature", sign);
		JSONArray apis = new JSONArray();
		apis.add("chooseImage");
		apis.add("uploadImage");
		apis.add("onMenuShareTimeline");
		apis.add("onMenuShareAppMessage");
		apis.add("onMenuShareQQ");
		apis.add("onMenuShareWeibo");
		apis.add("getLocation");
		json.put("jsApiList", apis);
		return json.toString();
	}

	@RequestMapping(value = "/weixin/app/upload", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String upload(@RequestParam("pic") String pic, @RequestParam("position") int position,
			@RequestParam("latitude") float latitude, @RequestParam("longitude") float longitude, HttpSession session,
			HttpServletResponse response) throws MalformedURLException, IOException {
		Account account = (Account) session.getAttribute("account");
		JSONObject jo = new JSONObject();
		if (account == null) {
			jo.put("status", 0);
			jo.put("msg", "用户不存在");
			return jo.toString();
		}
		if (Account.USERTYPE_MANAGER.equals(account.getUsertype()) || session.getAttribute("supervisor") != null) {
			Newsstand stand = newsstandDao.findOne(account.getNewsstand());
			String pos = stand.getPosition();
			if (StringUtils.isNotEmpty(pos)) {
				String[] ll = pos.split(",");
				float lat = NumberUtils.toFloat(ll[1]);
				float lng = NumberUtils.toFloat(ll[0]);
				if (Math.pow(lat - latitude, 2) + Math.pow(lng - longitude, 2) > 0.0009f * 0.0009f) {
					jo.put("status", 0);
					jo.put("msg", "你当前不位置不在有效范围");
					return jo.toString();
				} else {
					SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
					String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + getAccessToken()
							+ "&media_id=" + pic;
					System.out.println(url);
					String newFile = fmt.format(new Date()) + "/" + pic;
					FileUtils.copyURLToFile(new URL(url), new File(env.getProperty("file.upload.path") + newFile));
					jo.put("url", newFile);
					jo.put("position", position);
					jo.put("status", 1);

					return jo.toString();
				}
			} else {
				jo.put("status", 0);
				jo.put("msg", "书报亭坐标不正确，请联系管理员");
				return jo.toString();
			}
		} else {
			jo.put("status", 0);
			jo.put("msg", "只有认证用户才能上传！");
			return jo.toString();
		}
	}

	@RequestMapping(value = "/weixin/app/upload2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String upload2(@RequestParam("pic") String pic, HttpSession session, HttpServletResponse response)
			throws MalformedURLException, IOException {
		JSONObject jo = new JSONObject();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + getAccessToken() + "&media_id="
				+ pic;
		System.out.println(url);
		String newFile = fmt.format(new Date()) + "/" + pic;
		FileUtils.copyURLToFile(new URL(url), new File(env.getProperty("file.upload.path") + newFile));
		jo.put("url", newFile);
		jo.put("status", 1);

		return jo.toString();
	}

	@RequestMapping(value = "/weixin/app/supervisor", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String supervisor(@RequestParam("id") String id, HttpSession session, HttpServletResponse response)
			throws MalformedURLException, IOException {
		Account account = new Account();
		account.setNewsstand(id);
		session.setAttribute("account", account);
		return new JSONObject().toString();
	}

	@RequestMapping(value = "/weixin/app/photo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String submitPhoto(@RequestParam("pic1") String pic1, @RequestParam("pic2") String pic2,
			@RequestParam("pic3") String pic3, @RequestParam("pic4") String pic4, @RequestParam("pic5") String pic5,
			@RequestParam("pic6") String pic6, HttpSession session, HttpServletResponse response)
			throws MalformedURLException, IOException {
		Account account = (Account) session.getAttribute("account");
		String stand = account.getNewsstand();
		AdPhoto photo = new AdPhoto();
		photo.setPhoto1(pic1);
		photo.setPhoto2(pic2);
		photo.setPhoto3(pic3);
		photo.setPhoto4(pic4);
		photo.setPhoto5(pic5);
		photo.setPhoto6(pic6);
		photo.setNewsstand(stand);
		photo.setUpdatetime(new Date());
		photo.setId(UUID.randomUUID().toString());
		adphotoDao.save(photo);

		return new JSONObject().toString();
	}

	@Transactional
	public String getTicket() {
		Token token = tokenDao.findOne("jsapi_ticket");
		if (token == null || token.getExpire().before(new Date())) {
			token = new Token();
			token.setId("jsapi_ticket");

			String accessToken = getAccessToken();
			String result = Common.httpGet(
					"https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi");
			JSONObject json = JSONObject.fromObject(result);
			if (json.getInt("errcode") > 0) {
				throw new RuntimeException();
			} else {
				String ticket = json.getString("ticket");
				token.setValue(ticket);
				token.setExpire(new Date(new Date().getTime() + 7000 * 1000));
				tokenDao.save(token);
				return ticket;
			}
		} else {
			return token.getValue();
		}
	}

	@RequestMapping(value = "/weixin/users", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String weixinUsers() throws MalformedURLException, IOException {
		JSONArray users = new JSONArray();
		String token = getAccessToken();
		String result = Common.httpGet("https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + token);
		JSONObject json = JSONObject.fromObject(result);
		if (json.has("errcode")) {
			throw new RuntimeException();
		} else {
			JSONArray openids = json.getJSONObject("data").getJSONArray("openid");
			for (int i = 0; i < openids.size(); i++) {
				String openid = openids.getString(i);
				String o = Common.httpGet("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token
						+ "&openid=" + openid + "&lang=zh_CN");
				users.add(JSONObject.fromObject(o));
			}
		}
		return users.toString();
	}

	@Transactional
	public String getAccessToken() {
		Token token = tokenDao.findOne("access_token");
		String appid = env.getProperty("wechat.appid");
		String secret = env.getProperty("wechat.appsecret");

		if (token == null || token.getExpire().before(new Date())) {
			token = new Token();
			token.setId("access_token");
			String result = Common.httpGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
					+ appid + "&secret=" + secret);
			JSONObject json = JSONObject.fromObject(result);
			if (json.has("errcode")) {
				throw new RuntimeException();
			} else {
				String accessToken = json.getString("access_token");
				token.setValue(accessToken);
				token.setExpire(new Date(new Date().getTime() + 7000 * 1000));
				tokenDao.save(token);
				return accessToken;
			}
		} else {
			String result = Common
					.httpGet("https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=" + token.getValue());
			JSONObject json = JSONObject.fromObject(result);
			if (json.has("errcode")) {
				result = Common.httpGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
						+ appid + "&secret=" + secret);
				json = JSONObject.fromObject(result);
				String accessToken = json.getString("access_token");
				token.setValue(accessToken);
				token.setExpire(new Date(new Date().getTime() + 7000 * 1000));
				tokenDao.save(token);
				return accessToken;
			}

			return token.getValue();
		}
	}

	public String random(int length) {
		String seed = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
		int pos = seed.length() - 1;
		String r = "";
		for (int i = 0; i < length; i++) {
			r += seed.charAt(((Double) (pos * Math.random())).intValue());
		}
		return r;
	}

	@RequestMapping(value = "/events", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public Set<Event> events(HttpSession session) throws MalformedURLException, IOException {
		Volunteer account = (Volunteer) session.getAttribute("volunteer");
		List<EventJoin> joins = eventJoinDao.findByVolunteer(account.getId());
		Set<Event> events = new HashSet<Event>();
		for (EventJoin eventJoin : joins) {
			Event event = eventDao.findOne(eventJoin.getEventId());
			event.setCanjoin(eventJoin.getStatus());
			events.add(event);
		}
		return events;
	}

	@RequestMapping(value = "/myteam", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public List<Volunteer> myVolunteers(HttpSession session) {
		Volunteer account = (Volunteer) session.getAttribute("volunteer");
		if (account.getType().equals(Volunteer.TYPE_COMPANY)) {
			List<Volunteer> teams = volunteerDao.findByCompany(account.getId());
			for (Volunteer volunteer : teams) {
				Number s = eventJoinPersonDao.sumByPerson(volunteer.getId());
				volunteer.setScore(s == null ? 0 : s.intValue());
			}
			return teams;
		} else {
			List<Volunteer> teams = volunteerDao.findByCompany(account.getCompany());
			for (Volunteer volunteer : teams) {
				Number s = eventJoinPersonDao.sumByPerson(volunteer.getId());
				volunteer.setScore(s == null ? 0 : s.intValue());
			}
			return teams;
		}
	}

	@RequestMapping(value = "/companys", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public List<VolunteerCompany> companys() {
		List<VolunteerCompany> companys = new ArrayList<VolunteerCompany>();
		List<Volunteer> teams = volunteerDao.findByType(Volunteer.TYPE_COMPANY);
		for (Volunteer v : teams) {
			companys.add(volunteerCompanyDao.findOne(v.getId()));
		}
		companys.addAll(volunteerCompanyDao.findByStatus(1));
		return companys;
	}

	@RequestMapping(value = "/volunteer/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public Volunteer volunteer(@PathVariable("id") String id) {
		Volunteer v = volunteerDao.findOne(id);
		return v;
	}

	@RequestMapping(value = "/volunteerpass/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public Volunteer volunteerPass(@PathVariable("id") String id) {
		Volunteer v = volunteerDao.findOne(id);
		v.setStatus(1);
		return v;
	}

	@RequestMapping(value = "/volunteer/manager/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public Volunteer volunteerManager(@PathVariable("id") String id) {
		Volunteer v = volunteerDao.findOne(id);
		v.setIsmanager(v.getIsmanager() > 0 ? 0 : 1);
		return v;
	}

	@RequestMapping(value = "/volunteernopass/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public Volunteer volunteerNoPass(@PathVariable("id") String id) {
		Volunteer v = volunteerDao.findOne(id);
		v.setStatus(-1);
		return v;
	}

	@RequestMapping(value = "/eventjoin/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public List<EventJoinPerson> eventjoin(@PathVariable("id") String id, HttpSession session) {
		List<EventJoinPerson> ejps = eventJoinPersonDao.findByEventIdAndStatus(id, 1);
		for (EventJoinPerson ejp : ejps) {
			ejp.setPersonObj(volunteerDao.findOne(ejp.getPerson()));
		}
		return ejps;
	}

	@RequestMapping(value = "/eventjoin/{id}/{status}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String eventjoinStatus(@PathVariable("id") String id, @PathVariable("status") int status) {
		eventJoinPersonDao.findOne(id).setStatus(status);
		return "{}";
	}

	@RequestMapping(value = "/eventjoin/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String eventjoin(@PathVariable("id") String id, @RequestParam("score") int score,
			@RequestParam("eventDate") String eventDate, @RequestParam("person") String person) {
		List<EventJoinPerson> ejp = eventJoinPersonDao.findByEventIdAndPersonAndEventDate(id, person, eventDate);
		for (EventJoinPerson eventJoinPerson : ejp) {
			eventJoinPersonDao.delete(eventJoinPerson);
		}
		EventJoinPerson evt = new EventJoinPerson();
		evt.setEventId(id);
		evt.setEventDate(eventDate);
		evt.setPerson(person);
		evt.setStatus(1);
		evt.setScore(score);
		evt.setId(UUID.randomUUID().toString());
		eventJoinPersonDao.save(evt);
		return "{}";
	}

	@RequestMapping(value = "/eventjoin/{id}/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Transactional
	@ResponseBody
	public String eventjoinDelete(@PathVariable("id") String id) {
		eventJoinPersonDao.delete(id);
		return "{}";
	}
}