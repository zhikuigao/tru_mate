package com.jws.app.operater.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.apache.log4j.Logger;
import com.jws.app.operater.data.UserLoginMapper;
import com.jws.app.operater.model.UserLogin;
import com.jws.app.operater.service.UserTokenService;
@Service("userTokenService")
public class UserTokenServiceImpl implements UserTokenService{
	
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private UserLoginMapper userLoginMapper;
	
	@Override
	public String addToken(String userId, String device) {
		//1.将同设备上所有登录有效性设置为false
		UserLogin login = new UserLogin();
		login.setUserId(userId);
		login.setLoginDevice(device);
		login.setValidity(false);
		login.setValidityOld(true);
		try {
			userLoginMapper.updateLogin(login);
		} catch (Exception e) {
			logger.error("更新登录记录失败:"+e.getMessage());
		}
		//2.添加新的登录历史token记录
		String token=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession().getId();
		login.setValidity(true);
		login.setToken(token);
		try {
			userLoginMapper.insert(login);
		} catch (Exception e) {
			logger.error("新增登录记录失败:"+e.getMessage());
		}
		return token;
	}

}
