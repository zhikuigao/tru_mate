package com.jws.app.operater.data;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.AppVote;


public interface AppVoteMapper {
    
	int saveAppVote(AppVote appVote);
	
	String queryAppVoteId(@Param("appId") String appId, @Param("userId") String userId);
	
}