package com.jws.app.operater.service;

import net.sf.json.JSONObject;

public interface UserBusiService {
	
	public JSONObject  sendVerifyCode(JSONObject param);
	
	public JSONObject register(JSONObject param);
	
	public JSONObject login(JSONObject param);
	
	public  JSONObject  entry(JSONObject  param);
	
	public  JSONObject  monitorSysNotice();
	
	public  JSONObject  monitorSysUpgrade();
	
	public  JSONObject  monitorTimingPush();
	
	public  JSONObject  monitorPushData();
	
	public  JSONObject  monitorFailData();
}
