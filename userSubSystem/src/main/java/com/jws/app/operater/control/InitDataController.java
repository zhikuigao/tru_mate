package com.jws.app.operater.control;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jws.app.operater.model.AppRecommend;
import com.jws.app.operater.model.AppVersion;
import com.jws.app.operater.model.NewsRecommend;
import com.jws.app.operater.model.SysNotice;
import com.jws.app.operater.service.InitDataService;

@Controller("initDataController")
public class InitDataController {
	@Resource
	private InitDataService initDataService;

	/**
	 * 添加公告
	 * @return
	 */
	@RequestMapping(value = "/addNotice.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String  recommendNotice(@ModelAttribute SysNotice notice){
		if (null != notice) {
			boolean result = initDataService.addNotice(notice);
			if (result) {
				return "保存成功";
			}
			return "保存失败";
		}
		return "获取参数失败";
	}
	/**
	 * 添加推荐资讯
	 * @return
	 */
	@RequestMapping(value = "/addRecNews.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String  addRecNews(@ModelAttribute NewsRecommend news){
		if (null != news) {
			return initDataService.addRecNews(news);
		}
		return "获取参数失败";
	}
	
	/**
	 * 添加推荐应用
	 * @return
	 */
	@RequestMapping(value = "/addRecApp.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String  addRecApp(@ModelAttribute AppRecommend app){
		if (null != app) {
			return initDataService.addRecApp(app);
		}
		return "获取参数失败";
	}
	/**
	 * 添加小美版本
	 * @return
	 */
	@RequestMapping(value = "/addAppVersion.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String  addAppVersion(@ModelAttribute AppVersion app){
		if (null != app) {
			return initDataService.addAppVersion(app);
		}
		return "获取参数失败";
	}
	/**
	 * 获取小美最新版本
	 * @return
	 */
	@RequestMapping(value = "/getNewestVersion.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String  getNewestVersion(@ModelAttribute AppVersion app){
		return initDataService.getNewestVersion(app);
	}

	
}
