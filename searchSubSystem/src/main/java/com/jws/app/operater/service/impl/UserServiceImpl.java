package com.jws.app.operater.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jws.app.operater.data.UserInfoExtendMapper;
import com.jws.app.operater.model.UserInfoExtend;
import com.jws.app.operater.service.UserService;
import com.jws.common.cache.BaseCache;
import com.jws.common.constant.Constants;
import com.opensymphony.oscache.base.NeedsRefreshException;
@Service("userService")
public class UserServiceImpl implements UserService {
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private UserInfoExtendMapper userInfoExtendMapper;
	
	private BaseCache cache = new BaseCache("userExtend",864000);

	@Override
	public Boolean synUserMateVersion(String mateVersion, String Device,
			String userId) {
		UserInfoExtend extend = new UserInfoExtend();
		extend.setUserId(userId);
		if (StringUtils.equals(Device, Constants.DEVICE_PC)) {
			extend.setVersionPc(mateVersion);
		}
		if (StringUtils.equals(Device, Constants.DEVICE_ANDROID)) {
			extend.setVersionAndroid(mateVersion);
		}
		if (StringUtils.equals(Device, Constants.DEVICE_IOS)) {
			extend.setVersionIos(mateVersion);
		}
		//从缓存中获取对象信息
		Object object = null;
		try {
			object = cache.get(userId);
		} catch (NeedsRefreshException e) {
			//用户扩展信息未加入缓存时会进入此处异常
//			logger.error("未找到用户扩展缓存信息");
		}
		//找到了缓存
		if (null !=object && object instanceof UserInfoExtend && StringUtils.equals(((UserInfoExtend)object).getUserId(), userId) ) {
			//缓存中的对象extendCache，比较版本号
			UserInfoExtend extendCache =(UserInfoExtend)object;
			if (StringUtils.equals(Device, Constants.DEVICE_PC)) {
				if (StringUtils.equals(extendCache.getVersionPc(), mateVersion)) {
					//同缓存中的版本号一致，直接返回
					return true;
				}else{
					extendCache.setVersionPc(mateVersion);
				}
			}
			if (StringUtils.equals(Device, Constants.DEVICE_ANDROID)) {
				if (StringUtils.equals(extendCache.getVersionAndroid(), mateVersion)) {
					return true;
				}else{
					extendCache.setVersionAndroid(mateVersion);
				}
			}
			if (StringUtils.equals(Device, Constants.DEVICE_IOS)) {
				if (StringUtils.equals(extendCache.getVersionIos(), mateVersion)) {
					return true;
				}else{
					extendCache.setVersionIos(mateVersion);
				}
			}
			try {
				//同缓存中的版本号不一致，更新最新数据
				userInfoExtendMapper.updateUserExtend(extend);
				//更新缓存
				cache.remove(userId);
				cache.put(userId, extendCache);
			} catch (Exception e) {
				logger.error("同步用户现使用的小美版本信息异常2"+e.getMessage());
				return false;
			}
		}else{
			try {
				//找不到缓存, 查询表记录
				UserInfoExtend oldExtend = userInfoExtendMapper.queryUserExtend(userId);
				//表中不存在数据，直接新增
				if (null == oldExtend || StringUtils.isEmpty(oldExtend.getUserId())) {
					userInfoExtendMapper.insertUserExtend(extend);
				}else{
					//与表记录比较版本号，一致，直接返回
					if (StringUtils.equals(Device, Constants.DEVICE_PC)) {
						if (StringUtils.equals(mateVersion, oldExtend.getVersionPc())) {
							cache.put(userId, extend);
							return true;
						}
					}
					if (StringUtils.equals(Device, Constants.DEVICE_ANDROID)) {
						if (StringUtils.equals(mateVersion, oldExtend.getVersionAndroid())) {
							cache.put(userId, extend);
							return true;
						}
					}
					if (StringUtils.equals(Device, Constants.DEVICE_IOS)) {
						if (StringUtils.equals(mateVersion, oldExtend.getVersionIos())) {
							cache.put(userId, extend);
							return true;
						}
					}
					//将最新版本记录同步到表中
					userInfoExtendMapper.updateUserExtend(extend);
				}
				//添加缓存
				cache.put(userId, extend);
			} catch (Exception e) {
				logger.error("同步用户现使用的小美版本信息异常1"+e.getMessage());
				return false;
			}
		}
		return true;
	}

}
