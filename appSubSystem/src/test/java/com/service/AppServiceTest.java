package com.service;

import java.util.ArrayList;
import java.util.Date;
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
import com.jws.app.operater.data.AppHotMessageMapper;
import com.jws.app.operater.data.AppInfoMapper;
import com.jws.app.operater.data.AppTypeMapper;
import com.jws.app.operater.data.AppUsedPercentMapper;
import com.jws.app.operater.data.SysAppLogMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.AppHotMessage;
import com.jws.app.operater.model.AppType;
import com.jws.app.operater.model.AppVote;
import com.jws.app.operater.model.SysAppLog;
import com.jws.app.operater.service.AppBusiService;
import com.jws.app.operater.service.AppService;
import com.jws.app.operater.service.impl.AppServiceImpl;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.exception.ServiceException;

public class AppServiceTest{
	@InjectMocks
	private  AppService appService = new AppServiceImpl();
	@Mock
	private  SysAppLogMapper sysAppLogMapper;
	@Mock
	private AppInfoMapper appInfoMapper;
	@Mock
	private AppTypeMapper appTypeMapper;
	@Mock
	private AppBusiService appBusiService;
	@Mock
	private AppUsedPercentMapper appUsedPercentMapper;
	@Mock
	private SystemTimeMapper systemTimeMapper;
	@Mock
	private AppHotMessageMapper appHotMessageMapper;
	
	@Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );
    }
	
	@Test
	public void testCommonEntry() throws ServiceException{
		JSONObject json = new JSONObject();
		JSONObject paramMap = new JSONObject();
		paramMap.put("source", "busiSystem");
		paramMap.put("receive", "appSystem");
		paramMap.put("language", "ZH");
		paramMap.put("userId", "ed90f33bd1c54470a89b883a959a0473");
		json.put("paramMap", paramMap);
		JSONObject securityJson = new JSONObject();
		securityJson.put("time", "2016-04-25 18:08:50");
		json.put("securityJson", securityJson);
		//mock 注入类调用方法
		Mockito.when(sysAppLogMapper.logInsert(Mockito.any(SysAppLog.class))).thenReturn(1);
		Mockito.when(sysAppLogMapper.logUpdateByPrimaryKey(Mockito.any(SysAppLog.class))).thenReturn(1);	
		//1.缺少所需请求参数
		Assert.assertEquals(0, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		
		//2.安全验证失败
		securityJson.put("Md5Str", "f38bf2fcc8ef2cf494e357a15000ddw3");
		paramMap.put("busiCode", BusiCodeConstant.APP_RECOMMEND);
		paramMap.put("number", "1");
		json.put("paramMap", paramMap);
		json.put("securityJson", securityJson);
		Assert.assertEquals(0, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		
		//3 app 推荐
		securityJson.put("Md5Str", "f38bf2fcc8ef2cf494e357a15000dd03");
		//初始化参数
		json.put("securityJson", securityJson);
		HashMap<String, String> userInfo = new HashMap<>();
		userInfo.put("professional","游戏");
		userInfo.put("industry","游戏");
		//mock appInfoMapper.queryUserProfessinal 方法
		Mockito.when(appInfoMapper.queryUserProfessinal(Mockito.anyString())).thenReturn(userInfo);
		List<AppType> tags = new ArrayList<>();
		AppType type0 = new AppType();
		type0.setProfessionalTag("输入法");
		type0.setIndustryTag("游戏大全1");
		tags.add(type0);
		//mock appTypeMapper.queryAllAppTags 方法
		Mockito.when(appTypeMapper.queryAllAppTags()).thenReturn(tags);
		List<HashMap<String, Object>> list = new ArrayList<>();
		HashMap<String, Object> map = new HashMap<>();
		list.add(map);
		list.add(map);
		//mock appInfoMapper.queryAppByTypes 方法
		Mockito.when(appInfoMapper.queryAppByTypes(Mockito.anyString())).thenReturn(list);
		//3.1
		Assert.assertEquals(1, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		
		paramMap.put("number", "3");
		json.put("paramMap", paramMap);
		List<AppType> tags1 = new ArrayList<>();
		AppType type1 = new AppType();
		type1.setProfessionalTag("游戏大全1");
		type1.setIndustryTag("输入法");
		tags1.add(type1);
		Mockito.when(appTypeMapper.queryAllAppTags()).thenReturn(tags1);
		//3.2
		Assert.assertEquals(1, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		
		//4.查询所有应用种类
		paramMap.put("busiCode", BusiCodeConstant.APP_QUERY_ALL_APP_TYPES);
		json.put("paramMap", paramMap);
		List<AppType> appTypes = new ArrayList<>();
		Mockito.when(appBusiService.queryAllAppTypes()).thenReturn(appTypes);
		Assert.assertEquals(1, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		//5.查询所有应用信息
		paramMap.put("busiCode", BusiCodeConstant.APP_QUERY_ALL_APPS);
		json.put("paramMap", paramMap);
		//5.1
		Assert.assertEquals(0, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		paramMap.put("typeId", "12");
		paramMap.put("currPage", "0");
		paramMap.put("pageNum", "2");
		json.put("paramMap", paramMap);
		Mockito.when(appInfoMapper.countAppByTypeId(Mockito.anyInt())).thenReturn(2);
		List<HashMap<String, Object>> apps = new ArrayList<>();
		Mockito.when(appInfoMapper.queryAppByType(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt() )).thenReturn(apps);
		//5.2
		Assert.assertEquals(1, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		//6.应用分页搜索
		paramMap.put("busiCode", BusiCodeConstant.APP_SEARCH_PAGINATION);
		json.put("paramMap", paramMap);
		//6.1
		Assert.assertEquals(0, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		paramMap.put("searchKey", "searchKey");
		json.put("paramMap", paramMap);
		Mockito.when(appInfoMapper.countAppBySearchKey(Mockito.anyString())).thenReturn(2);
		HashMap<String, Object> app = new HashMap<>();
		app.put("size", 1024);
		apps.add(app);
		HashMap<String, Object> app1 = new HashMap<>();
		app1.put("size", 102);
		apps.add(app1);
		Mockito.when(appInfoMapper.queryAppBySearchKey(Mockito.anyString(),Mockito.anyInt(),Mockito.anyInt() )).thenReturn(apps);
		//6.2
		Assert.assertEquals(1, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		//7.查询应用详情
		paramMap.put("busiCode", BusiCodeConstant.APP_QUERY_INFO);
		json.put("paramMap", paramMap);
		//7.1
		Assert.assertEquals(0, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		paramMap.put("appId", "1234567");
		json.put("paramMap", paramMap);
		Mockito.when(appInfoMapper.queryAppInfo(Mockito.anyString())).thenReturn(null);
		//7.2
		Assert.assertEquals(0, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		HashMap<Object, Object> info = new HashMap<>();
		info.put("size", 1024);
		Mockito.when(appInfoMapper.queryAppInfo(Mockito.anyString())).thenReturn(info);
		//7.3
		Assert.assertEquals(1, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		info.put("size", 102);
		//7.4
		Assert.assertEquals(1, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		//8.应用点赞
		paramMap.put("busiCode", BusiCodeConstant.APP_SAVE_APP_VOTE);
		json.put("paramMap", paramMap);
		Mockito.when(appBusiService.queryAppVoteId(Mockito.anyString(),Mockito.anyString())).thenReturn("");
		Mockito.when(appBusiService.saveAppVote(Mockito.any(AppVote.class))).thenReturn(1);
		Mockito.when(appBusiService.updateAppVoteCountByAppId(Mockito.anyString())).thenReturn(1);
		Mockito.when(appBusiService.queryAppVoteCountByAppId(Mockito.anyString())).thenReturn(34);
		//8.1
		Assert.assertEquals(1, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		paramMap.remove("appId");
		json.put("paramMap", paramMap);
		//8.2
		Assert.assertEquals(0, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		paramMap.put("appId", "1234567");
		json.put("paramMap", paramMap);
		Mockito.when(appBusiService.queryAppVoteId(Mockito.anyString(),Mockito.anyString())).thenReturn("123");
		//8.3
		Assert.assertEquals(2602, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		Mockito.when(appBusiService.queryAppVoteId(Mockito.anyString(),Mockito.anyString())).thenReturn("");
		Mockito.when(appBusiService.saveAppVote(Mockito.any(AppVote.class))).thenReturn(0);
		//8.4
		Assert.assertEquals(0, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		//9 时间去哪儿
		paramMap.put("busiCode", BusiCodeConstant.APP_USED_PERCENT);
		json.put("paramMap", paramMap);
		//9.1
		Assert.assertEquals(0, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		paramMap.put("maxId", 1);
		json.put("paramMap", paramMap);
		Mockito.when(appUsedPercentMapper.getMaxIdByUserId(Mockito.anyString())).thenReturn(1);
		//9.2
		Assert.assertEquals(1, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		List<HashMap<Object, Object>> perlist = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			HashMap<Object, Object> per = new HashMap<>();
			if (i==0) {
				per.put("type", "1");
			}else{
				per.put("type", "2");
			}
			perlist.add(per);			
		}
		Mockito.when(appUsedPercentMapper.getMaxIdByUserId(Mockito.anyString())).thenReturn(2);
		Mockito.when(systemTimeMapper.getSystemTime()).thenReturn(new Date());
		Mockito.when(appUsedPercentMapper.queryAppPercentOther(Mockito.anyString(),Mockito.anyString())).thenReturn(perlist);
		List<HashMap<Object, Object>> yesList = new ArrayList<>();
		Mockito.when(appUsedPercentMapper.queryAppPercentYes(Mockito.anyString(),Mockito.anyString())).thenReturn(yesList);
		//9.3
		Assert.assertEquals(1, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));		
		//9.热门资讯
		paramMap.put("busiCode", BusiCodeConstant.APP_HOTMESSAGE);
		json.put("paramMap", paramMap);
		paramMap.put("page", "1");
		paramMap.put("pagNum", "10");
		paramMap.put("createrTime", "2015-05-15 00:00:00");
		List<AppHotMessage> appHote = new ArrayList<AppHotMessage>();
		Mockito.when(appHotMessageMapper.queryHotMessage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(appHote);
		Mockito.when(appHotMessageMapper.queryHotMessageTime()).thenReturn("");
		Assert.assertEquals(0, appService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		//小美翻墙搜索
	}

}
