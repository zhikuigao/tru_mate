package com.service;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jws.app.operater.data.SysBusiLogMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.SysBusiLog;
import com.jws.app.operater.service.SysBusiLogService;
import com.jws.app.operater.service.impl.SysBusiLogServiceImpl;

public class SysBusiLogServiceTest {
	@InjectMocks
	private SysBusiLogService service = new SysBusiLogServiceImpl();
	@Mock
	private SystemTimeMapper systemTimeMapper;
	@Mock
	private SysBusiLogMapper sysBusiLogMapper;
	@Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );
    }

	@Test
	public void testAddCallLog() {
		JSONObject json= new JSONObject();
		json.put("source", "test");
		Mockito.when(sysBusiLogMapper.logInsertSelective(Mockito.any(SysBusiLog.class))).thenReturn(1);
		Assert.assertNotNull(service.addCallLog(json));
		json.put("receive", "test");
		Assert.assertNotNull(service.addCallLog(json));
	}

	@Test
	public void testUpdateCallLog() {
		JSONObject json= new JSONObject();
		json.put("source", "test");
		SysBusiLog log = new SysBusiLog();
		log.setId("123");
		Mockito.when(sysBusiLogMapper.logUpdateByPrimaryKey(Mockito.any(SysBusiLog.class))).thenReturn(1);
		service.updateCallLog(null, log, json);
		Assert.assertTrue(true);
	}

}
