package com.jws.app.operater.service;

import com.jws.app.operater.model.AppRecommend;
import com.jws.app.operater.model.AppVersion;
import com.jws.app.operater.model.NewsRecommend;
import com.jws.app.operater.model.SysNotice;

public interface InitDataService {
	
	boolean addNotice(SysNotice notice);
	
	String addRecNews(NewsRecommend news);
	
	String addRecApp(AppRecommend app);
	
	String addAppVersion(AppVersion app);
	
	String getNewestVersion(AppVersion app);

}
