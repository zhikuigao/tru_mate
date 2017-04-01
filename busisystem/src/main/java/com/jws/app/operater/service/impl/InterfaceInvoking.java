package com.jws.app.operater.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jws.app.operater.model.SysBusiLog;
import com.jws.app.operater.service.SysBusiLogService;
import com.jws.common.constant.Constants;
import com.jws.common.util.HttpConnectUtil;
import com.jws.common.util.MD5Util;
import com.jws.common.util.ResultPackaging;

/**
 * 接口调用service
 * @author ljw
 *
 */
@Service("interfaceInvoking")
public class InterfaceInvoking {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private SysBusiLogService sysBusiLogService;
	
	public JSONObject invoking(String requestUrl, JSONObject paraMap, JSONObject oldParam){
		JSONObject  paramObject = new JSONObject();
		//根据子业务系统，生成相应的安全校验对象
		JSONObject  securityJson = generateSecurityJson(paraMap.get("receive").toString());		
		paramObject.put("securityJson", securityJson);
		if (null != oldParam) {
			paraMap.put("device", oldParam.optString("device"));
			paraMap.put("token", oldParam.optString("token"));
			paraMap.put("language", oldParam.optString("language"));
			paraMap.put("mateVersion", oldParam.optString("mateVersion"));
		}
		paramObject.put("paramMap", paraMap);
		//添加接口调用日志
		SysBusiLog log  = sysBusiLogService.addCallLog(paraMap);
		String returnString = "";
		try {
			 logger.error("日志"+requestUrl+"日志:"+paramObject);
			 returnString = HttpConnectUtil.invoking(requestUrl, paramObject);
			 logger.error("日志2"+returnString);
		} catch (Exception e) {
			logger.error("接口调用异常: "+e);
		}	
		//更新接口调用日志
		sysBusiLogService.updateCallLog(returnString, log, paraMap);
		//异常或网络问题
		if (StringUtils.isEmpty(returnString) || StringUtils.equals("-1", returnString)) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_104, Constants.RESULT_CODE_104, null, paraMap.get("language").toString());
		}
		return   JSONObject.fromObject(returnString);
	}
	
	/**
	 * 根据约定生成md5安全验证码
	 * @return
	 */
	private  JSONObject  generateSecurityJson(String receive){
		JSONObject  securityJson = new JSONObject();
		String  key = receive+"Mate";
		String  time = Constants.df.format( new Date());
		String  Md5Str = MD5Util.getMD5String(key+time);
		//封装安全机制生成的数据
		securityJson.put("time", time);
		securityJson.put("Md5Str", Md5Str);	
		return securityJson;
	}
	
	
}
