package com.jws.app.operater.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public interface BusiService {
	
	public JSONObject  commonEntry(HttpServletRequest request);
	
	public JSONObject  commonFileEntry(HttpServletRequest request);
	
	public long getSysTime();
	
}
