package com.jws.app.operater.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jws.app.operater.data.SysAppLogMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.SysAppLog;
import com.jws.app.operater.service.CalculationAppService;
import com.jws.app.operater.service.CalculationSubService;
import com.jws.common.constant.Constants;

@Service("calculationAppService")
public class CalculationAppServiceImpl implements CalculationAppService{
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private SystemTimeMapper systemTimeMapper;
	@Resource
	private SysAppLogMapper sysAppLogMapper;
	@Resource
	private CalculationSubService calculationSubService;

	@Override
	public void calculationYes() {
		Date currDate = systemTimeMapper.getSystemTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currDate);
		String end =Constants.dfd.format(calendar.getTime());
		calendar.add(Calendar.DATE, -1);
		String start = Constants.dfd.format(calendar.getTime());
		JSONObject param = new JSONObject();
		param.put("start", start);
		param.put("end", end);
		param.put("type", "昨天");
		SysAppLog log =addCallLog(param.toString(), "calculationYes");
		calculationSubService.calculationDurationPercent(start, end,"0");
		updateCallLog(log);
	}

	@Override
	public void calculationWeek() {
		Date currDate = systemTimeMapper.getSystemTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currDate);
		String end =Constants.dfd.format(calendar.getTime());
		calendar.add(Calendar.DATE, -7);
		String start = Constants.dfd.format(calendar.getTime());
		JSONObject param = new JSONObject();
		param.put("start", start);
		param.put("end", end);
		param.put("type", "最近一周");
		SysAppLog log =addCallLog(param.toString(), "calculationWeek");
		calculationSubService.calculationDurationMore(start, end,"1");
		updateCallLog(log);
	}

	@Override
	public void calculationMonth() {
		Date currDate = systemTimeMapper.getSystemTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currDate);
		String end =Constants.dfd.format(calendar.getTime());
		calendar.add(Calendar.DATE, -30);
		String start = Constants.dfd.format(calendar.getTime());
		JSONObject param = new JSONObject();
		param.put("start", start);
		param.put("end", end);
		param.put("type", "最近30天");
		SysAppLog log =addCallLog(param.toString(), "calculationMonth");
		calculationSubService.calculationDurationMore(start, end, "2");
		updateCallLog(log);
	}

	
	
	/**
	 * 添加日志
	 * @param param
	 * @return
	 */
	private  SysAppLog  addCallLog(String param, String source){
		try {
			SysAppLog  log = new SysAppLog();
			log.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			log.setRequest(param);
			log.setSource(source);
			sysAppLogMapper.logInsert(log);
			return log;
		} catch (Exception e) {
			logger.error("时间去哪定时任务记录日志异常"+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 更新日志
	 * @param returnJson
	 * @param oldLog
	 * @param paramObject
	 */
	private  void  updateCallLog(SysAppLog  oldLog){
		try {
			oldLog.setResponse("执行成功");
			sysAppLogMapper.logUpdateByPrimaryKey(oldLog);
		} catch (Exception e) {
			logger.error("时间去哪定时任务更新日志异常"+e.getMessage());
		}
	}


}
