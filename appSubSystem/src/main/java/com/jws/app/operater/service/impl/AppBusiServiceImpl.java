package com.jws.app.operater.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.jws.app.operater.data.AppInfoMapper;
import com.jws.app.operater.data.AppTypeMapper;
import com.jws.app.operater.data.AppVersionMapper;
import com.jws.app.operater.data.AppVoteMapper;
import com.jws.app.operater.model.AppType;
import com.jws.app.operater.model.AppVersion;
import com.jws.app.operater.model.AppVote;
import com.jws.app.operater.service.AppBusiService;
import com.jws.common.exception.ServiceException;

@Service("appBusiService")
public class AppBusiServiceImpl implements AppBusiService {

	@Resource
	private AppInfoMapper appInfoMapper;
	@Resource
	private AppTypeMapper appTypeMapper;
	@Resource
	private AppVersionMapper appVersionMapper;
	@Resource
	private AppVoteMapper appVoteMapper;
	
	@Override
	public List<AppType> queryAllAppTypes() throws ServiceException {
		return appTypeMapper.queryAllAppTypes();
	}

	@Override
	public List<AppVersion> queryAppVersions(String appId, String versionId) throws ServiceException {

		// latest：获取最新版本; 空串""：获取所有版本; 实际id：获取指定版本
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("appId", appId);
		if (StringUtils.isEmpty(versionId)) {
			versionId = null;
		} else if(StringUtils.equals(versionId, "latest")) {
			versionId = null;
			paramMap.put("limit", 1);
		}

		paramMap.put("versionId", versionId);
		
		return appVersionMapper.queryAppVersions(paramMap);
	}

	@Override
	public int saveAppVote(AppVote appVote) {
		return appVoteMapper.saveAppVote(appVote);
	}

	@Override
	public String queryAppVoteId(String appId, String userId) throws ServiceException {
		return appVoteMapper.queryAppVoteId(appId, userId);
	}

	@Override
	public int updateAppVoteCountByAppId(String appId) throws ServiceException {
		return appInfoMapper.updateAppVoteCountByAppId(appId);
	}
	
	@Override
	public int queryAppVoteCountByAppId(String appId) throws ServiceException {
		return appInfoMapper.queryAppVoteCountByAppId(appId);
	}
	
}
