package com.jws.common.util;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jws.common.constant.ConstantEn;
import com.jws.common.constant.ConstantZh;

/**
 * 封装公共的返回函数
 * @author lx
 *
 */
public class ResultPackaging {

	/**
	 * 封装返回函数
	 * @param resultCode 返回码
	 * @param code  错误描述映射码
	 * @param language  返回信息语种
	 * @param   object  返回数据
	 * @return
	 */
	public static final  JSONObject dealJsonObject(int resultCode, int code,  Object object ,String language){ 
		JSONObject returnObject= new JSONObject();
		JSONObject returnCode= new  JSONObject();
		returnCode.put("code", resultCode);
		if (StringUtils.equals("ZH", language)) {
			returnCode.put("desc", ConstantZh.map.get(code));
		}else {
			returnCode.put("desc", ConstantEn.map.get(code));
		}		
		returnObject.put("result", returnCode);
		if (null != object ) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			String gsont = gson.toJson(object);		
			returnObject.put("resultData", gsont);
		}
		return returnObject ;
	}
	
	
	
	
//	@SuppressWarnings("rawtypes")
//	public static final  JSONObject dealMoreObject(int resultCode, int code, String language,  HashMap<String, Object> objectMap){ 
//		JSONObject returnObject= new JSONObject();
//		JSONObject returnCode= new  JSONObject();
//		returnCode.put("code", resultCode);
//		if (StringUtils.equals("ZH", language)) {
//			returnCode.put("desc", ConstantZh.map.get(code));
//		}else {
//			returnCode.put("desc", ConstantEn.map.get(code));
//		}		
//		returnObject.put("result", returnCode);
//		if (null != objectMap && !objectMap.isEmpty()) {
//			JSONObject data = new JSONObject();
//			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//			Iterator iter = objectMap.entrySet().iterator();
//			while (iter.hasNext()) {
//				Map.Entry entry = (Map.Entry) iter.next();
//				Object key = entry.getKey();
//				Object val = entry.getValue();
//				String gsont = gson.toJson(val);	
//				data.put(key, gsont);
//			}
//			returnObject.put("resultData", data);
//		}
//		return returnObject ;
//	}
	    
}
