package com.jws.app.operater.control;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jws.app.operater.service.AppService;

@Controller("appController")
@RequestMapping("/entry")
public class AppController {
	@Resource
	private AppService appService;
	
	/**
	 * app模块api统一入口
	 * @param paramObject
	 * @return
	 */
	@RequestMapping(value = "/appBlockEntry.do", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject  userBlockEntry(String paramObject) {
		return appService.commonEntry(paramObject);
	}
}
