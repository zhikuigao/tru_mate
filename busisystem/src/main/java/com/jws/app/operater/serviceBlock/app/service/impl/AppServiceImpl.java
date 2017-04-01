package com.jws.app.operater.serviceBlock.app.service.impl;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.jws.app.operater.service.BlockEntryService;
import com.jws.app.operater.service.impl.InterfaceInvoking;
import com.jws.common.constant.BlockBusiCodeConstant;
import com.jws.common.constant.ConfigConstants;
import com.jws.common.constant.Constants;
import com.jws.common.util.ResultPackaging;

@Service("appService")
public class AppServiceImpl implements BlockEntryService {
	@Resource
	private InterfaceInvoking interfaceInvoking;
	/**
	 * 根据busiCode分方法入口
	 */
	@Override
	public JSONObject entry(JSONObject paramJson) {
		//组装参数
		JSONObject returnObject = new JSONObject();
		if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.APP_QUERY_ALL_APP_TYPES)) {
			returnObject = queryAllAppTypes(paramJson);
		} else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.APP_QUERY_ALL_APPS)) {
			returnObject = queryAllApps(paramJson);
		} else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.APP_QUERY_INFO)) {
			returnObject = queryAppInfo(paramJson);	
		} else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.APP_SEARCH_PAGINATION)) {
			returnObject = appSearchPagination(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.APP_SAVE_APP_VOTE)) {
			returnObject = saveAppVote(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.APP_RECOMMEND)) {
			//应用推荐
			returnObject = appRecommend(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.APP_USED_PERCENT)) {
			//时间去哪
			returnObject = appUsedPercent(paramJson);	
		} else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.APP_HOTMESSAGE)) {
			//应用推荐
			returnObject = appHotMessage(paramJson);	
		}else{
			//找不到对应的业务方法
			returnObject = ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_100, null, paramJson.optString("language", "ZH"));
		}
		return returnObject;
	}
	
	/**
	 * 时间去哪
	 * @param paramJson
	 * @return
	 */
	private JSONObject appUsedPercent(JSONObject paramJson) {
		if (!paramJson.has("userId") || !paramJson.has("maxId")) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_APP_LACK_PARAM, null, paramJson.optString("language", "ZH"));
		}
		JSONObject  json = new JSONObject();
		json.put("source", Constants.CALL_SOURCE);
		json.put("receive", Constants.CALL_TO_APP);
		json.put("busiCode", paramJson.optString("busiCode"));
		json.put("userId", paramJson.getString("userId"));
		json.put("maxId", paramJson.getString("maxId"));
		return interfaceInvoking.invoking(ConfigConstants.URL_APP_SYSTEM, json, paramJson);	
	}
	
	/**
	 * 应用推荐
	 * @param paramJson
	 * @return
	 */
	private JSONObject appRecommend(JSONObject paramJson) {
		if (!paramJson.has("userId") || !paramJson.has("number")) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_APP_LACK_PARAM, null, paramJson.optString("language", "ZH"));
		}
		JSONObject  appSystemParamObject = new JSONObject();
		appSystemParamObject.put("source", Constants.CALL_SOURCE);
		appSystemParamObject.put("receive", Constants.CALL_TO_APP);
		appSystemParamObject.put("busiCode", paramJson.optString("busiCode"));
		appSystemParamObject.put("userId", paramJson.getString("userId"));
		appSystemParamObject.put("number", paramJson.getString("number"));
		return interfaceInvoking.invoking(ConfigConstants.URL_APP_SYSTEM, appSystemParamObject, paramJson);	
	}
	
	/**
	 * 应用推荐
	 * @param paramJson
	 * @return
	 */
	private JSONObject appHotMessage(JSONObject paramJson) {
//		if (!paramJson.has("createTime")) {
//			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_APP_LACK_PARAM, null, paramJson.optString("language", "EN"));
//		}
		JSONObject object = new JSONObject();
		object.put("source", Constants.CALL_SOURCE);
		object.put("receive", Constants.CALL_TO_APP);
		object.put("busiCode", paramJson.optString("busiCode"));
		//object.put("page", paramJson.get("page"));
		//object.put("pageNum", paramJson.get("pageNum"));
		object.put("createTime", paramJson.optString("createTime"));
		return interfaceInvoking.invoking(ConfigConstants.URL_APP_SYSTEM, object, paramJson);
	}

	
	/**
	 * 查询所有应用种类
	 * @param paramJson
	 * @return
	 */
	private JSONObject queryAllAppTypes(JSONObject paramJson) {
		JSONObject  appSystemParamObject = new JSONObject();
		appSystemParamObject.put("source", Constants.CALL_SOURCE);
		appSystemParamObject.put("receive", Constants.CALL_TO_APP);
		appSystemParamObject.put("busiCode", paramJson.optString("busiCode"));
		// 将每一个接口具体的业务参数添加到appSystemParamObject
		return interfaceInvoking.invoking(ConfigConstants.URL_APP_SYSTEM, appSystemParamObject, paramJson);	
	}

	/**
	 * 查询全部应用
	 * @param requestJson
	 * @return
	 */
	private JSONObject appSearchPagination(JSONObject requestJson) {
		// 获取参数		
		if (!requestJson.has("searchKey") || !requestJson.has("currPage") || !requestJson.has("pageNum")) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_APP_LACK_PARAM, null, requestJson.optString("language", "ZH"));
		}
		//组装参数
		JSONObject object = new JSONObject();
		object.put("source", Constants.CALL_SOURCE);
		object.put("receive", Constants.CALL_TO_APP);
		object.put("busiCode", requestJson.optString("busiCode"));
		object.put("searchKey", requestJson.get("searchKey"));
		object.put("currPage", requestJson.get("currPage"));
		object.put("pageNum", requestJson.get("pageNum"));
		return interfaceInvoking.invoking(ConfigConstants.URL_APP_SYSTEM, object, requestJson);
	}
	
	/**
	 * 查询全部应用
	 * @param requestJson
	 * @return
	 */
	private JSONObject queryAllApps(JSONObject requestJson) {
		// 获取参数		
		if (!requestJson.has("typeId") || !requestJson.has("currPage") || !requestJson.has("pageNum")) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_APP_LACK_PARAM, null, requestJson.optString("language", "ZH"));
		}
		//组装参数
		JSONObject object = new JSONObject();
		object.put("source", Constants.CALL_SOURCE);
		object.put("receive", Constants.CALL_TO_APP);
		object.put("busiCode", requestJson.optString("busiCode"));
		object.put("typeId", requestJson.get("typeId"));
		object.put("currPage", requestJson.get("currPage"));
		object.put("pageNum", requestJson.get("pageNum"));
		return interfaceInvoking.invoking(ConfigConstants.URL_APP_SYSTEM, object, requestJson);
	}
	
	/**
	 * 查询应用详情
	 * @param paramJson
	 * @return
	 */
	private JSONObject queryAppInfo(JSONObject paramJson) {
		// 将每一个接口具体的业务参数添加到appSystemParamObject
		JSONObject  appSystemParamObject = new JSONObject();
		appSystemParamObject.put("source", Constants.CALL_SOURCE);
		appSystemParamObject.put("receive", Constants.CALL_TO_APP);
		appSystemParamObject.put("busiCode", paramJson.optString("busiCode"));
		appSystemParamObject.put("appId", paramJson.optString("appId"));
		return interfaceInvoking.invoking(ConfigConstants.URL_APP_SYSTEM, appSystemParamObject, paramJson);	
	}
	
	/**
	 * @param requestJson
	 * @return
	 */
	private JSONObject saveAppVote(JSONObject requestJson) {
		// 将每一个接口具体的业务参数添加到appSystemParamObject
		// 获取参数
		if (!requestJson.has("appId") || !requestJson.has("userId")) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_APP_LACK_PARAM, null, requestJson.optString("language", "ZH"));
		}
		JSONObject  appSystemParamObject = new JSONObject();
		appSystemParamObject.put("source", Constants.CALL_SOURCE);
		appSystemParamObject.put("receive", Constants.CALL_TO_APP);
		appSystemParamObject.put("busiCode", requestJson.optString("busiCode"));
		appSystemParamObject.put("appId", requestJson.optString("appId"));
		appSystemParamObject.put("userId", requestJson.optString("userId"));
		return interfaceInvoking.invoking(ConfigConstants.URL_APP_SYSTEM, appSystemParamObject, requestJson);
	}

}
