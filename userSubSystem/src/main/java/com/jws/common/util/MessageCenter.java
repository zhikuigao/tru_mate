package com.jws.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

public  class MessageCenter {
	/**
	 * 同步用户信息到消息中心
	 * @throws Exception 
	 */
	public static String SynUserToMessageCenter(JSONObject object) throws Exception {
		HttpURLConnection connection = null;
		URL url = new URL(object.getString("url"));
//		URL url = new URL("http://192.168.1.153:28080/jwim/post");
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
		connection.setRequestProperty("header", object.getString("language"));
		object.remove("language");
		object.remove("url");
		StringBuffer params = new StringBuffer();
		params.append("paramObject").append("=").append(object.toString());
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
			connection.disconnect();
			return  "";
		}
		connection.disconnect();
		return  bankXmlBuffer.toString();
	}
	
	/**
	 * 发送消息到消息中心
	 * @throws Exception 
	 */
	public static JSONObject PushMessage(JSONObject object) throws Exception {
		JSONObject result = new JSONObject();
		HttpURLConnection connection = null;
		URL url = new URL(object.getString("url"));
//		URL url = new URL("http://192.168.1.153:28080/jwim/post");
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
		connection.setRequestProperty("header", object.getString("language"));
		object.remove("language");
		object.remove("url");
		StringBuffer params = new StringBuffer();
		params.append("paramObject").append("=").append(URLEncoder.encode(object.toString(), "UTF8"));
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
			connection.disconnect();
			result.put("code", 0);
			return  result;
		}
		connection.disconnect();
		if (StringUtils.isNotEmpty(bankXmlBuffer.toString())) {
			result.put("result", bankXmlBuffer.toString());
			try {
				JSONObject json = JSONObject.fromObject(bankXmlBuffer.toString());
				JSONObject resultJson = JSONObject.fromObject(json.get("result").toString());
				//code = 1 推送成功 4104 发送方应用信息未找到 4105 发送方应用Token无效，审核未通过
				if (StringUtils.equals(resultJson.get("code").toString(), "200")
						||StringUtils.equals(resultJson.get("code").toString(), "4104")
						||StringUtils.equals(resultJson.get("code").toString(), "4105")) {
					result.put("code", resultJson.get("code").toString());
					return result;
				}
			} catch (Exception e) {
				System.out.println("更新同步标识异常："+e);
			}
		}
		result.put("code", 0);
		return  result;
	}
	
	public static void main(String[] args) {
//		try {
//			JSONObject param = new JSONObject();
////			String  time = Constants.df.format( new Date());
////			param.put("time", time);
////			param.put("md5Str", MD5Util.getMD5String("messageCenter"+time));
//			param.put("requestCode", "chater_register");
//			param.put("appId", 1);
//			param.put("appToken", "83c39424-7e5b-4416-88c1-2d6d6e54b794");
//			param.put("chaterId", "0197ee04dfdd4b38957e57f5a562593e");
//			param.put("chaterType", 0);
//			param.put("language", "EN");
//			param.put("url", "http://192.168.1.153:28080/jwim/post");
//			String result = MessageCenter.SynUserToMessageCenter(param);
//			System.out.println(result);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			System.out.println(e);
//		}
//		Calendar calendar = Calendar.getInstance();
//		System.out.println(Constants.df.format(calendar.getTime()));
//		calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DATE), 10, 0, 0);
//		System.out.println(Constants.df.format(calendar.getTime()));
	}

}
