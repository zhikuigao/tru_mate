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
	
	public static SimpleDateFormat  dFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final SimpleDateFormat  df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//接入的第三方应用
	public static final String  GIT_LAB="gitLab";
	public static final String  RED_MINE="redMine";
	
	//请求设备
	public static final String  DEVICE_PC="pc";
	public static final String  DEVICE_ANDROID="android";
	public static final String  DEVICE_IOS="ios";
	
	//成功
	public static final int  RESULT_CODE_SUCCESS=1;
	public static final int  RESULT_CODE_FAIL=0;
	/** 保存失败	 */
	public static final int  RESULT_CODE_1100 = 1100;
	public static final int  RESULT_CODE_1101 = 1101;
	public static final int  RESULT_CODE_1102 = 1102;
	public static final int  RESULT_CODE_1103 = 1103;
	public static final int  RESULT_CODE_1104 = 1104;
	/**	账号已存在	 */
	public static final int  RESULT_CODE_1105 = 1105;
	/** 验证码发送异常 */
	public static final int  RESULT_CODE_1106 = 1106;
	/**	 busiCode 有误	 */
	public static final int  RESULT_CODE_1107 = 1107;
	public static final int  RESULT_CODE_1108 = 1108;
	/**	 验证码已失效	 */
	public static final int  RESULT_CODE_1109 = 1109;
	/**	 账号或密码有误	 */
	public static final int  RESULT_CODE_1110 = 1110;
	/**	用户id有问题	 */
	public static final int  RESULT_CODE_1111 = 1111;
	/**	不存在该应用对应的版本信息	 */
	public static final int  RESULT_CODE_1112 = 1112;
	/**	找不到旧版本对应的信息	 */
	public static final int  RESULT_CODE_1113 = 1113;
	/**	已经是最新版本	 */
	public static final int  RESULT_CODE_1114 = 1114;
	/**	账号未注册	 */
	public static final int  RESULT_CODE_1115 = 1115;
	/**	 验证码不正确	 */
	public static final int  RESULT_CODE_1116 = 1116;
	/**	 邮箱或手机号格式不正确	 */
	public static final int  RESULT_CODE_1117 = 1117;
	/**	Discuz发表文章失败	 */
	public static final int  RESULT_CODE_1118 = 1118;
	/** 未监控到新数据  */
	public static final int  RESULT_CODE_1119 = 1119;
	/**保存推送消息异常*/
	public static final int  RESULT_CODE_1120 = 1120;
	/**推送消息失败*/
	public static final int  RESULT_CODE_1121 = 1121;
	/**信息已删除*/
	public static final int  RESULT_CODE_1122 = 1122;
	
}
