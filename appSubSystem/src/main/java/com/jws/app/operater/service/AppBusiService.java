package com.jws.app.operater.service;

import java.util.List;

import com.jws.app.operater.model.AppType;
import com.jws.app.operater.model.AppVersion;
import com.jws.app.operater.model.AppVote;
import com.jws.common.exception.ServiceException;

public interface AppBusiService {
	

	/**
	 * 查询所有应用种类
	 * @return		
	 * @throws ServiceException 
	 */
	public List<AppType> queryAllAppTypes() throws ServiceException;

	/**
	 * 查询应用详情
	 * @param appId
	 * @param versionId
	 * @return
	 * @throws ServiceException
	 */
	public List<AppVersion> queryAppVersions(String appId, String versionId) throws ServiceException;
	
	/**
	 * 应用点赞
	 * @param appVote
	 * @throws ServiceException
	 */
	public int saveAppVote(AppVote appVote);
	
	/**
	 * 查询应用
	 * @param appId
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public String queryAppVoteId(String appId, String userId) throws ServiceException;

	/**
	 * 根据appId更新点赞数
	 * @param appId
	 * @throws ServiceException
	 */
	public int updateAppVoteCountByAppId(String appId) throws ServiceException;

	/**
	 * 根据appId获取点赞数
	 * @param appId
	 * @return
	 * @throws ServiceException
	 */
	public int queryAppVoteCountByAppId(String appId) throws ServiceException;

}
