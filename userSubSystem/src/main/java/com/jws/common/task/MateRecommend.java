package com.jws.common.task;


import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jws.app.operater.service.UserService;

@Component
public class MateRecommend {
	
	@Resource
	private UserService userService;
	/**
	 * 监控系统公告,实时发送
	 * 每天10点到18点每隔1个小时执行
	 */
	@Scheduled(cron="0 0 10-18/1 * * ?")
	public void recommendNotice(){
		userService.monitorSysNotice();
	}
	/**
	 * 监控系统升级，实时发送
	 * 每天下午4点半执行
	 */
	@Scheduled(cron="0 30 16 ? * *")
	public void recommendUpgrade(){
		userService.monitorSysUpgrade();
	}
	/**
	 * 监控定时推送内容，资讯、应用信息
	 * 每天早上7点执行
	 */
	@Scheduled(cron="0 0 7 ? * *")
//	@Scheduled(cron="0 0/10 10-21 * * ?")
	public void recommendTiming(){
		userService.monitorTimingPush();
	}
	
	/**
	 * 监控定时发送数据
	 * 每天9点 59分 14点59分执行
	 */
	@Scheduled(cron="0 59 09,14 * * ?")
//	@Scheduled(cron="0 0/15 10-21 * * ?")
	public void pushTimingData(){
		userService.pushTimingData();
	}
	
	/**
	 * 监控发送失败的数据
	 * 每天10点到10点50分和15点到15点50分，每10分钟触发一次
	 */
	@Scheduled(cron="0 10/10 10,15 * * ?")
//	@Scheduled(cron="0 1/5 10-21 * * ?")
	public void pushFailData(){
		userService.pushFailData();
	}

}
