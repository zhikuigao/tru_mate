package com.jws.app.operater.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

public interface ViewService {
	
	public String   qqcallback(HttpServletRequest request);
	
	public String   qqgetopenid(HttpServletRequest request, ModelMap model);
}
