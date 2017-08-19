package com.shzejing.spring.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class RequestPage {
	private int page = 1;
	private int rows = 10;
	private String srt;
	private String order;

	public Pageable toPageable() {
		Pageable pageable = new PageRequest(page - 1, rows, toSort());
		return pageable;
	}

	public Sort toSort() {
		Sort sort = new Sort(this.order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, srt);
		return sort;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSrt() {
		return srt;
	}

	public void setSrt(String srt) {
		this.srt = srt;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
