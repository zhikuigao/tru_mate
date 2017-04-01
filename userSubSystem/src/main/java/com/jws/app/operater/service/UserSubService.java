package com.jws.app.operater.service;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

public interface UserSubService {
	
	public JSONObject addNewUser(JSONObject param);
	
	public JSONObject updateNewUser(JSONObject param);
	
	public int verifyIsValid(JSONObject param);
	
	public void addConfig(String userId);
	
	public int synUserToMC(String userId, String language);
	
	public String sendMsg(JSONObject param);

	public HashMap<String, Object> batchAddPushMsg(List<HashMap<String, Object>> list, String msgType, String pushType);

	public JSONObject selectPushType(JSONObject param);
	
	public JSONObject selectPushContentAll(JSONObject param);
	
	public JSONObject selectPushInnovationType(JSONObject param);

}