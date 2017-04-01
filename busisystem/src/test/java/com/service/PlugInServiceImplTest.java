package com.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jws.app.operater.service.impl.InterfaceInvoking;
import com.jws.app.operater.serviceBlock.plugIn.service.impl.PlugInServiceImpl;
import com.jws.common.constant.Constants;

public class PlugInServiceImplTest {
	@InjectMocks
	private PlugInServiceImpl PlugInService = new PlugInServiceImpl();
	@Mock
	private InterfaceInvoking interfaceInvoking;
	@Mock
	private HttpServletRequest request;
	
	@Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));	
    }

	@Test
	public void testEntry() throws Exception {
		JSONObject param = new JSONObject();
		param.put("busiCode", "");
		//1.找不到对应的业务方法
		Assert.assertEquals(0, PlugInService.entry(param).getJSONObject("result").get("code"));
		param.put("busiCode", "createUrl");
		//2.缺少参数
		Assert.assertEquals(0, PlugInService.entry(param).getJSONObject("result").get("code"));
		param.put("userId", "123456789");
		param.put("plugName", "gitLab");
		//3.生成gitLab对应配置连接
		Assert.assertEquals(1, PlugInService.entry(param).getJSONObject("result").get("code"));
	}

	@Test
	public void testAccessPoint() throws IOException {
		request = Mockito.mock(HttpServletRequest.class);  
		String path = "0UUEnLcbZz9DAb%2BSyfS0kHf3Wj4a6c1zsXl0aOolMxUsRF1oYWissLUhU9n%2B+Lj4J";
		Mockito.when(request.getQueryString()).thenReturn(path);
		JSONObject hook = new JSONObject(); 
		hook.put("user_name", "admin");
		hook.put("object_kind", "push");
		hook.put("total_commits_count", 2);
		hook.put("ref", "refs/heads/master");
		JSONObject project = new JSONObject(); 
		project.put("name", "admin");
		JSONArray commits = new JSONArray(); 
		JSONObject com  = new JSONObject();
		JSONObject com1  = new JSONObject();
		JSONObject author  = new JSONObject();
		author.put("name", "admin");
		com.put("id", "123");
		com.put("message", "testJunit");
		com.put("author", author);
		com.put("timestamp", "2015-09-24T17:58:48");
		com1.put("id", "123");
		com1.put("message", "testJunit1");
		com1.put("author", author);
		com1.put("timestamp", "2015-09-24T17:58:48");
		commits.add(com);
		commits.add(com1);
		hook.put("project", project);
		hook.put("commits", commits);
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	    ObjectOutputStream outStream = new ObjectOutputStream(byteOut);
	    outStream.writeObject(hook.toString());
		final InputStream is= new ByteArrayInputStream(byteOut.toByteArray(),7,byteOut.toByteArray().length);
		Mockito.when(request.getInputStream()).thenReturn(new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return is.read();
			}
		});
		Mockito.when(interfaceInvoking.invoking(Mockito.anyString(), Mockito.any(JSONObject.class), Mockito.any(JSONObject.class)))
		.thenReturn(null);
		Assert.assertNull(PlugInService.accessPoint(request, Constants.GIT_LAB));
		byteOut.close();
		outStream.close();
		is.close();
	}

}
