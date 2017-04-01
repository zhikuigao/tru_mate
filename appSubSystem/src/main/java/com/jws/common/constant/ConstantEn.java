package com.jws.common.constant;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public  class  ConstantEn {
	@SuppressWarnings("rawtypes")
	public final static Map map = new HashMap();  
	// 1.需把code反馈给用户， 其他为程序问题
	static {
		map.put(1, "Success"); 
		map.put(2001, "Error busiCode");
		map.put(2002, "Abnormal parameter transformation");  
		map.put(2003, "Lack of request parameters");
		map.put(2004, "Security verification does not pass in application system");

	    map.put(2101, "Query app types failed");
	    map.put(2201, "Query app infos failed");
	    map.put(2301, "Query app versions failed");
	    map.put(2601, "Save app vote failed");
	    map.put(2602, "Have voted, you can go to the comments section.");
	    map.put(2603, "Not found the corresponding information");
	    map.put(2604, "User information is not overall");
	}
}