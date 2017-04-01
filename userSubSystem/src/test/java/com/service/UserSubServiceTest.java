package com.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jws.app.operater.data.CategoryRecMapper;
import com.jws.app.operater.data.EmailVerifyMapper;
import com.jws.app.operater.data.McPushContentMapper;
import com.jws.app.operater.data.SysNoticeMapper;
import com.jws.app.operater.data.SysUserLogMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.service.UserSubService;
import com.jws.app.operater.service.impl.UserSubServiceImpl;

public class UserSubServiceTest {
	
	@InjectMocks
	private UserSubService userSubService = new UserSubServiceImpl();
	@Mock 
	private SystemTimeMapper systemTimeMapper;
	@Mock
	private UserInfoMapper userInfoMapper;
	@Mock
	private EmailVerifyMapper emailVerifyMapper;
	@Mock
	private SysUserLogMapper sysUserLogMapper;
	@Mock
	private SysNoticeMapper sysNoticeMapper;
	@Mock
	private McPushContentMapper mcPushContentMapper;
	@Mock
	private CategoryRecMapper categoryRecMapper;
	
	@Before
	public void setUp() throws Exception {
		  MockitoAnnotations.initMocks( this );
	}


	@Test
	public void testAddNewUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddConfig() {
		fail("Not yet implemented");
	}

	@Test
	public void testVerifyIsValid() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateNewUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testSynUserToMC() {
		fail("Not yet implemented");
	}

	@Test
	public void testSendMsg() {
		fail("Not yet implemented");
	}

	@Test
	public void testBatchAddPushMsg() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectPushType() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectPushContentAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectPushInnovationType() {
		fail("Not yet implemented");
	}

}
