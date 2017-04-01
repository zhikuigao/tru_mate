package com.jws.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.jws.common.constant.ConfigConstants;


/**
 * 调用qq接口，获取openid
 * @author ljw
 *
 */
public  class GetOpenId {
	
	/**
	 * 发送http 请求
	 * @throws Exception 
	 */
	public static String GetOpenId(String  token) throws Exception {
		HttpURLConnection connection = null;
//		System.out.println("token="+token);
		URL url = new URL("https://graph.qq.com/oauth2.0/me");
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setReadTimeout(120000);
		connection.setConnectTimeout(5000);
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty("Connection", "Keep-Alive");
		// 表单参数提交，输入参数
		StringBuffer params = new StringBuffer();
		params.append("access_token=").append(token);
		byte[] bypes = params.toString().getBytes();
		connection.getOutputStream().write(bypes);
		
		StringBuffer bankXmlBuffer=new StringBuffer();
		BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
		String inputLine;
		while((inputLine=in.readLine())!=null){
			bankXmlBuffer.append(inputLine);
		}
		int responseCode = connection.getResponseCode();
		if (responseCode != 200 || StringUtils.isEmpty(bankXmlBuffer.toString())) {
			return  "";
		}
		JSONObject json = JSONObject.fromObject(bankXmlBuffer.substring(bankXmlBuffer.indexOf("{"), bankXmlBuffer.lastIndexOf("}")+1));		
		if (json.has("openid")) {
			return  json.getString("openid");
		}
		return  "";
	}
	
	public static String GetNickName(String  token, String openId) throws Exception {
		HttpURLConnection connection = null;

		URL url = new URL("https://graph.qq.com/user/get_user_info");
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setReadTimeout(120000);
		connection.setConnectTimeout(5000);
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty("Connection", "Keep-Alive");
		// 表单参数提交，输入参数
		StringBuffer params = new StringBuffer();
		params.append("access_token=").append(token)
		.append("&").append("oauth_consumer_key=").append(ConfigConstants.QQ_CONSUMER_KEY)
		.append("&").append("openid=").append(openId);
		byte[] bypes = params.toString().getBytes();
		connection.getOutputStream().write(bypes);
		
		StringBuffer bankXmlBuffer=new StringBuffer();
		BufferedReader in=new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
		String inputLine;
		while((inputLine=in.readLine())!=null){
			bankXmlBuffer.append(inputLine);
		}
		int responseCode = connection.getResponseCode();
		if (responseCode != 200 || StringUtils.isEmpty(bankXmlBuffer.toString())) {
			return  "";
		}
		JSONObject json = JSONObject.fromObject(bankXmlBuffer.substring(bankXmlBuffer.indexOf("{"), bankXmlBuffer.lastIndexOf("}")+1));		
//		System.out.println(json);
		if (json.has("nickname")) {
			return  json.getString("nickname");
		}
		return  "";
	}
	
	public static void main(String[] args) {
		try {
//			GetOpenId("43AC1E9A91031A771B3236CA32E744E2");
			System.out.println(GetNickName("123","34"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
