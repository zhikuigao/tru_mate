package com.jws.app.operater.service;

import net.sf.json.JSONObject;

import com.jws.app.operater.model.SysBusiLog;

public interface SysBusiLogService {
	
	public  SysBusiLog addCallLog(JSONObject  jsonObject);
	
	public  void  updateCallLog(String  returnJson, SysBusiLog  oldLog , JSONObject  jsonObject);
}
