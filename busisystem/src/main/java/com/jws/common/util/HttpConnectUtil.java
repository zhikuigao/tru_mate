package com.jws.common.util;

import java.net.URLEncoder;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class HttpConnectUtil {
	public static String  invoking(String requestUrl, JSONObject paramJson) throws Exception{
		HttpParams params = new BasicHttpParams();
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,20000);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        //参数编码转换，防止“{}”不识别
    	 String param = URLEncoder.encode(paramJson.toString(),"UTF-8");
    	 //url的相关信息
        String url = requestUrl+param;
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpGet);
        int stateCode = response.getStatusLine().getStatusCode();// 获得联网状态
        if (stateCode == 200) {
        	String strResult = EntityUtils.toString(response.getEntity(), "utf-8");
            return strResult;
		}else {
			return "-1";
		}
	}
}
