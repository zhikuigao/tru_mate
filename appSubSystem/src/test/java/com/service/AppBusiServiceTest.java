package com.service;

import java.util.HashMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.jws.app.operater.data.AppInfoMapper;
import com.jws.app.operater.data.AppTypeMapper;
import com.jws.app.operater.data.AppVersionMapper;
import com.jws.app.operater.data.AppVoteMapper;
import com.jws.app.operater.model.AppVote;
import com.jws.app.operater.service.AppBusiService;
import com.jws.app.operater.service.impl.AppBusiServiceImpl;
import com.jws.common.exception.ServiceException;

public class AppBusiServiceTest {
	@InjectMocks
	private AppBusiService app = new AppBusiServiceImpl();
	@Mock
	private AppInfoMapper appInfoMapper;
	@Mock
	private AppTypeMapper appTypeMapper;
	@Mock
	private AppVersionMapper appVersionMapper;
	@Mock
	private AppVoteMapper appVoteMapper;
	
	@Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks( this );
    }

	@Test
	public void testQueryAllAppTypes() throws ServiceException {
		Mockito.when(appTypeMapper.queryAllAppTypes()).thenReturn(null);
		Assert.assertNull(app.queryAllAppTypes());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testQueryAppVersions() throws ServiceException {
		Mockito.when(appVersionMapper.queryAppVersions(Mockito.any(HashMap.class))).thenReturn(null);
		Assert.assertNull(app.queryAppVersions("123", ""));
		Assert.assertNull(app.queryAppVersions("123", "latest"));
	}

	@Test
	public void testSaveAppVote() {
		Mockito.when(appVoteMapper.saveAppVote(Mockito.any(AppVote.class))).thenReturn(1);
		Assert.assertEquals(1,app.saveAppVote(null));
	}

	@Test
	public void testQueryAppVoteId() throws ServiceException {
		Mockito.when(appVoteMapper.queryAppVoteId(Mockito.any(String.class),Mockito.any(String.class))).thenReturn("123");
		Assert.assertEquals("123",app.queryAppVoteId("123","456"));
	}

	@Test
	public void testUpdateAppVoteCountByAppId() throws ServiceException {
		Mockito.when(appInfoMapper.updateAppVoteCountByAppId(Mockito.any(String.class))).thenReturn(1);
		Assert.assertEquals(1,app.updateAppVoteCountByAppId("123"));
	}

	@Test
	public void testQueryAppVoteCountByAppId() throws ServiceException {
		Mockito.when(appInfoMapper.queryAppVoteCountByAppId(Mockito.any(String.class))).thenReturn(1);
		Assert.assertEquals(1,app.queryAppVoteCountByAppId("123"));
	}

}
