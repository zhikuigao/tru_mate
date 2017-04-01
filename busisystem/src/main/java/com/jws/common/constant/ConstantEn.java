package com.jws.common.constant;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public  class  ConstantEn {
	@SuppressWarnings("rawtypes")
	public final static Map map = new HashMap();  
	static {  
		map.put(1, "Success"); 
		// 业务系统100
		map.put(100, "Request parameters is empty"); 
	    map.put(101, "Lack of request parameters");  
	    map.put(102, "Error busiBlock"); 
	    map.put(103, "Error busiCode"); 
	    map.put(104, "Calling subsystem error"); 
	    map.put(105, "Json format is wrong"); 
	    map.put(106, "Program exception");
	    map.put(107, "Pase file stream error");
	    map.put(108, "Save file stream error");
	    map.put(109, "Security verification does not pass in busi system");  
	    map.put(110, "No configuration of corresponding path in uploading"); 
	    map.put(111, "Error request parameters"); 
	    map.put(112, "Login has expired");
	    // 用户子系统1001
	    map.put(1001, "Lack of request parameters");
	    map.put(1002, "Account  format is wrong, please input right email or phone number"); 
	    map.put(1005, "Fetching user behavior and upload it error"); 
	    map.put(1006, "Too many pictures");
	    // 应用子系统2001 
	    map.put(2001, "Lack of request parameters"); 
	    // 搜索子系统3001
	    map.put(3001, "Lack of request parameters");
	    map.put(3002, "Save user search record failed");
	    // 第三方应用接入
	    map.put(4001, "Error request parameters");
	    map.put(4002, "Get body's information failed");
	    
	}

}
