package com.jws.app.operater.serviceBlock.plugIn.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public interface PlugInService {
	
	public JSONObject entry(JSONObject param) throws Exception;
	
	public JSONObject accessPoint(HttpServletRequest request, String plugName)  throws IOException ;

}
