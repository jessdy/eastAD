package com.shzejing.spring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.shzejing.spring.entities.Account;

@Repository
public class LoginAdapter extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Account obj = (Account) request.getSession().getAttribute("admin");
		if (!request.getRequestURI().contains("admin")) {
			// 不需要判断权限
			return super.preHandle(request, response, handler);
		} else if (null == obj) {
			// 未登录
			System.out.println("未登录");
			response.sendError(505);
			return false;
		}

		return super.preHandle(request, response, handler);
	}
}
