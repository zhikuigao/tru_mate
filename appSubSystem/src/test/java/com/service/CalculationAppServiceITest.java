package com.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jws.app.operater.data.AppUsedPercentMapper;
import com.jws.app.operater.data.SysAppLogMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.AppUsedYesterday;
import com.jws.app.operater.model.SysAppLog;
import com.jws.app.operater.service.impl.CalculationAppServiceImpl;
import com.jws.common.constant.Constants;

public class CalculationAppServiceITest {
	@InjectMocks
	private CalculationAppServiceImpl calcu = new CalculationAppServiceImpl();
	@Mock
	private SystemTimeMapper systemTimeMapper;
	@Mock
	private AppUsedPercentMapper appUsedPercentMapper;
	@Mock
	private SysAppLogMapper sysAppLogMapper;
	
	@Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );
    }

	@SuppressWarnings("unchecked")
//	@Test
	public void testCalculationYes() throws ParseException {
		Date curr = new Date();
		Mockito.when(systemTimeMapper.getSystemTime()).thenReturn(curr);
		Mockito.when(sysAppLogMapper.logInsert(Mockito.mock(SysAppLog.class))).thenReturn(1);
		List<AppUsedYesterday> list = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			AppUsedYesterday yes = new AppUsedYesterday();
			yes.setAppName("name"+i);
			yes.setDayUse(Constants.dfd.parse(Constants.dfd.format(curr)));
			if (i<7) {
				yes.setUserId("1");
				yes.setDuration(Integer.valueOf(1800*i));
			}else{
				yes.setUserId("2");
				yes.setDuration(Integer.valueOf(400*i));
			}
			list.add(yes);
		}
		Mockito.when(appUsedPercentMapper.queryAppUsedYes(Mockito.anyString(), Mockito.anyString())).thenReturn(list);
		Mockito.when(appUsedPercentMapper.batchInsertAppUsedPercent(Mockito.anyList())).thenReturn(1);
		Mockito.when(sysAppLogMapper.logUpdateByPrimaryKey(Mockito.mock(SysAppLog.class))).thenReturn(1);
		try {
			calcu.calculationYes();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.toString().contains("RuntimeException"));
		}
	}

//	@Test
	@SuppressWarnings("unchecked")
	public void testCalculationWeek() throws ParseException {
		Date curr = new Date();
		Mockito.when(systemTimeMapper.getSystemTime()).thenReturn(curr);
		Mockito.when(sysAppLogMapper.logInsert(Mockito.mock(SysAppLog.class))).thenReturn(1);
		List<AppUsedYesterday> list = new ArrayList<>();
		for (int i = 40; i > 0; i--) {
			AppUsedYesterday yes = new AppUsedYesterday();
			yes.setAppName("name"+i%9);
			yes.setDayUse(Constants.dfd.parse(Constants.dfd.format(curr)));
			if (i<37) {
				yes.setUserId("1");
				yes.setDuration(Integer.valueOf(1800*i));
			}else{
				yes.setUserId("2");
				yes.setDuration(Integer.valueOf(400*i));
			}
			list.add(yes);
		}
		Mockito.when(appUsedPercentMapper.queryAppUsedYes(Mockito.anyString(), Mockito.anyString())).thenReturn(list);
		Mockito.when(appUsedPercentMapper.batchInsertAppUsedPercent(Mockito.anyList())).thenReturn(1);
		Mockito.when(sysAppLogMapper.logUpdateByPrimaryKey(Mockito.mock(SysAppLog.class))).thenReturn(1);
		try {
			calcu.calculationWeek();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.toString().contains("RuntimeException"));
		}
	}

//	@Test
	@SuppressWarnings("unchecked")
	public void testCalculationMonth() throws ParseException {
		Date curr = new Date();
		Mockito.when(systemTimeMapper.getSystemTime()).thenReturn(curr);
		Mockito.when(sysAppLogMapper.logInsert(Mockito.mock(SysAppLog.class))).thenReturn(1);
		List<AppUsedYesterday> list = new ArrayList<>();
		for (int i = 100; i > 0; i--) {
			AppUsedYesterday yes = new AppUsedYesterday();
			yes.setAppName("name"+i%12);
			yes.setDayUse(Constants.dfd.parse(Constants.dfd.format(curr)));
			if (i<37) {
				yes.setUserId("1");
				yes.setDuration(Integer.valueOf(1800*i));
			}else{
				yes.setUserId("2");
				yes.setDuration(Integer.valueOf(400*i));
			}
			list.add(yes);
		}
		Mockito.when(appUsedPercentMapper.queryAppUsedYes(Mockito.anyString(), Mockito.anyString())).thenReturn(list);
		Mockito.when(appUsedPercentMapper.batchInsertAppUsedPercent(Mockito.anyList())).thenReturn(1);
		Mockito.when(sysAppLogMapper.logUpdateByPrimaryKey(Mockito.mock(SysAppLog.class))).thenReturn(1);
		try {
			calcu.calculationMonth();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.toString().contains("RuntimeException"));
		}
	}

}
