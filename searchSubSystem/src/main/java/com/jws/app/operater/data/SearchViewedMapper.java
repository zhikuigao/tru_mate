package com.jws.app.operater.data;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.SearchInfoAll;
import com.jws.app.operater.model.SearchViewed;

public interface SearchViewedMapper {
	
    int saveSearchViewed(SearchViewed viewed);
    
    List<SearchViewed> querySearchViewed(@Param("map") HashMap<String, Object> map);
    
    int countViewedHistory(@Param("userId") String userId, @Param("keyWord") String keyWord);

	int saveSearchVieweds(@Param("searchVieweds") List<SearchViewed> searchVieweds);
    
	List<SearchInfoAll> queryRecommendSearchHis(@Param("userId")String userId);
}