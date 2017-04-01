package com.jws.app.operater.data;

import java.util.List;

import com.jws.app.operater.model.AppType;

public interface AppTypeMapper {
	
	List<AppType> queryAllAppTypes();
	
	List<AppType> queryAllAppTags();
	
}