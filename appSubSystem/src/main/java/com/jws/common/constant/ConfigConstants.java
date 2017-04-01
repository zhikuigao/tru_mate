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
	// 文件服务器路径
	public static final String FILE_SERVER_URL = properties.getProperty("FileServer.url");
	
	// 批量插入数据路数量配置
	public static final int BATCH_INSERT = Integer.valueOf(properties.getProperty("Batch.insert"));
	
	//时间去哪展示应用个数
	public static final int TIME_SHOW_NUMBER = Integer.valueOf(properties.getProperty("Time.show.number"));

}
