
package com.jws.app.operater.data;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.AppHotMessage;



public interface AppHotMessageMapper {
	
	 List<AppHotMessage> queryHotMessage(@Param("page")Integer page,@Param("pageNum")Integer pageNum);
	 String queryHotMessageTime();
	
}