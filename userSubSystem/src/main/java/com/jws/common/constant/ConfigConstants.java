package com.jws.common.constant;


import java.util.Properties;

import com.jws.common.util.FileOperation;


/**
 * 系统常量
 * @author ljw	
 *
 */
public class ConfigConstants {
	private ConfigConstants() {
	}

	//读取常量配置表
	public static Properties properties=FileOperation.readConfigProperties("dataConfig.properties");
	
	public static final String SECURITY_KEY = properties.getProperty("Security.key");
	/**邮箱发送设置  */
	public static final String MAIL_USER = properties.getProperty("Mail.user");
	
	public static final String MAIL_PWD = properties.getProperty("Mail.pwd");
	
	public static final String MAIL_TIME_OUT_ZH = properties.getProperty("Mail.timeout.ZH");
	
	public static final String MAIL_TIME_OUT_EN = properties.getProperty("Mail.timeout.EN");
	
	/** 手机发送设置*/
	public static final String PHONE_USER_ID = properties.getProperty("Phone.userid");
	
	public static final String PHONE_ACCOUNT = properties.getProperty("Phone.account");
	
	public static final String PHONE_PWD = properties.getProperty("Phone.pwd");
	
	public static final String PHONE_TIME_OUT_ZH = properties.getProperty("Phone.timeout.ZH");
	
	public static final String PHONE_TIME_OUT_EN = properties.getProperty("Phone.timeout.EN");	
	
	/** 论坛板块设置 */
	public static final String DISCUZ_BBS_URL = properties.getProperty("Discuz.bbs.url");
	
	public static final String DISCUZ_PALTE_ID = properties.getProperty("Discuz.palte.id");
	
	/**
	 * 读取服务器文件配置
	 */
	public static final String FTP_SERVER = properties.getProperty("File.url");	
	
	public static final String MESSAGE_CENTER = properties.getProperty("MC.url");
	public static final String MESSAGE_KEY = properties.getProperty("MC.key");
	
	public static final String FILE_SERVER = properties.getProperty("File.server");
	
	public static final int MESSAGE_PUSH_TIME1 = Integer.valueOf(properties.getProperty("MC.push.time1"));
	public static final int MESSAGE_PUSH_TIME2 = Integer.valueOf(properties.getProperty("MC.push.time2"));
	
	public static final int PLUGIN_EFFEC_TIME = Integer.valueOf(properties.getProperty("PlugIn.Effective.Time"));

}
