package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jws.app.operater.data.AppVersionMapper;
import com.jws.app.operater.data.McPushContentMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.AppVersion;
import com.jws.app.operater.service.OtherService;
import com.jws.app.operater.service.impl.MessageServiceImpl;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.constant.Constants;

public class MessageServiceTest {
	@InjectMocks
	private OtherService messageService = new MessageServiceImpl();
	@Mock
	private SystemTimeMapper systemTimeMapper;
	@Mock
	private AppVersionMapper appVersionMapper;
	@Mock
	private McPushContentMapper mcPushContentMapper;

	@Before
	public void setUp() throws Exception {
		  MockitoAnnotations.initMocks( this );
	}
	
	@Test
	public void testEntry() {
		JSONObject param = new JSONObject();
		param.put("busiCode", "");
		param.put("language", "ZH");
		Assert.assertNotNull(messageService.entry(param));
		//1.查询系统消息
		param.put("busiCode", BusiCodeConstant.QUERY_SYS_INFO);
		//缺少请求参数
		Assert.assertEquals(0, messageService.entry(param).getJSONObject("result").get("code"));
		//pc端
		param.put("lastTime", "2016-07-05 15:33:44");
		param.put("device", Constants.DEVICE_PC);
		param.put("pageNum", "3");
		param.put("page", "-1");
		List<HashMap<String, Object>> list = new ArrayList<>();
		Mockito.when(mcPushContentMapper.getSystemByPage(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString())).thenReturn(list);
		Assert.assertEquals(1, messageService.entry(param).getJSONObject("result").get("code"));
		HashMap<String, Object> map = new HashMap<>();
		map.put("content", "123445555");
		list.add(map);
		Mockito.when(mcPushContentMapper.getSystemByPage(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString())).thenReturn(list);
		Assert.assertEquals(1, messageService.entry(param).getJSONObject("result").get("code"));
		//android
		param.put("device", Constants.DEVICE_ANDROID);
		
		Mockito.when(appVersionMapper.getTotalVersion(Mockito.any(AppVersion.class))).thenReturn(2);
		Mockito.when(appVersionMapper.getVersionByPage(Mockito.any(AppVersion.class))).thenReturn(list);
		Assert.assertEquals(1, messageService.entry(param).getJSONObject("result").get("code"));
		//2.查询系统消息详情
		param.put("busiCode", BusiCodeConstant.QUERY_SYS_CONTENT);
		//缺少请求参数
		Assert.assertEquals(0, messageService.entry(param).getJSONObject("result").get("code"));
		
		param.put("sysId", "123456");
		Mockito.when(appVersionMapper.getVersionInfo(Mockito.anyString())).thenReturn(null);
		Assert.assertEquals(1122, messageService.entry(param).getJSONObject("result").get("code"));
		map.put("status", "0");
		Mockito.when(appVersionMapper.getVersionInfo(Mockito.anyString())).thenReturn(map);
		Assert.assertEquals(1, messageService.entry(param).getJSONObject("result").get("code"));
	}

}
