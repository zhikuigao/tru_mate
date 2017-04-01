package com.jws.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jws.common.constant.ConfigConstants;


/**
 * 手机验证码发送
 * @author ljw
 *
 */
public  class SendPhoneVerifyCode {
	private final Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 发送http 请求
	 * @throws Exception 
	 */
	public static int SendPhoneVerifyCode(String  phone, String  code, String  language) throws Exception {
		HttpURLConnection connection = null;

//		URL url = new URL("http://sz.ipyy.com/smsJson.aspx");
		URL url = new URL("http://222.73.117.158/msg/HttpBatchSendSM");
//		System.out.println("http://222.73.117.158/msg/HttpBatchSendSM");
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setReadTimeout(120000);
		connection.setConnectTimeout(5000);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty("Connection", "Keep-Alive");
		
		String  message = "";
		if (StringUtils.equals("ZH", language)) {
			 message = "【小美】您的验证码是"+code+"，有效期为"+ConfigConstants.PHONE_TIME_OUT_ZH+"分钟，请尽快验证。如非本人操作，请忽略本短信";
		}else {
			 message = "【Mate】your verify code is "+code+", validity is "+ConfigConstants.PHONE_TIME_OUT_EN+" minutes, Please verify as soon as possible. If the operation not in person, please ignore this message";
		}
		message = URLEncoder.encode(message, "UTF8");
		// 表单参数提交，输入参数
		StringBuffer params = new StringBuffer();
//		params.append("userid").append("=").append(ConfigConstants.PHONE_USER_ID)
//		.append("&").append("account").append("=").append(ConfigConstants.PHONE_ACCOUNT)
//		.append("&").append("password").append("=").append(ConfigConstants.PHONE_PWD)
//		.append("&").append("mobile").append("=").append(phone)
//		.append("&").append("content").append("=").append(message)
//		.append("&").append("sendTime").append("=").append("")
//		.append("&").append("action").append("=").append("send")
//		.append("&").append("extno").append("=").append("");
		params.append("account").append("=").append(ConfigConstants.PHONE_ACCOUNT)
		.append("&").append("pswd").append("=").append(ConfigConstants.PHONE_PWD)
		.append("&").append("mobile").append("=").append(phone)
		.append("&").append("msg").append("=").append(message)
		.append("&").append("needstatus").append("=").append(true);
//		System.out.println(params.toString());
		byte[] bypes = params.toString().getBytes();
		connection.getOutputStream().write(bypes);
		
		StringBuffer bankXmlBuffer=new StringBuffer();
		BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
		String inputLine;
		while((inputLine=in.readLine())!=null){
			bankXmlBuffer.append(inputLine);
		}
		int responseCode = connection.getResponseCode();
//		System.out.println("验证码发送返回code="+responseCode);
//		System.out.println("验证码发送返回content="+bankXmlBuffer);
		if (responseCode != 200 || StringUtils.isEmpty(bankXmlBuffer.toString())) {
			connection.disconnect();
			return  -1;
		}
		//{"returnstatus":"Success","message":"操作成功","remainpoint":"28","taskID":"1601261646309117","successCounts":"1"}
//		JSONObject returnJson = JSONObject.fromObject(bankXmlBuffer.toString());
//		if (!StringUtils.equals("Success", returnJson.getString("returnstatus"))) {
//			return  -1;
//		}
		String[] status = bankXmlBuffer.toString().split(",");
		if (status.length<2 || !StringUtils.equals("0", status[1].substring(0, 1))) {
			connection.disconnect();
			return  -1;
		}	
		connection.disconnect();
		return  0;
	}
	
	
	public static void main(String[] args) {
		
	}

}
