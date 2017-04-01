package com.jws.common.constant;


import java.text.SimpleDateFormat;


/**
 * 系统常量
 * @author zhubibo	
 *
 */
public class Constants {
	private Constants() {
	}
	
	public static final SimpleDateFormat  df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final SimpleDateFormat  dfd = new SimpleDateFormat("yyyy-MM-dd");
	//成功
	public static final int  RESULT_CODE_SUCCESS=1;
	public static final int  RESULT_CODE_FAIL=0;
	
	public static final int  RESULT_CODE_BUSICODE_ERROR = 2001;
	public static final int  RESULT_CODE_CONVERT_FAILED = 2002;
	public static final int  RESULT_CODE_PARAM_LACK = 2003;
	public static final int  RESULT_CODE_SECURITY_VERIFY_FAILED = 2004;
	
	public static final int  RESULT_CODE_QUERY_APP_TYPES_FAILED = 2101;	
	public static final int  RESULT_CODE_QUERY_APP_INFOS_FAILED = 2201;	
	public static final int  RESULT_CODE_QUERY_APP_VERSIONS_FAIELD = 2301;
	
	public static final int  RESULT_CODE_SAVE_APP_VOTE_FAILED = 2601;
	public static final int  RESULT_CODE_SAVE_APP_VOTE_REPEAT = 2602;
	public static final int  NOT_FOUND_INFO = 2603;
	public static final int  USER_INFO_UN_OVERALL = 2604;
}
