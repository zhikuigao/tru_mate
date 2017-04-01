package com.jws.app.operater.service.impl;

import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jws.app.operater.data.SysBusiLogMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.SysBusiLog;
import com.jws.app.operater.service.SysBusiLogService;
import com.jws.common.constant.Constants;
@Service("sysBusiLogService")
public class SysBusiLogServiceImpl  implements SysBusiLogService{
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private SystemTimeMapper systemTimeMapper;
	@Resource
	private SysBusiLogMapper sysBusiLogMapper;

	@Override
	public SysBusiLog addCallLog(JSONObject  jsonObject) {
		SysBusiLog  log = new SysBusiLog();
		try {
			log.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			log.setRequest(jsonObject.toString());
			if (jsonObject.containsKey("device")) {
				log.setSource(jsonObject.get("device").toString());
			}
			if (jsonObject.containsKey("receive")) {
				log.setReceive(jsonObject.get("receive").toString());
			}else {
				log.setReceive(Constants.CALL_SOURCE);
			}			
//			log.setCreateTime(systemTimeMapper.getSystemTime());
			sysBusiLogMapper.logInsertSelective(log);
		} catch (Exception e) {
			logger.error("记录日志异常"+e.getMessage());
		}
		return log;
	}

	@Override
	public void updateCallLog(String returnJson, SysBusiLog oldLog,
			JSONObject  jsonObject) {
		try {
			if (null != jsonObject && jsonObject.has("device")) {
				oldLog.setSource(jsonObject.getString("device"));
			}
			oldLog.setResponse(returnJson);
			if (null != oldLog.getId()) {
				sysBusiLogMapper.logUpdateByPrimaryKey(oldLog);
			}
		} catch (Exception e) {
			logger.error("更新日志异常"+e.getMessage());
		}
	}
	
	

}
