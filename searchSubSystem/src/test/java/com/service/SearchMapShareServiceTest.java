package com.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jws.app.operater.data.SearchMapShareMapper;
import com.jws.app.operater.data.SearchMapsMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.SearchMapShare;
import com.jws.app.operater.model.SearchMaps;
import com.jws.app.operater.service.SearchMapShareService;
import com.jws.app.operater.service.impl.SearchMapShareServiceImpl;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.constant.Constants;

public class SearchMapShareServiceTest {
	
	@InjectMocks
	private SearchMapShareService searchMapShareService = new SearchMapShareServiceImpl();
	@Mock
	private SearchMapShareMapper searchMapShareMapper;
	@Mock
	private SearchMapsMapper searchMapsMapper;
	@Mock
	private SystemTimeMapper systemTimeMapper;
	
	@Before
	public void setUp() throws Exception {
		  MockitoAnnotations.initMocks( this );
	}

	@Test
	public void testEntry() throws Exception {
		JSONObject param = new JSONObject();
		//1.无对应的code
		Assert.assertNull(searchMapShareService.entry(param));
		//2.保存搜索分享
		param.put("busiCode", BusiCodeConstant.SAVE_SEARCH_SHARE);
		//缺少请求参数
		Assert.assertEquals(0, searchMapShareService.entry(param).getJSONObject("result").get("code"));
		//用户不存在
		param.put("userId", "123");
		param.put("mapId", "456");
		param.put("shareTo", "qq");
		param.put("picUrl", "/busisystem/mate/share/3453234.png");
		Mockito.when(systemTimeMapper.existUser(Mockito.anyString())).thenReturn(0);
		Assert.assertEquals(0, searchMapShareService.entry(param).getJSONObject("result").get("code"));
		//地图信息不存在
		Mockito.when(systemTimeMapper.existUser(Mockito.anyString())).thenReturn(1);
		Mockito.when(searchMapsMapper.queryMapById(Mockito.anyString())).thenReturn(null);
		Assert.assertEquals(0, searchMapShareService.entry(param).getJSONObject("result").get("code"));
		//保存成功
		SearchMaps map = new SearchMaps();
		map.setFirstKeyword("test");
		Mockito.when(searchMapsMapper.queryMapById(Mockito.anyString())).thenReturn(map);
		Mockito.when(searchMapShareMapper.insert(Mockito.any(SearchMapShare.class))).thenReturn(1);
		Assert.assertEquals(1, searchMapShareService.entry(param).getJSONObject("result").get("code"));
		param.put("device", Constants.DEVICE_PC);
		Assert.assertEquals(1, searchMapShareService.entry(param).getJSONObject("result").get("code"));
		param.put("device", Constants.DEVICE_ANDROID);
		Assert.assertEquals(1, searchMapShareService.entry(param).getJSONObject("result").get("code"));
		
		//3.查询搜索分享
		JSONObject param1 = new JSONObject();
		param1.put("busiCode", BusiCodeConstant.QUERY_SEARCH_SHARES);
		//缺少请求参数
		Assert.assertEquals(0, searchMapShareService.entry(param1).getJSONObject("result").get("code"));
		//page>0
		param1.put("userId", "123");
		param1.put("pageNum", "3");
		param1.put("page", "1");
		param1.put("firstTime", "2016-07-05 17:20:49");
		Mockito.when(searchMapShareMapper.getTotalNumber(Mockito.anyString(),Mockito.any(Date.class))).thenReturn(1);
		List<SearchMapShare> list = new ArrayList<>();
		Mockito.when(searchMapShareMapper.queryShareByPage(Mockito.any(SearchMapShare.class))).thenReturn(list);
		Assert.assertEquals(1, searchMapShareService.entry(param1).getJSONObject("result").get("code"));
		//page == 0
		param1.put("page", "-1");
		Mockito.when(systemTimeMapper.getSystemTime()).thenReturn(new Date());
		SearchMapShare share = new SearchMapShare();
		share.setThumbnailUrl("/mate/share/232.png");
		list.add(share);
		Mockito.when(searchMapShareMapper.queryShareByPage(Mockito.any(SearchMapShare.class))).thenReturn(list);
		Assert.assertEquals(1, searchMapShareService.entry(param1).getJSONObject("result").get("code"));
	}

}
