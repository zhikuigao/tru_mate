package com.service;
import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jws.app.operater.data.SysUserLogMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.model.SysUserLog;
import com.jws.app.operater.model.UserInfo;
import com.jws.app.operater.service.OtherService;
import com.jws.app.operater.service.UserBusiService;
import com.jws.app.operater.service.UserSubService;
import com.jws.app.operater.service.impl.UserServiceImpl;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.constant.UserEnum;

public class UserServiceTest {
	@InjectMocks
	private UserServiceImpl userService = new UserServiceImpl();
	@Mock
	private UserBusiService userBusiService;
	@Mock
	private  OtherService plugInService;
	@Mock
	private  OtherService messageService;
	@Mock
	private SystemTimeMapper systemTimeMapper;
	@Mock
	private SysUserLogMapper sysUserLogMapper;
	@Mock
	private UserSubService userSubService;
	@Mock
	private UserInfoMapper userInfoMapper;
	
	@Before
	public void setUp() throws Exception {
		  MockitoAnnotations.initMocks( this );
	}

	@Test
	public void testCommonEntry() {
		JSONObject param = new JSONObject();
		JSONObject paramMap = new JSONObject();
		paramMap.put("busiCode", BusiCodeConstant.USER_SEND_VERIFY_CODE);
		paramMap.put("source", "test");
		paramMap.put("language", "ZH");
		param.put("paramMap", paramMap);
		JSONObject securityJson = new JSONObject();
		securityJson.put("time", "2015-09-24 17:58:48");
		securityJson.put("Md5Str", "ba8ed8033359ce5768e3c217e086da");
		param.put("securityJson",securityJson);
		Mockito.when(sysUserLogMapper.userLogInsert(Mockito.any(SysUserLog.class))).thenReturn(1);
		Mockito.when(sysUserLogMapper.userLogUpdateByPrimaryKey(Mockito.any(SysUserLog.class))).thenReturn(1);
		//参数验证
		Assert.assertEquals(0, userService.commonEntry(param.toString()).getJSONObject("result").getInt("code"));
		
		//安全验证
		paramMap.put("device", "pc");
		param.put("paramMap", paramMap);
		Assert.assertEquals(0, userService.commonEntry(param.toString()).getJSONObject("result").getInt("code"));
		//发送验证码
		securityJson.put("Md5Str", "ba8ed8033359ce5768e3c217e086da13");
		param.put("securityJson",securityJson);
		Mockito.when(userBusiService.sendVerifyCode(Mockito.any(JSONObject.class))).thenReturn(null);
		Assert.assertNull(userService.commonEntry(param.toString()));
		
		paramMap.put("busiCode", BusiCodeConstant.USER_REGISTER);
		param.put("paramMap", paramMap);	
		Mockito.when(userBusiService.register(Mockito.any(JSONObject.class))).thenReturn(null);
		//注册
		Assert.assertNull(userService.commonEntry(param.toString()));
		
		paramMap.put("busiCode", BusiCodeConstant.USER_LOGIN);
		param.put("paramMap", paramMap);
		Mockito.when(userBusiService.login(Mockito.any(JSONObject.class))).thenReturn(null);
		//登录
		Assert.assertNull(userService.commonEntry(param.toString()));
		
//		paramMap.put("busiCode", BusiCodeConstant.SELECT_PUSH_CONTENTALL);
//		param.put("paramMap", paramMap);
//		Mockito.when(userSubService.selectPushContentAll(Mockito.any(JSONObject.class))).thenReturn(null);
		//查询所有消息
//		Assert.assertNull(userService.commonEntry(param.toString()));
		
//		paramMap.put("busiCode", BusiCodeConstant.SELECT_PUSH_INNOVATIONTYPE);
//		param.put("paramMap", paramMap);
//		Mockito.when(userSubService.selectPushInnovationType(Mockito.any(JSONObject.class))).thenReturn(null);
		//查询消息类型
//		Assert.assertNull(userService.commonEntry(param.toString()));
		
		paramMap.put("busiCode", BusiCodeConstant.VERSION_UPGRADE);
		paramMap.put("keyCode", UserEnum.User);
		param.put("paramMap", paramMap);
		Mockito.when(userBusiService.entry(Mockito.any(JSONObject.class))).thenReturn(null);
		//用户相关其他统一入口
		Assert.assertNull(userService.commonEntry(param.toString()));
		
		paramMap.put("keyCode", UserEnum.PlugIn);
		param.put("paramMap", paramMap);
		Mockito.when(plugInService.entry(Mockito.any(JSONObject.class))).thenReturn(null);
		//第三方插件
		Assert.assertNull(userService.commonEntry(param.toString()));
		
		paramMap.put("keyCode", UserEnum.Message);
		param.put("paramMap", paramMap);
		Mockito.when(messageService.entry(Mockito.any(JSONObject.class))).thenReturn(null);
		//消息
		Assert.assertNull(userService.commonEntry(param.toString()));
		
		//查询消息类型
		//个人消息
		paramMap.put("busiCode", BusiCodeConstant.QUERY_PERSON);
		paramMap.put("id", "79879");
		param.put("paramMap", paramMap);
	//	UserInfo ui = new UserInfo();
		Mockito.when(userInfoMapper.queryPerson(Mockito.anyString())).thenReturn(null);
		Assert.assertEquals(1122, userService.commonEntry(param.toString()).getJSONObject("result").getInt("code"));
		//改变消息
		paramMap.put("busiCode", BusiCodeConstant.UPDATE_PERSON);
		paramMap.put("professional", "");
		paramMap.put("industry", "");
		paramMap.put("sex", "");
		paramMap.put("birthday", "");
		paramMap.put("id", "325325");
		paramMap.put("nickname", "");
		param.put("paramMap", paramMap);
		Mockito.when(userInfoMapper.updatePerson(Mockito.any(UserInfo.class))).thenReturn(1);
		Assert.assertEquals(1, userService.commonEntry(param.toString()).getJSONObject("result").getInt("code"));
	}

	@Test
	public void testMonitorSysNotice() {
		Mockito.when(sysUserLogMapper.userLogInsert(Mockito.any(SysUserLog.class))).thenReturn(1);
		Mockito.when(sysUserLogMapper.userLogUpdateByPrimaryKey(Mockito.any(SysUserLog.class))).thenReturn(1);
		Mockito.when(userBusiService.monitorSysNotice()).thenReturn(null);
		userService.monitorSysNotice();
	}

	@Test
	public void testMonitorSysUpgrade() {
		Mockito.when(sysUserLogMapper.userLogInsert(Mockito.any(SysUserLog.class))).thenReturn(1);
		Mockito.when(sysUserLogMapper.userLogUpdateByPrimaryKey(Mockito.any(SysUserLog.class))).thenReturn(1);
		Mockito.when(userBusiService.monitorSysUpgrade()).thenReturn(null);
		userService.monitorSysUpgrade();
	}

	@Test
	public void testMonitorTimingPush() {
		Mockito.when(sysUserLogMapper.userLogInsert(Mockito.any(SysUserLog.class))).thenReturn(1);
		Mockito.when(sysUserLogMapper.userLogUpdateByPrimaryKey(Mockito.any(SysUserLog.class))).thenReturn(1);
		Mockito.when(userBusiService.monitorTimingPush()).thenReturn(null);
		userService.monitorTimingPush();
	}

	@Test
	public void testPushTimingData() {
		Mockito.when(sysUserLogMapper.userLogInsert(Mockito.any(SysUserLog.class))).thenReturn(1);
		Mockito.when(sysUserLogMapper.userLogUpdateByPrimaryKey(Mockito.any(SysUserLog.class))).thenReturn(1);
		Mockito.when(userBusiService.monitorPushData()).thenReturn(null);
		userService.pushTimingData();
	}

	@Test
	public void testPushFailData() {
		Mockito.when(sysUserLogMapper.userLogInsert(Mockito.any(SysUserLog.class))).thenReturn(1);
		Mockito.when(sysUserLogMapper.userLogUpdateByPrimaryKey(Mockito.any(SysUserLog.class))).thenReturn(1);
		Mockito.when(userBusiService.monitorFailData()).thenReturn(null);
		userService.pushFailData();
	}

}
