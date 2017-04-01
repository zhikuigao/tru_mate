package com.jws.app.operater.control;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jws.app.operater.service.UserBusiService;
import com.jws.common.constant.Constants;

@Controller("monitorControl")
public class MonitorMcPushController {
	
	@Resource
	private UserBusiService userBusiService;
	
	/**
	 * 监控系统公告,实时发送
	 * @param 
	 * @return
	 */	
	@RequestMapping(value = "/recommendNotice.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String  recommendNotice(){
		JSONObject json = userBusiService.monitorSysNotice();
		JSONObject result = JSONObject.fromObject(json.get("result"));
		if (Integer.valueOf(result.getString("code"))!=Constants.RESULT_CODE_SUCCESS) {
			return "推送公告失败";
		}
		JSONObject json1 = userBusiService.monitorSysUpgrade();
		JSONObject result1 = JSONObject.fromObject(json1.get("result"));
		if (Integer.valueOf(result1.getString("code"))!=Constants.RESULT_CODE_SUCCESS) {
			return "推送小美升级失败";
		}
		return "推送成功";
	}
	/**
	 * 监控定时推送内容，资讯、应用信息
	 * @param 
	 * @return
	 */	
	@RequestMapping(value = "/recommendTiming.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String  recommendTiming(){
		JSONObject json = userBusiService.monitorTimingPush();
		JSONObject result = JSONObject.fromObject(json.get("result"));
		if (Integer.valueOf(result.getString("code"))==Constants.RESULT_CODE_SUCCESS) {
			return "监控成功";
		}
		return "监控失败";
	}
	/**
	 * 监控发送失败的数据
	 * @param 
	 * @return
	 */	
	@RequestMapping(value = "/pushFailData.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String  pushFailData(){
		JSONObject json = userBusiService.monitorFailData();
		JSONObject result = JSONObject.fromObject(json.get("result"));
		if (Integer.valueOf(result.getString("code"))==Constants.RESULT_CODE_SUCCESS) {
			return "推送成功";
		}
		return "推送失败";
	}
	
	

}
