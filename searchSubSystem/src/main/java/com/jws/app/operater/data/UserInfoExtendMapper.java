package com.jws.app.operater.data;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.UserInfoExtend;

public interface UserInfoExtendMapper {
	
	UserInfoExtend queryUserExtend(@Param("userId")String userId);
    
    int updateUserExtend(@Param("record")UserInfoExtend record);
    
    int insertUserExtend(@Param("record")UserInfoExtend record);
    
}