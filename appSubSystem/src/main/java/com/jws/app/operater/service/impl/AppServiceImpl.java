package com.jws.app.operater.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jws.app.operater.data.AppHotMessageMapper;
import com.jws.app.operater.data.AppInfoMapper;
import com.jws.app.operater.data.AppTypeMapper;
import com.jws.app.operater.data.AppUsedPercentMapper;
import com.jws.app.operater.data.AppVoteMapper;
import com.jws.app.operater.data.SysAppLogMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.AppHotMessage;
import com.jws.app.operater.model.AppType;
import com.jws.app.operater.model.AppVote;
import com.jws.app.operater.model.SysAppLog;
import com.jws.app.operater.service.AppBusiService;
import com.jws.app.operater.service.AppService;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.constant.ConfigConstants;
import com.jws.common.constant.Constants;
import com.jws.common.exception.ServiceException;
import com.jws.common.util.JsonUtil;
import com.jws.common.util.Levenshtein;
import com.jws.common.util.MD5Util;
import com.jws.common.util.ResultPackaging;

@Service("appService")
public class AppServiceImpl  implements AppService{
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private AppBusiService appBusiService;
	@Resource
	private AppInfoMapper appInfoMapper;
	@Resource
	private AppTypeMapper appTypeMapper;
	@Resource
	private AppVoteMapper appVoteMapper;
	@Resource
	private SysAppLogMapper sysAppLogMapper;
	@Resource
	private AppUsedPercentMapper appUsedPercentMapper;
	@Resource
	private SystemTimeMapper systemTimeMapper;
	@Resource
	private AppHotMessageMapper appHotMessageMapper;


	@Override
	public JSONObject commonEntry(String param) {
		// 返回结果		
		JSONObject returnJson = new JSONObject();
		// 请求参数
		JSONObject requestJson = new JSONObject();		
		// 业务参数
		JSONObject paramJson = new JSONObject();
		// 安全参数
		JSONObject  securityJson = new JSONObject();
		//1.记录调用日志
		requestJson = JSONObject.fromObject(param);
		SysAppLog  log = addCallLog(param, requestJson.optString("device", "busiSystem"));
		try {
			paramJson = requestJson.optJSONObject("paramMap");
			securityJson =  requestJson.optJSONObject("securityJson");
		} catch (Exception e) {
			logger.error("参数转换失败："+e.getMessage());
			returnJson = ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_CONVERT_FAILED, null, requestJson.optString("language", "ZH"));
			updateCallLog(returnJson, log, null);
			return  returnJson;
		}
		// 参数验证
		if (!securityJson.has("time") || !securityJson.has("Md5Str") ||!paramJson.has("busiCode") || !paramJson.has("language") || 
				 !paramJson.has("source")) {
			returnJson = ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_PARAM_LACK, null, requestJson.optString("language", "ZH"));
			updateCallLog(returnJson, log, null);
			return  returnJson;
		}
		//2.安全校验
		securityJson.put("language", paramJson.optString("language", "ZH"));
		JSONObject securityObject = securityVerification(securityJson);
		if (null != securityObject) {
			updateCallLog(securityObject, log, paramJson);
			return  securityObject;
		}		
		//3.子业务跳转
		//分配到业务层 appBusiService
		if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.APP_QUERY_ALL_APP_TYPES)) {
			// 查询所有应用种类
			returnJson = queryAllAppTypes(paramJson);
		} else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.APP_QUERY_ALL_APPS)) {
			// 查询所有应用信息
			returnJson = queryAllApps(paramJson);
		} else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.APP_SEARCH_PAGINATION)) {
			// 应用分页搜索
			returnJson = appSearchPagination(paramJson);
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.APP_QUERY_INFO)) {
			// 查询应用详情
			returnJson = queryAppInfo(paramJson);
		} else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.APP_SAVE_APP_VOTE)) {
			// 应用点赞
			returnJson = saveAppVote(paramJson);
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.APP_RECOMMEND)) {
			// app 推荐
			returnJson = appRecommend(paramJson);
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.APP_USED_PERCENT)) {
			//时间去哪
			returnJson = appUsedPercent(paramJson);
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.APP_HOTMESSAGE)) {
			// app 推荐
			returnJson = queryAppHotMessage(paramJson);
		}
		//4.更新调用日志
		updateCallLog(returnJson, log, paramJson);
		return returnJson;
	}
	

	private JSONObject appUsedPercent(JSONObject requestJson){
		// 获取参数		
		if (!requestJson.has("userId") || !requestJson.has("maxId")) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_PARAM_LACK, null, requestJson.optString("language", "ZH"));
		}
		JSONObject result = new JSONObject();
		String userId = requestJson.optString("userId");
		Integer maxId = Integer.valueOf(requestJson.optString("maxId"));
		Integer newMaxId = appUsedPercentMapper.getMaxIdByUserId(userId);
		result.put("maxId", newMaxId);
		List<HashMap<Object, Object>> yesList = new ArrayList<>();
		List<HashMap<Object, Object>> weekList = new ArrayList<>();
		List<HashMap<Object, Object>> monthList = new ArrayList<>();
		if (maxId == newMaxId) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, result, requestJson.optString("language", "ZH"));
		}
		Date currDate = systemTimeMapper.getSystemTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currDate);
		calendar.add(Calendar.DATE, -1);
		List<HashMap<Object, Object>> list = appUsedPercentMapper.queryAppPercentOther(userId,Constants.dfd.format(currDate));
		yesList = appUsedPercentMapper.queryAppPercentYes(userId, Constants.dfd.format(calendar.getTime()));
		if (list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				HashMap<Object, Object> map = list.get(i);
				if (StringUtils.equals("1", map.get("type").toString())) {
					weekList.add(map);
				}else{
					monthList.add(map);					
				}
			}			
		}
		result.put("yesList", yesList);
		result.put("weekList", weekList);
		result.put("monthList", monthList);
		return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, result, requestJson.optString("language", "ZH"));
	}

	private JSONObject appRecommend(JSONObject requestJson){
		// 获取参数		
		if (!requestJson.has("userId") || !requestJson.has("number")) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_PARAM_LACK, null, requestJson.optString("language", "ZH"));
		}
		String userId = requestJson.optString("userId");
		int number = requestJson.optInt("number");
		try {
			JSONObject json= new JSONObject();
			//查询用户设置的职业信息
			HashMap<String, String> userInfo = appInfoMapper.queryUserProfessinal(userId);
			if (null == userInfo || StringUtils.isEmpty(userInfo.get("professional")) || StringUtils.isEmpty(userInfo.get("industry"))) {
				return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.USER_INFO_UN_OVERALL, null, requestJson.optString("language", "ZH"));
			}
			//查询分类对应的tag
			List<AppType> tags = appTypeMapper.queryAllAppTags();
			StringBuffer idBuffer = new StringBuffer();
			//比较职业相似度
			for (int i = 0; i < tags.size(); i++) {
				if (null != tags.get(i).getProfessionalTag()) {
					Float simvalue=Levenshtein.levenshtein(userInfo.get("professional").toString(), tags.get(i).getProfessionalTag());
					if (simvalue>0) {
						idBuffer.append(tags.get(i).getId()).append(",");
					}
				}
			}
			//比较行业相似度
			if (idBuffer.length()==0) {
				for (int i = 0; i < tags.size(); i++) {
					if (null != tags.get(i).getIndustryTag()) {
						Float simvalue=Levenshtein.levenshtein(userInfo.get("industry").toString(), tags.get(i).getIndustryTag());
						if (simvalue>0) {
							idBuffer.append(tags.get(i).getId()).append(",");
						}
					}
				}
			}
			List<HashMap<String, Object>> list = new ArrayList<>();
			if (idBuffer.length()>0) {
				list = appInfoMapper.queryAppByTypes(idBuffer.substring(0, idBuffer.length()-1));
			}else{
				list = appInfoMapper.queryAppByTypes(null);
			}
			if (list.size()==0 || list.size()<number || list.size()==number ) {
				json.put("appInfos", list);
			}else{
				List<HashMap<String, Object>> returnList = new ArrayList<>();
				HashMap<Integer,Integer> map = new HashMap<>(); 
				while(returnList.size()<number){
					int n= (int)(Math.random()*list.size());
					if (map.containsKey(n)) {
						continue;
					}
					map.put(n, n);
					returnList.add(list.get(n));
				}
				json.put("appInfos",returnList);
			}
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, json, requestJson.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("推荐应用信息异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_QUERY_APP_INFOS_FAILED, null, requestJson.optString("language", "ZH"));
		}
	}     

	/**
	 * 查询所有应用种类
	 * @param requestJson
	 * @return
	 * @throws ServiceException
	 */
	private JSONObject queryAllAppTypes(JSONObject requestJson) {
//		JSONObject returnJson = new JSONObject();
		try {
			List<AppType> appTypes = appBusiService.queryAllAppTypes();
//			returnJson.put("appTypes", gson.toJson(appTypes));
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, appTypes, requestJson.optString("language", "ZH"));
		} catch (ServiceException e) {
			logger.error("查询所有应用种类异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_QUERY_APP_TYPES_FAILED, null, requestJson.optString("language", "ZH"));
		}
	}

	
	
	
	
	private JSONObject queryAppHotMessage(JSONObject requestJson){
		// 获取参数		
		JSONObject json = new JSONObject();
		String time = requestJson.optString("createTime");
		try {
			String dbTime = this.appHotMessageMapper.queryHotMessageTime();
			//不传时间，或者是时间版本更新，返回新数据
			if(StringUtils.isEmpty(time) || !time.equals(dbTime)){
				List<AppHotMessage> hotList = this.appHotMessageMapper.queryHotMessage(0,100);
				json = JsonUtil.addJsonObject("data",hotList,json);
				json = JsonUtil.add2JsonObject("createTime",dbTime,json);
				json = ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, json, requestJson.optString("language", "EN"));
			}else{
				json = ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, json, requestJson.optString("language", "EN"));
			}
			return json;
		} catch (Exception e) {
			logger.error("<<<<<<热点资讯<<<<<"+e);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_QUERY_APP_INFOS_FAILED,null , requestJson.optString("language", "ZH"));
		}
	}
	
	
	/**
	 * 应用分页搜索
	 * @param requestJson
	 * @return
	 * @throws ServiceException
	 */
	private JSONObject appSearchPagination(JSONObject requestJson) {
		// 获取参数		
		if (!requestJson.has("searchKey") || !requestJson.has("currPage") || !requestJson.has("pageNum")) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_PARAM_LACK, null, requestJson.optString("language", "ZH"));
		}
		JSONObject json = new JSONObject();
		String searchKey = "%"+requestJson.getString("searchKey")+"%";
		Integer currPage = Integer.valueOf(requestJson.getString("currPage"));
		Integer pageNum = Integer.valueOf(requestJson.getString("pageNum"));
		try {
			Integer totalNum = appInfoMapper.countAppBySearchKey(searchKey);
			json.put("totalNum", totalNum);
			List<HashMap<String, Object>> apps = new ArrayList<>();
			if (totalNum>0) {
				Integer pageFrom = currPage * pageNum;
				//查询分类下的应用
				apps = appInfoMapper.queryAppBySearchKey(searchKey,pageFrom,pageNum);
				if (apps.size()>0) {
					for (int i = 0; i < apps.size(); i++) {
						Float size = Float.valueOf(apps.get(i).get("size").toString());
						if (size>512) {
							apps.get(i).put("size",size/1024+"G");
						}else {
							apps.get(i).put("size", size+"M");
						}
					}
				}
			}
			json.put("apps", apps);
			currPage++;
			json.put("currPage", currPage);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS,json , requestJson.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("应用分页搜索异常："+e);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_QUERY_APP_INFOS_FAILED,null , requestJson.optString("language", "ZH"));
		}
		
	}
	
	/**
	 * 根据分类查询应用信息
	 * @param requestJson
	 * @return
	 * @throws ServiceException
	 */
	private JSONObject queryAllApps(JSONObject requestJson) {
		// 获取参数		
		if (!requestJson.has("typeId") || !requestJson.has("currPage") || !requestJson.has("pageNum")) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_PARAM_LACK, null, requestJson.optString("language", "ZH"));
		}
		JSONObject json = new JSONObject();
		Integer typeId = Integer.valueOf(requestJson.getString("typeId"));
		Integer currPage = Integer.valueOf(requestJson.getString("currPage"));
		Integer pageNum = Integer.valueOf(requestJson.getString("pageNum"));
		try {
			Integer totalNum = appInfoMapper.countAppByTypeId(typeId);
			json.put("totalNum", totalNum);
			List<HashMap<String, Object>> apps = new ArrayList<>();
			if (totalNum>0) {
				Integer pageFrom = currPage * pageNum;
				//查询分类下的应用
				apps = appInfoMapper.queryAppByType(typeId,pageFrom,pageNum);
			}
			json.put("apps", apps);
			currPage++;
			json.put("currPage", currPage);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS,json , requestJson.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("分页查询应用异常："+e);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_QUERY_APP_INFOS_FAILED,null , requestJson.optString("language", "ZH"));
		}
		
	}
	
	/**
	 * 查询应用详情
	 * @param requestJson
	 * @return
	 * @throws ServiceException
	 */
	private JSONObject queryAppInfo(JSONObject requestJson) {
		// 获取参数
		if (!requestJson.has("appId")) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_PARAM_LACK, null, requestJson.optString("language", "ZH"));
		}
		String appId = requestJson.optString("appId");
		try {
			HashMap<Object, Object> info = appInfoMapper.queryAppInfo(appId);
			if (null == info) {
				return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.NOT_FOUND_INFO, null, requestJson.optString("language", "ZH"));
			}
			Float size = Float.valueOf(info.get("size").toString());
			if (size>512) {
				info.put("size", size/1024+"G");
			}else {
				info.put("size", size+"M");
			}
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, info, requestJson.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("查询应用详情异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_QUERY_APP_VERSIONS_FAIELD, null, requestJson.optString("language", "ZH"));
		}
	}
	
	/**
	 * 应用点赞
	 * @param paramJson
	 * @return
	 */
	private JSONObject saveAppVote(JSONObject requestJson) {
		JSONObject returnJson = new JSONObject();
		// 获取参数
		if (!requestJson.has("appId") || !requestJson.has("userId")) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_PARAM_LACK, null, requestJson.optString("language", "ZH"));
		}
		
		String appId = requestJson.optString("appId");
		String userId = requestJson.optString("userId");
		
		try {
			// 检查是否已赞
			String voteId = appBusiService.queryAppVoteId(appId, userId); 
			if (!StringUtils.isEmpty(voteId)) {
				return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SAVE_APP_VOTE_REPEAT, Constants.RESULT_CODE_SAVE_APP_VOTE_REPEAT, null, requestJson.optString("language", "ZH"));
			}			
			// 如果没有赞
			AppVote appVote = new AppVote();
			appVote.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			appVote.setAppId(appId);
			appVote.setUserId(userId);
			// 应用点赞
			int saveResult = appBusiService.saveAppVote(appVote);
			if (saveResult != 1) {
				logger.error("userId 或 appId 不存在");
				return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_APP_VOTE_FAILED, null, requestJson.optString("language", "ZH"));
			}
			// 更新应用信息表
			appBusiService.updateAppVoteCountByAppId(appId);
			
			// 获取最新的点赞数
			int voteCount = appBusiService.queryAppVoteCountByAppId(appId);
			returnJson.put("voteCount", voteCount);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, returnJson, requestJson.optString("language", "ZH"));
			
		} catch (Exception e) {
			logger.error("应用点赞异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_APP_VOTE_FAILED, null, requestJson.optString("language", "ZH"));
		}		
	}
	
	/**
	 * 安全校验
	 * @param securityJson
	 * @return
	 */
	private JSONObject  securityVerification(JSONObject  securityJson){
		String time = securityJson.getString("time");
		String Md5Str = securityJson.getString("Md5Str");
		String key = ConfigConstants.SECURITY_KEY;
		String newMd5Str = MD5Util.getMD5String(key+time);
		if (!StringUtils.equals(Md5Str, newMd5Str)) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SECURITY_VERIFY_FAILED, null, securityJson.getString("language"));
		}
		return  null;
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
			logger.error("记录日志异常"+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 更新日志
	 * @param returnJson
	 * @param oldLog
	 * @param paramObject
	 */
	private  void  updateCallLog(JSONObject  returnJson, SysAppLog  oldLog , JSONObject paramObject){
		try {
			oldLog.setResponse(String.valueOf(returnJson));
			if (null != paramObject) {
				oldLog.setSource(paramObject.getString("source"));
			}
			sysAppLogMapper.logUpdateByPrimaryKey(oldLog);
		} catch (Exception e) {
			logger.error("更新日志异常"+e.getMessage());
		}
	}

}
