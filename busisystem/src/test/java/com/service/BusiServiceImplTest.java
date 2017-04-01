package com.service;

import static org.junit.Assert.fail;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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

import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.SysBusiLog;
import com.jws.app.operater.service.BlockEntryService;
import com.jws.app.operater.service.SysBusiLogService;
import com.jws.app.operater.service.impl.BusiServiceImpl;
import com.jws.app.operater.serviceBlock.plugIn.service.PlugInService;
import com.jws.app.operater.serviceBlock.user.service.UserTokenService;

public class BusiServiceImplTest {
	@InjectMocks
	private BusiServiceImpl busiService = new BusiServiceImpl(); 
	@Mock
	private UserTokenService userTokenService;
	@Mock
	private BlockEntryService  userService;
	@Mock
	private BlockEntryService  appService;
	@Mock
	private BlockEntryService  searchService;
	@Mock
	private PlugInService plugInService;
	@Mock
	private SysBusiLogService sysBusiLogService;
	@Mock
	private SystemTimeMapper systemTimeMapper;
	@Mock
	private HttpServletRequest request;
	
	@Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));	
    }

	@Test
	public void testCommonEntry() throws Exception{
		request = Mockito.mock(HttpServletRequest.class);  
		Mockito.when(request.getParameter("paramObject")).thenReturn(""); 
		//1 空参数
		JSONObject paramJson = new JSONObject();
		SysBusiLog log = new SysBusiLog();
		Mockito.when(sysBusiLogService.addCallLog(Mockito.any(JSONObject.class))).thenReturn(log);
		Mockito.doThrow(new RuntimeException()).doNothing().when(sysBusiLogService).updateCallLog(Mockito.any(String.class),Mockito.any(SysBusiLog.class),Mockito.any(JSONObject.class));
		Mockito.when(request.getParameter("paramObject")).thenReturn(paramJson.toString()); 
		//2参数验证不通过
		try {
			Assert.assertEquals(0,busiService.commonEntry(request).getJSONObject("result").get("code"));
		} catch (RuntimeException e) {
			Assert.assertTrue(e.toString().contains("RuntimeException"));
		}
		
		paramJson.put("busiBlock", "userBlock");
		paramJson.put("Md5Str", "f300d559b371bb9a93d8f14b2b6cf8df");
		paramJson.put("time", "2016-09-24 17:58:48");
		paramJson.put("busiCode", "queryAppInfo");
		Mockito.when(request.getParameter("paramObject")).thenReturn(paramJson.toString()); 
		Mockito.when(userTokenService.validToken(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		//2.1 token验证
		Assert.assertEquals(112,busiService.commonEntry(request).getJSONObject("result").get("code"));
		Mockito.when(userTokenService.validToken(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		//3安全验证不通过
		Assert.assertEquals(0,busiService.commonEntry(request).getJSONObject("result").get("code"));
		paramJson.put("time", "2015-09-24 17:58:48");
		Mockito.when(request.getParameter("paramObject")).thenReturn(paramJson.toString());
		JSONObject resultJson = new JSONObject();
		Mockito.when(userService.entry(Mockito.any(JSONObject.class))).thenReturn(resultJson);
		//4.userBlock
		Assert.assertNotNull(busiService.commonEntry(request));
		paramJson.put("busiBlock", "appBlock");
		Mockito.when(request.getParameter("paramObject")).thenReturn(paramJson.toString());
		Mockito.when(appService.entry(Mockito.any(JSONObject.class))).thenReturn(paramJson);
		//5.appBlock
		Assert.assertNotNull(busiService.commonEntry(request));
		paramJson.put("busiBlock", "searchBlock");
		Mockito.when(request.getParameter("paramObject")).thenReturn(paramJson.toString());
		Mockito.when(searchService.entry(Mockito.any(JSONObject.class))).thenReturn(paramJson);
		//6.searchBlock
		Assert.assertNotNull(busiService.commonEntry(request));
		
		paramJson.put("busiBlock", "plugInBlock");
		Mockito.when(request.getParameter("paramObject")).thenReturn(paramJson.toString());
		Mockito.when(plugInService.entry(Mockito.any(JSONObject.class))).thenReturn(paramJson);
		//7.plugInBlock
		Assert.assertNotNull(busiService.commonEntry(request));
		
		paramJson.put("busiBlock", "1234");
		Mockito.when(request.getParameter("paramObject")).thenReturn(paramJson.toString());
		//8.找不到对应的模块
		Assert.assertEquals(0,busiService.commonEntry(request).getJSONObject("result").get("code"));
	}

//	@Test
	public void testCommonFileEntry() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSysTime() {
		Mockito.when(systemTimeMapper.getSystemTime()).thenReturn(new Date());
		Assert.assertNotNull(busiService.getSysTime());
	}
	

}
