package com.jws.app.operater.data;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.SearchKey;

public interface SearchKeyMapper {
	
    int saveSearchKey(SearchKey key);
    
    List<SearchKey> querySearchKeyByUserId(@Param("map") HashMap<String, Object> map);
    
    int countKeyHistory(@Param("userId") String userId, @Param("keyWord") String keyWord);

	int saveSearchKeys(@Param("searchKeys") List<SearchKey> searchKeys);
	
}