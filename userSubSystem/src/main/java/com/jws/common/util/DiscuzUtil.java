/**
 * 
 */
package com.jws.common.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @description
 *         论坛处理工具类
 * @date   2016年3月31日 上午9:54:39
 * @author kevin
 * @email  kevin.zhu@jwis.cn
 */
public class DiscuzUtil {

	private static String urlStr = "http://bbs.jwis.cn/forum.php";
	/** 正式环境板块id */
//	private static final String article_plate_id = "39";
	/** 测试环境板块Id */
	private static final String article_plate_id = "37";
	
	public static void main(String[] args) throws Exception {
		
		HashMap<String, String> map = new HashMap<>();
		map.put("mod", "post");
		map.put("action", "newthread");
		map.put("fid", article_plate_id);
		map.put("topicsubmit", "yes");
		map.put("infloat", "yes");
		map.put("handlekey", "fastnewpost");
		map.put("inajax", "1");
		map.put("formhash", "093c5c74");
		map.put("usesig", "");
		map.put("subject", "模拟发表反馈2");
		map.put("message", "我是message\n ----bb9972bd79ad491a8af86ddf323de51d");
		
		sendHttpRequest("POST", urlStr, map);
		
	}
	
	/**
	 * 发表文章
	 * @param plateId	板块id
	 * @param title		文章标题
	 * @param content	文章内容
	 * @return
	 */
	public static int publishArticle(String bbsUrl, String plateId, String title, String content) throws Exception {
		HashMap<String, String> params = new HashMap<>();
		// 拼装discuz的参数
		params.put("mod", "post");
		params.put("action", "newthread");
		params.put("topicsubmit", "yes");
		params.put("infloat", "yes");
		params.put("handlekey", "fastnewpost");
		params.put("inajax", "1");
		params.put("formhash", "093c5c74");
		params.put("usesig", "");
		
		params.put("fid", plateId);
		params.put("subject", title);
		params.put("message", content);
		return sendHttpRequest("POST", bbsUrl, params);
	}
	
	/**
	 * 发送http 请求
	 * @param actionType
	 * @param urlStr
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	private static int sendHttpRequest(String actionType, String urlStr, Map<String, String> parameters) throws Exception {
		HttpURLConnection connection = null;
		URL url = new URL(urlStr);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(actionType);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setReadTimeout(120000);
		connection.setConnectTimeout(5000);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty("Connection", "Keep-Alive");

		// 表单参数提交，输入参数
		StringBuffer sb = new StringBuffer();// 处理请求参数  
        String params = "";// 编码之后的参数 
     
		// 编码请求参数  
        if (parameters.size() == 1) {  
            for (String name : parameters.keySet()) {  
//            	   System.out.println("1编码前"+name+"="+parameters.get(name));
                sb.append(name).append("=").append(  
                        java.net.URLEncoder.encode(parameters.get(name), "UTF-8"));  
            }  
            params = sb.toString(); 
//            System.out.println("编码后params="+params);
        } else {  
            for (String name : parameters.keySet()) {  
//            	 System.out.println("2编码前"+name+"="+parameters.get(name));
                sb.append(name).append("=").append(java.net.URLEncoder.encode(parameters.get(name),"UTF-8")).append("&");  
            }  
            String temp_params = sb.toString(); 
//            System.out.println("编码后temp_params="+temp_params);
            params = temp_params.substring(0, temp_params.length() - 1);  
        } 
        
		byte[] bytes = params.toString().getBytes();
		connection.getOutputStream().write(bytes);

		int responseCode = connection.getResponseCode();
		return responseCode;

	}
	
}
