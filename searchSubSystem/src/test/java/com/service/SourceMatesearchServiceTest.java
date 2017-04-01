package com.service;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.jws.app.operater.data.SearchConfigMapper;
import com.jws.app.operater.data.SourceMatesearchMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.service.SourceMatesearchService;
import com.jws.app.operater.service.impl.SourceMatesearchServiceImpl;
import com.jws.common.constant.BusiCodeConstant;

public class SourceMatesearchServiceTest {
	@InjectMocks
	private SourceMatesearchService  sourceMatesearchService = new SourceMatesearchServiceImpl();
	@Mock 
	private SystemTimeMapper systemTimeMapper;
	@Mock 
	private SearchConfigMapper searchConfigMapper;
	@Mock 
	private SourceMatesearchMapper sourceMatesearchMapper;
	
	@Before
	public void setUp() throws Exception {
		  MockitoAnnotations.initMocks( this );
	}
	
	@Test
	public void testEntry() throws Exception {
		JSONObject param = new JSONObject();
		//1.无对应的code
		Assert.assertNull(sourceMatesearchService.entry(param));
		
		//2、增加源
		param.put("busiCode", BusiCodeConstant.ADD_MATE_SOURCE);
		param.put("name", "ceshi");
		param.put("url", "ceshi");
		param.put("type", "1");
		param.put("userId", "235253");
		Assert.assertEquals(1, sourceMatesearchService.entry(param).getJSONObject("result").get("code"));
		
		//2、删除源
		param.put("busiCode", BusiCodeConstant.DELETE_MATE_SOURCE);
		
		param.put("id", "ceshi");
		Assert.assertEquals(1, sourceMatesearchService.entry(param).getJSONObject("result").get("code"));
		
		//3、查询源
		param.put("busiCode", BusiCodeConstant.QUERY_MATE_SOURCE);
		param.put("userId", "2352");
		Assert.assertEquals(1, sourceMatesearchService.entry(param).getJSONObject("result").get("code"));
	
	}

}
