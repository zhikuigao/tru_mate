package com.jws.app.operater.service;

import javax.servlet.http.HttpServletRequest;

public interface FactoryService {
	
	public String  getFactory(HttpServletRequest request);
	
	public String  saveFactory(HttpServletRequest request);
	
	public String  upload(HttpServletRequest request);

}
