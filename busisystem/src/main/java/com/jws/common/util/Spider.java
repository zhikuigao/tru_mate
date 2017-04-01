package com.jws.common.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class Spider {
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception{
         
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,20000);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
         
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        
        try{
            String url = "http://www.baidu.com";
            HttpGet httpGet = new HttpGet(url);
            System.out.println("executing request " + httpGet.getURI());
            
            ResponseHandler<String> responseHandler = new ResponseHandler<String>(){
                public String handleResponse(final HttpResponse response) throws ClientProtocolException,IOException{
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300){
                        HttpEntity entity = response.getEntity();
                        return entity !=null ? EntityUtils.toString(entity) : null;
                    }else{
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            String responseBody = httpclient.execute(httpGet,responseHandler);
            System.out.println("-------------------------------------------");
            System.out.println(responseBody);
            System.out.println("-------------------------------------------");
        }finally{
//        	httpclient.close();
        }
    }

}
