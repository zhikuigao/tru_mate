package com.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jws.app.operater.data.SearchConfigMapper;
import com.jws.app.operater.data.SearchMapsMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.SearchMaps;
import com.jws.app.operater.model.SearchMapsData;
import com.jws.app.operater.model.SearchMapsKeyword;
import com.jws.app.operater.service.SearchService;
import com.jws.app.operater.service.impl.SearchServiceImpl;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.util.JiveGlobe;

public class SearchServiceTest {
	
	@InjectMocks
	private SearchService searchService = new SearchServiceImpl();
	@Mock
	private SearchMapsMapper searchMapsMapper;
	@Mock 
	private SystemTimeMapper systemTimeMapper;
	@Mock 
	private SearchConfigMapper searchConfigMapper;
	@Mock 
	private JiveGlobe jiveGlobe;
	@Before
	public void setUp() throws Exception {
		  MockitoAnnotations.initMocks( this );
	}
	
	@Test
	public void testCommonEntry() {
		JSONObject json = new JSONObject();
		JSONObject paramMap = new JSONObject();
		paramMap.put("source", "busiSystem");
		paramMap.put("receive", "searchSystem");
		paramMap.put("language", "ZH");
		paramMap.put("userId", "ed90f33bd1c54470a89b883a959a0473");
		JSONObject securityJson = new JSONObject();
		securityJson.put("Md5Str", "8d5734dd5bae1c71526f50e5f578b709");
		securityJson.put("time", "2016-04-25 18:08:50");
		 json.put("securityJson", securityJson);
		json.put("paramMap",paramMap);
		
		 //时间校验
		Mockito.when(systemTimeMapper.getSystemTime()).thenReturn(new Date());
		
		//1.添加serachMaps数据
		paramMap.put("busiCode", BusiCodeConstant.ADDMAPS_SEARCH);
		paramMap.put("mapId", "1241241");
		paramMap.put("keyword", "测试");
		paramMap.put("keywordId", "214141");
		paramMap.put("title", "测试");
		paramMap.put("url", "www.baidu.com");
		paramMap.put("sourceSearch", "baidu");
		json.put("securityJson", securityJson);
		json.put("paramMap",paramMap);
		SearchMapsKeyword sds = new SearchMapsKeyword();
		sds.setKeyword("ceshi");
		Mockito.when(searchMapsMapper.querySearchMapsSameKeyWords(Mockito.anyString())).thenReturn(sds);
		Assert.assertEquals(1, searchService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		
		
		//2查询searchMaps历史记录
		paramMap.put("busiCode", BusiCodeConstant.QUERYHISTORY_MAPS);
		paramMap.put("key", "key");
		paramMap.put("page", "0");
		paramMap.put("pageNum", "10");
		json.put("paramMap",paramMap);
		SearchMaps ss = new SearchMaps();
		ss.setId("sgesgsegesges");
		ss.setUserId("sgeesgesg");
		ss.setFirstKeyword("segsges");
		SearchMapsKeyword sd = new SearchMapsKeyword();
		sd.setId("sgeseg");
		List<SearchMaps> qs = new ArrayList<SearchMaps>();
		List<SearchMapsKeyword> sks = new ArrayList<SearchMapsKeyword>();
		qs.add(ss);
		sks.add(sd);
		Mockito.when(searchMapsMapper.queryHistoryMaps(Mockito.anyString(),Mockito.anyString(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(qs);
		Mockito.when(searchMapsMapper.queryHistoryMapsCount(Mockito.anyString(),Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1);
		Mockito.when(searchMapsMapper.querySearchMapsKeyWords(Mockito.anyString())).thenReturn(sks);
		Assert.assertEquals(1, searchService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		
		//3searchMpas显示数据
		paramMap.put("busiCode", BusiCodeConstant.QUERYHISTORY_MAPSDATAS);
		paramMap.put("id", "324253");
		json.put("paramMap",paramMap);
		SearchMaps sm = new SearchMaps();
		List<SearchMapsKeyword> ls = new ArrayList<SearchMapsKeyword>();
		SearchMapsKeyword sk = new SearchMapsKeyword();
		ls.add(sk);
		Mockito.when(searchMapsMapper.queryMaps(Mockito.anyString())).thenReturn(sm);
		Mockito.when(searchMapsMapper.queryKeyWords(Mockito.anyString())).thenReturn(ls);
		Assert.assertEquals(1, searchService.commonEntry(json.toString()).getJSONObject("result").get("code"));

		//4searchMaps详情
		paramMap.put("busiCode", BusiCodeConstant.QUERQUERY_MAPKEYWORDDATAS);
		
		paramMap.put("keyWord", "324253");
		paramMap.put("page", "0");
		json.put("paramMap",paramMap);
		Mockito.when(searchMapsMapper.queryDatasSimple(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);
		Assert.assertEquals(1, searchService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		
		//5获取搜索源
//		paramMap.put("busiCode", BusiCodeConstant.SEARCH_SOURCE);
//		json.put("paramMap",paramMap);
//		Mockito.when(searchConfigMapper.queryAllSource(Mockito.anyString())).thenReturn(null);
//		Assert.assertEquals(1, searchService.commonEntry(json.toString()).getJSONObject("result").get("code"));
		
	}
	
	

}
