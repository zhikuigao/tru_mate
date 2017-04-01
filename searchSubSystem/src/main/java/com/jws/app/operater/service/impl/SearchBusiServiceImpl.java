package com.jws.app.operater.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import com.jws.app.operater.data.SearchKeyMapper;
import com.jws.app.operater.data.SearchViewedMapper;
import com.jws.app.operater.model.SearchInfoAll;
import com.jws.app.operater.model.SearchViewedBy;
import com.jws.app.operater.service.SearchBusiService;
import com.jws.common.util.Levenshtein;

@Service("searchBusiService")
public class SearchBusiServiceImpl implements SearchBusiService {

	@Resource
	private SearchKeyMapper searchKeyMapper;
	@Resource
	private SearchViewedMapper searchViewedMapper;
	
	/* (non-Javadoc)
	 * @see com.jws.app.operater.service.SearchBusiService#saveSearchKey(com.jws.app.operater.model.SearchKey)
	 */
//	@Override
//	public void saveSearchKey(SearchKey key) throws ServiceException {
//		searchKeyMapper.saveSearchKey(key);
//	}

	/* (non-Javadoc)
	 * @see com.jws.app.operater.service.SearchBusiService#querySearchKey(java.lang.String, java.lang.String, int, int)
	 */
//	@Override
//	public List<SearchKey> querySearchKey(String userId, String keyWord, int currPage, int pageNum) throws ServiceException {
//		HashMap<String, Object> paramMap = new HashMap<>();
//		paramMap.put("userId", userId);
//		paramMap.put("keyWord", keyWord);
//		paramMap.put("pageFrom", pageNum * currPage);
//		paramMap.put("pageNum", pageNum);
//		return searchKeyMapper.querySearchKeyByUserId(paramMap);
//	}

	/* (non-Javadoc)
	 * @see com.jws.app.operater.service.SearchBusiService#countKeyHistory(java.lang.String, java.lang.String)
	 */
//	@Override
//	public int countKeyHistory(String userId, String keyWord) throws ServiceException {
//		return searchKeyMapper.countKeyHistory(userId, keyWord);
//	}

	/* (non-Javadoc)
	 * @see com.jws.app.operater.service.SearchBusiService#saveSearchViewed(com.jws.app.operater.model.SearchViewed)
	 */
//	@Override
//	public void saveSearchViewed(SearchViewed viewed) throws ServiceException {
//		searchViewedMapper.saveSearchViewed(viewed);
//	}

	/* (non-Javadoc)
	 * @see com.jws.app.operater.service.SearchBusiService#querySearchViewed(java.lang.String, java.lang.String, int, int)
	 */
//	@Override
//	public List<SearchViewed> querySearchViewed(String userId, String keyWord, int currPage, int pageNum) throws ServiceException {
//		HashMap<String, Object> paramMap = new HashMap<>();
//		paramMap.put("userId", userId);
//		paramMap.put("keyWord", keyWord);
//		paramMap.put("pageFrom", pageNum * currPage);
//		paramMap.put("pageNum", pageNum);
//		return searchViewedMapper.querySearchViewed(paramMap);
//	}

	/* (non-Javadoc)
	 * @see com.jws.app.operater.service.SearchBusiService#countViewedHistory(java.lang.String, java.lang.String)
	 */
//	@Override
//	public int countViewedHistory(String userId, String keyWord) throws ServiceException {
//		return searchViewedMapper.countViewedHistory(userId, keyWord);
//	}

	/* (non-Javadoc)
	 * @see com.jws.app.operater.service.SearchBusiService#saveSearchKeys(java.util.List)
	 */
//	@Override
//	public int saveSearchKeys(List<SearchKey> searchKeys) throws ServiceException {
//		return searchKeyMapper.saveSearchKeys(searchKeys);
//	}

	/* (non-Javadoc)
	 * @see com.jws.app.operater.service.SearchBusiService#saveSearchVieweds(java.util.List)
	 */
//	@Override
//	public int saveSearchVieweds(List<SearchViewed> searchVieweds) throws ServiceException {
//		return searchViewedMapper.saveSearchVieweds(searchVieweds);
//	}

	@Override
	public List<SearchViewedBy> querySearchRecommend(JSONObject param) {
		List<SearchViewedBy> list= new ArrayList<>();
		if (param.has("userId")) {
			String userId = param.getString("userId");
			List<SearchInfoAll> views = searchViewedMapper.queryRecommendSearchHis(userId);
			if (null != views && views.size()>0) {
				int size = views.size();
				for (int i = 0; i < size; i++) {
					//获取相似度
					Float simvalue=Levenshtein.levenshtein(param.getString("searchKey"), views.get(i).getTitleTxt());
					if (simvalue>0.2) {
						SearchViewedBy view = new SearchViewedBy(views.get(i),simvalue);
						list.add(view);
					}
				}
			}
		}
		if (!param.has("userId") || list.size()==0) {
			List<SearchInfoAll> views = searchViewedMapper.queryRecommendSearchHis(null);
			if (null != views && views.size()>0) {
				int size = views.size();
				for (int i = 0; i < size; i++) {
//					if (param.has("userId") && StringUtils.equals(param.getString("userId"), views.get(i).getUserId())) {
//						continue;
//					}
					//获取相似度
					Float simvalue=Levenshtein.levenshtein(param.getString("searchKey"), views.get(i).getTitleTxt());
					if (simvalue>0.2) {
						SearchViewedBy view = new SearchViewedBy(views.get(i),simvalue);
						list.add(view);
					}
				}
			}
		}
		if (list.size()>0) {
			Collections.sort(list);
		}
		return list;
	}

}
