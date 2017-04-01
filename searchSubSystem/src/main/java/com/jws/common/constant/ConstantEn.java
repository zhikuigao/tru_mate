package com.jws.common.constant;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ConstantEn {
	@SuppressWarnings("rawtypes")
	public final static Map map = new HashMap();
	// 1.需把code反馈给用户， 其他为程序问题
	static {
		map.put(1, "Success");
		map.put(3000, "Program exception");
		map.put(3001, "Error busiCode");
		map.put(3002, "Abnormal parameter transformation");
		map.put(3003, "Lack of request parameters");
		map.put(3004, "Security verification does not pass in application system");
		map.put(3005, "Corresponding map information was not found.");
		map.put(3006, "Information of the user is error");
		map.put(3007, "Error id of map's data");
		map.put(3101, "Save search key word failed");
		 map.put(3102, "Query search map failed");
		map.put(3201, "Save viewed url failed");
	    map.put(3301, "Query search history failed");
	    map.put(3401, "Query view history failed");
	    map.put(3501, "Query search record file failed");
	    map.put(3601, "Error request parameter");
	    map.put(3602, "Name or link has exist");
	}
}
