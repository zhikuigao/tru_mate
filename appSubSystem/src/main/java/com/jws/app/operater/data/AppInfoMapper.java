package com.jws.app.operater.data;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppInfoMapper {
	
	/**
	 * 根据多个类目查询应用
	 * @param ids
	 * @return
	 */
	List<HashMap<String, Object>> queryAppByTypes(@Param("typeIds")String typeIds);
	/**
	 * 查询所有应用
	 * @param map
	 * @return
	 */
	List<HashMap<String, Object>> queryAllApp();
	/**
	 * 根据类别查询app
	 * @return
	 */
	List<HashMap<String, Object>> queryAppByType(@Param("typeId")Integer typeId,@Param("pageFrom")Integer pageFrom,@Param("pageNum")Integer pageNum);
	/**
	 * 根据关键字查询app
	 * @return
	 */
	List<HashMap<String, Object>> queryAppBySearchKey(@Param("searchKey")String searchKey,@Param("pageFrom")Integer pageFrom,@Param("pageNum")Integer pageNum);
	
	/**
	 * 查询用户的职业
	 * @param userId
	 * @return
	 */
	HashMap<String, String> queryUserProfessinal(@Param("userId")String userId);
	/**
	 * 查询应用详情
	 * @param id
	 * @return
	 */
	HashMap<Object, Object> queryAppInfo(@Param("id")String id);
	/**
	 * 统计关键词搜索结果总数
	 * @param searchKey
	 * @return
	 */
	int countAppBySearchKey(@Param("searchKey")String searchKey);
	
	/**
	 * 根据typeId统计总数
	 * @param typeId
	 * @return
	 */
	int countAppByTypeId(@Param("typeId") Integer typeId);

	/**
	 * 根据appId更新点赞数
	 * @param appId
	 */
	int updateAppVoteCountByAppId(@Param("appId") String appId);

	/**
	 * 根据appId获取点赞数
	 * @param appId
	 * @return
	 */
	int queryAppVoteCountByAppId(@Param("appId") String appId);
}