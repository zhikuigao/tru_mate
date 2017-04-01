package com.jws.app.operater.serviceBlock.user.service.impl;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.data.UserLoginMapper;
import com.jws.app.operater.model.UserLogin;
import com.jws.app.operater.serviceBlock.user.service.UserTokenService;
import com.jws.common.constant.ConfigConstants;
@Service("userTokenService")
public class UserTokenServiceImpl implements UserTokenService{

	@Resource
	private UserLoginMapper userLoginMapper;
	@Resource
	private SystemTimeMapper systemTimeMapper;
	
	@Override
	public Boolean validToken(String token, String device) {
		if (StringUtils.isEmpty(token)) {
			return false;
		}
		//1.查找最近一条记录，不存在就是无效的
		UserLogin login = new UserLogin();
		login.setLoginDevice(device);
		login.setToken(token);
		login.setValidity(true);
		UserLogin oldLog = userLoginMapper.queryLoginInfo(login);
		if (null==oldLog) {
			return false;
		}
		//2.根据登录时间判断是否失效
		Date curr= systemTimeMapper.getSystemTime();
		Date loginTime = oldLog.getCreateTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(loginTime);
		calendar.add(Calendar.DATE, ConfigConstants.LOGIN_EFFECTIVE_TIME);
		if ( curr.compareTo(calendar.getTime())>-1) {
			return false;
		}
		return true;
	}

}
