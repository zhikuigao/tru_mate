package com.jws.common.constant;


import java.text.SimpleDateFormat;
import java.util.regex.Pattern;


/**
 * 系统常量
 * @author ljw	
 *
 */
public class Constants {
	private Constants() {
	}

	public static final Pattern EMAIL_ADDRESS    = Pattern.compile(
	        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
	        "\\@" +
	        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
	        "(" +
	            "\\." +
	            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
	        ")+"
	    );
	
	public static final Pattern PHONE    = Pattern.compile(
			 "^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$"
	    );		
	
	public static final SimpleDateFormat  df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat  dfi = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat  sdfm = new SimpleDateFormat("yyyy-MM");
	
	public static final String CALL_SOURCE = "busiSystem";
	public static final String CALL_TO_USER = "userSystem";
	public static final String CALL_TO_APP = "appSystem";
	public static final String CALL_TO_SEARCH = "searchSystem";
	//上传DB文件夹名称
	public static final String MATE_RECORD_PATH = "record";
	//用户反馈图片对应文件夹名称
	public static final String MATE_UPLOAD_PATH = "upload";
	//搜索地图分享缩略图对应文件夹名称
	public static final String MATE_SHARE_PATH = "shareThumbnail";
	
	//个人头像存放位置
	public static final String USER_HEAD_PHOTO = "headPhoto";
	
	// 搜索文件目录
	public static final String MATE_SEARCH_FILE_PATH = "search";
	
	public static final String  GIT_LAB="gitLab";
	public static final String  RED_MINE="redMine";
	
	//请求设备
	public static final String  DEVICE_PC="pc";
	public static final String  DEVICE_ANDROID="android";
	public static final String  DEVICE_IOS="ios";
	
	//pic 
	public static final String  PIC_PNG=".png";
	
	//成功
	public static final int  RESULT_CODE_SUCCESS=1;
	public static final int  RESULT_CODE_FAIL=0;
	//其他公共返回code
	// 业务系统100
	/**请求参数为空 */
	public static final int  RESULT_CODE_100 = 100;
	/** 缺少请求参数 */
	public static final int  RESULT_CODE_101 = 101;
	/**busiBlock 有误 */
	public static final int  RESULT_CODE_102 = 102;
	/**busiCode 有误*/
	public static final int  RESULT_CODE_103 = 103;
	/** 调用子系统出错 */
	public static final int  RESULT_CODE_104 = 104;
	/** json格式有误 */
	public static final int  RESULT_CODE_105 = 105;
	/**  程序异常 */
	public static final int  RESULT_CODE_106 = 106;
	/** 转换文件流失败 */
	public static final int  RESULT_CODE_107 = 107;
	/** 保存流文件异常 */
	public static final int  RESULT_CODE_108 = 108;
	/** 安全验证不通过 */
	public static final int  RESULT_CODE_109 = 109;
	/** 未配置busicode对应的路径 */
	public static final int  RESULT_CODE_110 = 110;
	/** 请求参数有误 */
	public static final int  RESULT_CODE_111 = 111;
	/** token已过期，重新登录 */
	public static final int  RESULT_CODE_112 = 112;	
	
	//用户系统1001
	/**	 缺少请求参数	 */
	public static final int  RESULT_CODE_1001 = 1001;
	/**	 账号格式有误,请输入正确的邮箱或手机号	 */
	public static final int  RESULT_CODE_1002 = 1002;
	/** 上传文件异常*/
	public static final int  RESULT_CODE_1005 = 1005;
	/** 图片数量过多*/
	public static final int  RESULT_CODE_1006 = 1006;

	//应用系统2001
	public static final int RESULT_CODE_APP_LACK_PARAM = 2001;
	
	//搜索系统3001
	public static final int RESULT_CODE_SEARCH_LACK_PARAM = 3001;
	public static final int RESULT_CODE_SEARCH_SAVE_FILE_FAIELD = 3002;
	
	//第三方应用接入
	public static final int URL_ERROR = 4001;
	public static final int GET_BODY_FAIL = 4002;
}
