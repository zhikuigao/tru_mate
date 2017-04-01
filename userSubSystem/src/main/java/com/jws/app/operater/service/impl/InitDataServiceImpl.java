package com.jws.app.operater.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jws.app.operater.data.AppVersionMapper;
import com.jws.app.operater.data.SysNoticeMapper;
import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.model.AppCategoryRec;
import com.jws.app.operater.model.AppRecommend;
import com.jws.app.operater.model.AppVersion;
import com.jws.app.operater.model.CategoryRec;
import com.jws.app.operater.model.NewsRecommend;
import com.jws.app.operater.model.SysNotice;
import com.jws.app.operater.model.UserInfo;
import com.jws.app.operater.service.InitDataService;
import com.jws.common.constant.ConfigConstants;
@Service("initDataService")
public class InitDataServiceImpl implements InitDataService{
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private SysNoticeMapper sysNoticeMapper;
	@Resource
	private UserInfoMapper userInfoMapper;
	@Resource
	private AppVersionMapper appVersionMapper;

	@Override
	public boolean addNotice(SysNotice notice) {
		try {
			notice.setId(UUID.randomUUID().toString().replace("-", ""));
			sysNoticeMapper.insert(notice);
			return true;
		} catch (Exception e) {
			logger.error("保存公告信息异常："+e.getMessage());
		}
		return false;
	}

	@Override
	public String addRecNews(NewsRecommend news) {
		try {
			UserInfo user = userInfoMapper.queryById(news.getUserId().trim());
			if (null==user || null == user.getId()) {
				return "未找到对应的用户信息";
			}
			CategoryRec rec = new CategoryRec();
			rec.setUserId(news.getUserId());
			rec.setCategoryName(news.getCategoryName());
			int isexist = sysNoticeMapper.countNewsCategory(rec);
			if (isexist == 0) {
				sysNoticeMapper.insertNewsCategoryRec(rec);
			}
			sysNoticeMapper.insertNews(news);
		} catch (Exception e) {
			logger.error("添加推荐资讯异常"+e.getMessage());
			return "保存信息异常";
		}
		
		return "保存成功";
	}

	@Override
	public String addRecApp(AppRecommend app) {
		try {
			UserInfo user = userInfoMapper.queryById(app.getUserId().trim());
			if (null==user || null == user.getId()) {
				return "未找到对应的用户信息";
			}
			HashMap<String, Object> appInfo = sysNoticeMapper.queryAppInfo(app.getId());
			if (null==appInfo || !appInfo.containsKey("id")) {
				return "未找到对应的应用信息";
			}
			app.setAppIconUrl(appInfo.get("iconUrl").toString());
			app.setAppName(appInfo.get("name").toString());
			app.setAppVer(appInfo.get("version").toString());
			app.setAppDescribe(appInfo.get("appdesc").toString());
			app.setAppUrl(appInfo.get("url").toString());
			app.setCategoryId(appInfo.get("categoryId").toString());
			app.setCategoryName(appInfo.get("categoryName").toString());
			app.setAppId(app.getId());
			sysNoticeMapper.insertAppRec(app);
			AppCategoryRec rec = new AppCategoryRec();
			rec.setUserId(app.getUserId());
			rec.setCategoryId(Integer.valueOf(appInfo.get("categoryId").toString()));
			rec.setCategoryName(appInfo.get("categoryName").toString());
			int isexist = sysNoticeMapper.countAppCategory(rec);
			if (isexist == 0) {
				sysNoticeMapper.insertAppCategory(rec);
			}
		} catch (Exception e) {
			logger.error("添加推荐应用异常"+e.getMessage());
			return "保存信息异常";
		}
		
		return "保存成功";
	}

	@Override
	public String addAppVersion(AppVersion app) {
		try {
			AppVersion version = new AppVersion();
			version.setAppName(app.getAppName());
			version.setUseType(app.getUseType());
			List<HashMap<String, Object>> listMap = appVersionMapper.selectNewestVersions(version);
			app.setId(UUID.randomUUID().toString().replace("-", ""));
			app.setFileUrl("/package/"+app.getFileUrl());
			if (listMap.size()>0) {
				app.setVersionSerial(Integer.valueOf(listMap.get(0).get("versionSerial").toString())+1);
			}else{
				app.setVersionSerial(1);
			}
			app.setFileType("0");
			if (null==app.getVersionDescEn()) {
				app.setVersionDescEn("");
			}
			if (null == app.getVersionDescImgEn()) {
				app.setVersionDescImgEn("");
			}
			appVersionMapper.insert(app);
			AppVersion app1 = app;
			app1.setFileType("1");
			app1.setId(UUID.randomUUID().toString().replace("-", ""));
			appVersionMapper.insert(app1);
		} catch (Exception e) {
			logger.error("保存小美版本信息异常："+e.getMessage());
			return "保存失败";
		}
		return "保存成功";
	}

	@Override
	public String getNewestVersion(AppVersion app) {
		AppVersion version = new AppVersion();
		version.setAppName(app.getAppName());
		version.setUseType(app.getUseType());
		List<HashMap<String, Object>> list = appVersionMapper.selectNewestVersions(version);
		StringBuffer result= new StringBuffer();
		if (list.size()>0) {
			result.append("应用名：").append(app.getAppName())
			.append(";适用设备：").append(app.getUseType())
			.append(";版本号：").append(list.get(0).get("versionNum"))
			.append(";下载url:").append(ConfigConstants.FTP_SERVER+list.get(0).get("fileUrl"))
			.append(";版本描述中文:").append(list.get(0).get("versionDescZh"))
			.append(";版本描述英文:").append(list.get(0).get("versionDescEn"));
		}
		return result.toString();
	}

}
