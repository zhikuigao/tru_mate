package com.service;

import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jws.app.operater.data.McPushContentMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.service.UserSubService;
import com.jws.app.operater.service.impl.PlugInServiceImpl;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.constant.Constants;
public class PlugInServiceTest {

	@InjectMocks
	private PlugInServiceImpl plugInService = new PlugInServiceImpl();
	@Mock
	private UserSubService userSubService;
	@Mock
	private SystemTimeMapper systemTimeMapper;
	@Mock
	private McPushContentMapper mcPushContentMapper;
	
	@Before
	public void setUp() throws Exception {
		  MockitoAnnotations.initMocks( this );
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testEntry() {
		JSONObject param = new JSONObject();
		param.put("busiCode", BusiCodeConstant.TO_SEND_MESSAGE);
		JSONArray pathInfo = new JSONArray();
		pathInfo.add(Constants.GIT_LAB);
		pathInfo.add(1);
		pathInfo.add("userId123456");
		param.put("pathInfo", pathInfo);
		JSONObject contentJson = new JSONObject();
		param.put("bodyJson", contentJson);
		Mockito.when(systemTimeMapper.getSystemTime()).thenReturn(new Date());
		Mockito.when(mcPushContentMapper.batchInsertPushMsg(Mockito.anyList())).thenReturn(1);
		Mockito.when(userSubService.sendMsg(Mockito.any(JSONObject.class))).thenReturn("200");
		Mockito.when(mcPushContentMapper.batchUpdatePushStatus(Mockito.anyList())).thenReturn(1);
		//发送第三方消息接口
		Assert.assertEquals(1, plugInService.entry(param).getJSONObject("result").get("code"));
	}
	
	
	
	
	

}
