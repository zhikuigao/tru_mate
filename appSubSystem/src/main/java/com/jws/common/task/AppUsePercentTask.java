package com.jws.common.task;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jws.app.operater.service.CalculationAppService;

@Component
public class AppUsePercentTask {
	
	@Resource
	private CalculationAppService calculationAppService;
	
//	@Scheduled(cron="0 55 9 * * ?")
	@Scheduled(cron="0 10 0 ? * *")
	public void calculationYes(){
		calculationAppService.calculationYes();
	}
//	@Scheduled(cron="0 55 9 * * ?")
	@Scheduled(cron="0 10 0 ? * *")
	public void calculationWeek(){
		calculationAppService.calculationWeek();
	}
//	@Scheduled(cron="0 55 9 * * ?")
	@Scheduled(cron="0 20 0 ? * *")
	public void calculationMonth(){
		calculationAppService.calculationMonth();
	}

}
