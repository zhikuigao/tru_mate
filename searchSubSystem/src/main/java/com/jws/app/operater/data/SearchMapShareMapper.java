package com.jws.app.operater.data;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.SearchMapShare;

public interface SearchMapShareMapper {
	
	int insert(SearchMapShare record);
	
	int getTotalNumber(@Param("userId")String userId, @Param("firstTime")Date firstTime);

    List<SearchMapShare> queryShareByPage(SearchMapShare record);
}