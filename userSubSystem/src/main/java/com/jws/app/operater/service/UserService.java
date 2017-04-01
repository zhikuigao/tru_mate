package com.jws.app.operater.service;

import net.sf.json.JSONObject;

public interface UserService {
	public JSONObject commonEntry(String param);
	public void monitorSysNotice();
	
	public void monitorSysUpgrade();
	
	public void monitorTimingPush();
	
	public void pushTimingData();
	
	public void pushFailData();
}
