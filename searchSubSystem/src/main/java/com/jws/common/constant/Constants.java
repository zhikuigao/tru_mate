package com.jws.common.constant;


import java.text.SimpleDateFormat;

/**
 * 系统常量
 * @author
 *
 */
public class Constants {
	private Constants() {
	}
	
	public static final SimpleDateFormat  df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//请求设备
	public static final String  DEVICE_PC="pc";
	public static final String  DEVICE_ANDROID="android";
	public static final String  DEVICE_IOS="ios";
	//分享渠道
	public static final String  SHARE_WECHAT="1";
	
//	public static final String ID = UUID.randomUUID().toString().replaceAll("-", "");
	//成功
	public static final int  RESULT_CODE_SUCCESS=1;
	public static final int  RESULT_CODE_FAIL=0;
	
	public static final int  RESULT_CODE_EXCEPTION = 3000;
	public static final int  RESULT_CODE_BUSICODE_ERROR = 3001;
	public static final int  RESULT_CODE_CONVERT_FAILED = 3002;
	public static final int  RESULT_CODE_LACK_PARAM = 3003;
	public static final int  RESULT_CODE_SECURITY_VERIFY_FAILED = 3004;
	public static final int  NOT_FOUND_MAP = 3005;
	public static final int  NO_USER = 3006;
	// 保存搜索条件失败
	public static final int RESULT_CODE_SAVE_KEY_FAILED = 3101;
	// 保存搜索条件失败
	public static final int QUERY_MAP_FAILED = 3102;
	// 保存搜索的查看记录失败
	public static final int RESULT_CODE_SAVE_VIEWED_FAILED = 3201;
	// 查询用户搜索历史失败
	public static final int RESULT_CODE_QUERY_KEY_HISTORY = 3301;
	// 查询搜索条件对应的所有链接失败
	public static final int RESULT_CODE_QUERY_VIEWED_HISTORY = 3401;
	// 保存搜索记录文件失败
	public static final int RESULT_CODE_SAVE_FILE_FAILED = 3501;
	/** 参数有误	 */
	public static final int ERROR_PARAMTER = 3601;
	/** 参数有误	 */
	public static final int EXIST_DEFINED = 3602;
	
	public static final int  ERROR_MAPS_DATA_ID = 3007;
	
	
	
}
