package com.jws.app.operater.service;

import java.util.List;
import net.sf.json.JSONObject;
import com.jws.app.operater.model.SearchViewedBy;

public interface SearchBusiService {
	
	/**
	 * 3.1	保存搜索条件
	 * @param key
	 * @throws ServiceException
	 */
//	public void saveSearchKey(SearchKey key) throws ServiceException;
	
	/**
	 * 3.2	保存查看链接
	 * @param viewed
	 * @throws ServiceException
	 */
//	public void saveSearchViewed(SearchViewed viewed) throws ServiceException;
	
	/**
	 * 3.3	查询用户搜索历史
	 * @param userId
	 * @param keyWord
	 * @param currPage
	 * @param pageNum
	 * @return
	 * @throws ServiceException
	 */
//	public List<SearchKey> querySearchKey(String userId, String keyWord, int currPage, int pageNum) throws ServiceException;	

	/**
	 * 3.4	查询搜索条件对应的所有链接
	 * @param userId
	 * @param keyWord
	 * @param currPage
	 * @param pageNum
	 * @return
	 * @throws ServiceException
	 */
//	public List<SearchViewed> querySearchViewed(String userId, String keyWord, int currPage, int pageNum) throws ServiceException;
	
	/**
	 * 统计历史搜索关键字
	 * @param userId
	 * @param keyWord
	 * @return
	 * @throws ServiceException
	 */
//	public int countKeyHistory(String userId, String keyWord) throws ServiceException;
	
	/**
	 * 根据userId/keyWord统计历史查看链接
	 * @param userId
	 * @param keyWord
	 * @return
	 * @throws ServiceException
	 */
//	public int countViewedHistory(String userId, String keyWord) throws ServiceException;

	/**
	 * 批量插入SearchKey
	 * @param searchKeys
	 * @return
	 * @throws ServiceException
	 */
//	public int saveSearchKeys(List<SearchKey> searchKeys) throws ServiceException;

	/**
	 * 批量插入SearchViewed
	 * @param searchVieweds
	 * @return
	 * @throws ServiceException
	 */
//	public int saveSearchVieweds(List<SearchViewed> searchVieweds) throws ServiceException;
	/**
	 * 查询搜索推荐信息
	 * @param param
	 * @return
	 */
	public List<SearchViewedBy> querySearchRecommend(JSONObject param);
	
}
