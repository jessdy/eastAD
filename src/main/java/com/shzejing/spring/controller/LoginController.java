package com.shzejing.spring.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shzejing.spring.common.MD5Util;
import com.shzejing.spring.dao.AccountDao;
import com.shzejing.spring.entities.Account;

import net.sf.json.JSONObject;

@Controller
@Transactional(readOnly = true)
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private Environment env;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String login() {
		return "redirect:/index.html";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.removeAttribute("admin");
		return "redirect:/index.html";
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String loginPOST(@RequestParam("username") String username, @RequestParam("userpass") String userpass,
			HttpSession session) {

		if (StringUtils.isEmpty(username)) {
			JSONObject result = new JSONObject();
			result.put("status", 0);
			result.put("info", "用户名为空！");
			return result.toString();
		}

		if (StringUtils.isEmpty(userpass)) {
			JSONObject result = new JSONObject();
			result.put("status", 0);
			result.put("info", "密码为空！");
			return result.toString();
		}

		List<Account> accountList = accountDao.findByUsernameAndUserstatusAndUsertype(username, Account.ENABLE,
				Account.USERTYPE_ADMIN);
		JSONObject result = new JSONObject();
		if (accountList.size() == 0 || !accountList.get(0).getUserpass().equals(MD5Util.MD5Encode(userpass, "utf8"))) {
			accountList = accountDao.findByUsernameAndUserstatusAndUsertype(username, Account.ENABLE,
					Account.USERTYPE_ADMIN2);
			if (accountList.size() == 0 || !accountList.get(0).getUserpass().equals(MD5Util.MD5Encode(userpass, "utf8"))) {
				result.put("status", 0);
				result.put("info", "用户名或密码错误！");
				return result.toString();
			} else {
				Account account = accountList.get(0);
				session.setAttribute("admin", account);
				session.setAttribute("type", "admin".equals(account.getUsertype2()) ? "admin2" : "admin3");
				result.put("status", 1);
				result.put("info", "成功！");
			}
		} else {

			Account account = accountList.get(0);
			session.setAttribute("admin", account);
			session.setAttribute("type", "admin1");
			result.put("status", 1);
			result.put("info", "成功！");
		}

		return result.toString();
	}

	@RequestMapping(value = "/ueditor/upload")
	@ResponseBody
	public String ueditoruploadify(Model model, @RequestParam("upfile") MultipartFile multipartFile,
			HttpServletRequest request) throws IOException {
		String originalFilename = multipartFile.getOriginalFilename(); // 文件全名
		String suffix = StringUtils.substringAfter(originalFilename, "."); // 后缀
		String file = new Date().getTime() + "." + suffix;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		File path = new File(env.getProperty("file.upload.path") + fmt.format(new Date()));
		if (!path.exists()) {
			path.mkdirs();
		}
		FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),
				new File(env.getProperty("file.upload.path") + fmt.format(new Date()) + "/" + file));
		JSONObject jo = new JSONObject();
		jo.put("originalName", originalFilename);
		jo.put("name", file);
		jo.put("type", suffix);
		jo.put("state", "SUCCESS");
		jo.put("url", fmt.format(new Date()) + "/" + file);
		return jo.toString();
	}

}
