package com.jws.app.operater.data;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.AppVersion;

public interface AppVersionMapper {


	/**
	 * 获取应用的版本信息
	 * @param map
	 * @return
	 */
	List<AppVersion> queryAppVersions(@Param("map") HashMap<String, Object> map);
	
}