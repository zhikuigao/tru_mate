package com.jws.common.constant;


import java.util.Properties;

import com.jws.common.util.FileOperation;


/**
 * 系统常量
 * @author zhubibo	
 *
 */
public class ConfigConstants {
	private ConfigConstants() {
	}

	//读取常量配置表
	public static Properties properties=FileOperation.readConfigProperties("dataConfig.properties");
	
	public static final String SECURITY_KEY = properties.getProperty("Security.key");
	public static final String GOOGLESEARCH_IP = properties.getProperty("GoogleSearch.ip");
	
	public static final String HTTP_VISIT = properties.getProperty("Http.visit");
	
		

}
