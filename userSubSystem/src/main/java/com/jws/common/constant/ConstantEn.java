package com.jws.common.constant;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public  class  ConstantEn {
	@SuppressWarnings("rawtypes")
	public final static Map map = new HashMap();  
	static {  
		  map.put(1, "Success"); 
		  map.put(1100, "Save failed");
		  map.put(1101, "Abnormal parameter transformation");  
		  map.put(1102, "Lack of request parameters");
		  map.put(1103, "Security verification does not pass in user system");
		  map.put(1104, "Program exception");
		  map.put(1105, "Account has been registered");
		  map.put(1106, "Send verify code exception");
		  map.put(1107, "Error busiCode");
		  map.put(1108, "Add new user exception");
		  map.put(1109, "Verify code lose efficacy");
		  map.put(1110, "Account or password is wrong");
		  map.put(1111, "UserId  is error, the corresponding user infomation not exist or not tourist");
		  map.put(1112, "The application version information not exists");
		  map.put(1113, "Old application version information not exists");
		  map.put(1114, "Is the latest version");
		  map.put(1115, "Account has not registered");//1
		  map.put(1116, "Verify code is error");//1
		  map.put(1117, "Invalid email or phone number");//1
		  map.put(1118, "Discuz publish article failed");//1
		  map.put(1119, "Not monitor data");
		  map.put(1120, "Save push infomation exception");
		  map.put(1121, "Push message failed");
		  map.put(1122, "Information has been deleted");
	}

}
