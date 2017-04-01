package com.jws.app.operater.serviceBlock.search.service.impl;

import java.io.File;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.service.BlockEntryService;
import com.jws.app.operater.service.impl.InterfaceInvoking;
import com.jws.common.constant.BlockBusiCodeConstant;
import com.jws.common.constant.ConfigConstants;
import com.jws.common.constant.Constants;
import com.jws.common.constant.SearchEnum;
import com.jws.common.util.FileUtil;
import com.jws.common.util.ResultPackaging;

@Service("searchService")
public class SearchServiceImpl implements BlockEntryService {
	
//	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private InterfaceInvoking interfaceInvoking;
	@Resource
	private SystemTimeMapper systemTimeMapper;
	
	/**
	 * 根据busiCode分方法入口
	 */
	@Override
	public JSONObject entry(JSONObject paramJson) {
		JSONObject returnObject = new JSONObject();
		//调用子系统
		if(StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.SAVE_SEARCH_SHARE)){
			//保存分享记录
			returnObject = saveSearchShare(paramJson);
		}else if(StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.QUERY_SEARCH_SHARES)){
			//查询分享记录
			returnObject = querySearchShares(paramJson);
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.SAVE_CONFIG)) {
			//保存配置
			returnObject = saveConfig(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.QUERY_CONFIG)) {
			//查询配置
			returnObject = queryConfig(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.INTERNAL_SEARCH)) {
			//小美内部搜索接口
			returnObject = internalSearch(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.GOOGLE_DADAS)) {
			returnObject = requestGoogleDatas(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.SEARCH_SOURCE)) {
			returnObject = searchSource(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.ADDMAPS_SEARCH)) {
			returnObject = addMapsSearch(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.QUERYHISTORY_MAPS)) {
			returnObject = queryHistoryMaps(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.QUERYHISTORY_MAPSDATAS)) {
			returnObject = querySearchMapsDatas(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.QUERQUERY_MAPKEYWORDDATAS)) {
			returnObject = querySearchMapKeywordDatas(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.QUERQUERY_SEARCHLIKEKEYWORD)) {
			returnObject = querySearchLikeKeyword(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.ADD_MATE_SOURCE)) {
			returnObject = addSearchSource(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.DELETE_MATE_SOURCE)) {
			returnObject = delSearchSource(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.QUERY_MATE_SOURCE)) {
			returnObject = querySearchSource(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.QUERY_MAPSDATA_NODES)) {
			returnObject = querySearchMapsDataNodes(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.ADD_MAPSNODES)) {
			returnObject = addMapNodes(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.FLAG_MAP_DATA)) {
			//给地图内的文章加标记
			returnObject = flagMapData(paramJson);	
		}else if (StringUtils.equals(paramJson.optString("busiCode"), BlockBusiCodeConstant.OLD_MAP_RECOMMEND)) {
			//搜索关键词时旧地图推荐
			returnObject = oldMapRecommend(paramJson);	
		}
		else {
			//找不到对应的业务方法
			returnObject = ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_100, null, paramJson.optString("language", "EN"));
		}
		return returnObject;
	}
	/**
	 * 搜索关键词时旧地图推荐
	 * @return
	 */
	private JSONObject oldMapRecommend(JSONObject requestJson){
		if (!requestJson.has("userId")|| !requestJson.has("keyword") ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		JSONObject json = new JSONObject();
		json.put("source", Constants.CALL_SOURCE);
		json.put("receive", Constants.CALL_TO_SEARCH);
		json.put("busiCode", requestJson.optString("busiCode"));
		json.put("userId", requestJson.getString("userId"));
		json.put("keyword", requestJson.getString("keyword"));
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, json, requestJson);
	}
	/**
	 * 给地图内的文章加标记
	 * @return
	 */
	private JSONObject flagMapData(JSONObject requestJson){
		if (!requestJson.has("id")|| !requestJson.has("flag") ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		JSONObject json = new JSONObject();
		json.put("source", Constants.CALL_SOURCE);
		json.put("receive", Constants.CALL_TO_SEARCH);
		json.put("busiCode", requestJson.optString("busiCode"));
		json.put("id", requestJson.getString("id"));
		json.put("flag", requestJson.getString("flag"));
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, json, requestJson);
	}
	/**
	 * 用户配置搜索源
	 * @return
	 */
	private JSONObject internalSearch(JSONObject requestJson){
		if (!requestJson.has("searchKey")|| StringUtils.isEmpty(requestJson.getString("searchKey")) 
				|| !requestJson.has("number") ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		JSONObject json = new JSONObject();
		json.put("source", Constants.CALL_SOURCE);
		json.put("receive", Constants.CALL_TO_SEARCH);
		json.put("busiCode", requestJson.optString("busiCode"));
		json.put("searchKey", requestJson.getString("searchKey"));
		json.put("number", requestJson.getString("number"));
		if (requestJson.has("userId")) {
			json.put("userId", requestJson.getString("userId"));
		}
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, json, requestJson);
	}
	/**
	 * 用户配置搜索源
	 * @return
	 */
	private JSONObject queryConfig(JSONObject requestJson){
		if (!requestJson.has("userId")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		JSONObject json = new JSONObject();
		json.put("source", Constants.CALL_SOURCE);
		json.put("receive", Constants.CALL_TO_SEARCH);
		json.put("busiCode", requestJson.optString("busiCode"));
		json.put("userId", requestJson.getString("userId"));
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, json, requestJson);
	}
	
	
	private JSONObject searchSource(JSONObject requestJson){
		JSONObject json = new JSONObject();
		json.put("source", Constants.CALL_SOURCE);
		json.put("receive", Constants.CALL_TO_SEARCH);
		json.put("busiCode", requestJson.optString("busiCode"));
		json.put("language", requestJson.optString("language", "EN"));
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, json,requestJson);
	}
	
	private JSONObject addMapsSearch(JSONObject requestJson){
		if ( !requestJson.has("mapId")|| !requestJson.has("userId")
				|| (!requestJson.has("keyword") && (!requestJson.has("keywordId") || !requestJson.has("title")  ||!requestJson.has("url")))) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		JSONObject json = new JSONObject();
		json.put("source", Constants.CALL_SOURCE);
		json.put("receive", Constants.CALL_TO_SEARCH);
		json.put("busiCode", requestJson.optString("busiCode"));
		json.put("language", requestJson.optString("language", "EN"));
		json.put("mapId", requestJson.getString("mapId"));
		json.put("userId", requestJson.getString("userId"));
		json.put("keyword", requestJson.getString("keyword"));
		json.put("keywordId", requestJson.getString("keywordId"));
		json.put("title", requestJson.getString("title"));
		json.put("url", requestJson.getString("url"));
		json.put("sourceSearch", requestJson.getString("sourceSearch"));
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, json,requestJson);
	}
	/**
	 * 用户配置搜索源
	 * @return
	 */
	private JSONObject saveConfig(JSONObject requestJson){
		if (!requestJson.has("userId") || !requestJson.has("type")|| !requestJson.has("operate")
				|| (!requestJson.has("id") && (!requestJson.has("name") || !requestJson.has("url"))) ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		JSONObject json = new JSONObject();
		json.putAll(requestJson);
		json.put("source", Constants.CALL_SOURCE);
		json.put("receive", Constants.CALL_TO_SEARCH);
		json.put("busiCode", requestJson.optString("busiCode"));
		json.put("userId", requestJson.getString("userId"));
		json.put("type", requestJson.getString("type"));
		json.put("operate", requestJson.getString("operate"));
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, json, requestJson);
	}
	private JSONObject queryHistoryMaps(JSONObject requestJson){
		if (!requestJson.has("userId") ||!requestJson.has("key") ||!requestJson.has("page") ||!requestJson.has("pageNum")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		JSONObject json = new JSONObject();
		json.putAll(requestJson);
		json.put("source", Constants.CALL_SOURCE);
		json.put("receive", Constants.CALL_TO_SEARCH);
		json.put("busiCode", requestJson.optString("busiCode"));
		json.put("userId", requestJson.getString("userId"));
		json.put("key", requestJson.getString("key"));
		json.put("page", requestJson.getString("page"));
		json.put("pageNum", requestJson.getString("pageNum"));
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, json, requestJson);
	}
	
	private JSONObject querySearchMapsDatas(JSONObject requestJson){
		if (!requestJson.has("id")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		JSONObject json = new JSONObject();
		json.putAll(requestJson);
		json.put("source", Constants.CALL_SOURCE);
		json.put("receive", Constants.CALL_TO_SEARCH);
		json.put("busiCode", requestJson.optString("busiCode"));
		json.put("id", requestJson.getString("id"));
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, json, requestJson);
	}
	
	private JSONObject querySearchMapsDataNodes(JSONObject requestJson){
		if (!requestJson.has("id")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		JSONObject json = new JSONObject();
		json.putAll(requestJson);
		json.put("source", Constants.CALL_SOURCE);
		json.put("receive", Constants.CALL_TO_SEARCH);
		json.put("busiCode", requestJson.optString("busiCode"));
		json.put("id", requestJson.getString("id"));
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, json, requestJson);
	}
	
	private JSONObject addMapNodes(JSONObject requestJson){
		if (!requestJson.has("titleId") || !requestJson.has("title")||!requestJson.has("url")	) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		JSONObject json = new JSONObject();
		json.putAll(requestJson);
		json.put("source", Constants.CALL_SOURCE);
		json.put("receive", Constants.CALL_TO_SEARCH);
		json.put("busiCode", requestJson.optString("busiCode"));
		json.put("titleId", requestJson.getString("titleId"));
		json.put("title", requestJson.getString("title"));
		json.put("url", requestJson.getString("url"));
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, json, requestJson);
	}
	
	private JSONObject querySearchMapKeywordDatas(JSONObject requestJson){
		if (!requestJson.has("keyWord") ||!requestJson.has("page") ||!requestJson.has("pageNum") ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		JSONObject json = new JSONObject();
		json.putAll(requestJson);
		json.put("source", Constants.CALL_SOURCE);
		json.put("receive", Constants.CALL_TO_SEARCH);
		json.put("busiCode", requestJson.optString("busiCode"));
		json.put("keyWord", requestJson.getString("keyWord"));
		json.put("page", requestJson.getString("page"));
		json.put("pageNum", requestJson.getString("pageNum"));
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, json, requestJson);
	}
	
	private JSONObject querySearchLikeKeyword(JSONObject requestJson){
		if (!requestJson.has("key") ||!requestJson.has("userId")  ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		JSONObject json = new JSONObject();
		json.putAll(requestJson);
		json.put("source", Constants.CALL_SOURCE);
		json.put("receive", Constants.CALL_TO_SEARCH);
		json.put("busiCode", requestJson.optString("busiCode"));
		json.put("key", requestJson.getString("key"));
		json.put("userId", requestJson.getString("userId"));
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, json, requestJson);
	}
	/**
	 * 保存搜索条件
	 * @param requestJson
	 * @return
	 */
//	private JSONObject saveSearchKey(JSONObject requestJson) {
//		// 将每一个接口具体的业务参数添加到searchSystemParamObject
//		if (!requestJson.has("userId") || !requestJson.has("keyWord")) {
//			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
//		}
//		JSONObject  searchSystemParamObject = new JSONObject();
//		searchSystemParamObject.put("source", Constants.CALL_SOURCE);
//		searchSystemParamObject.put("receive", Constants.CALL_TO_SEARCH);
//		searchSystemParamObject.put("busiCode", requestJson.optString("busiCode"));
//		searchSystemParamObject.put("userId", requestJson.optString("userId"));
//		searchSystemParamObject.put("keyWord", requestJson.optString("keyWord"));
//		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, searchSystemParamObject, requestJson);	
//	}
	private JSONObject requestGoogleDatas(JSONObject paramJson) {
		if (!paramJson.has("start") || !paramJson.has("queryString") ) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_APP_LACK_PARAM, null, paramJson.optString("language", "EN"));
		}
		JSONObject object = new JSONObject();
		object.put("source", Constants.CALL_SOURCE);
		object.put("receive", Constants.CALL_TO_SEARCH);
		object.put("busiCode", paramJson.optString("busiCode"));
		object.put("start", paramJson.get("start"));
		object.put("queryString", paramJson.get("queryString"));
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, object, paramJson);	
	}
	/**
	 * 保存查看链接
	 * @param requestJson
	 * @return
	 */
//	private JSONObject saveSearchViewed(JSONObject requestJson) {
//		// 将每一个接口具体的业务参数添加到searchSystemParamObject
//		// 必填
//		if (!requestJson.has("userId") || !requestJson.has("url")) {
//			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
//		}
//		JSONObject  searchSystemParamObject = new JSONObject();
//		searchSystemParamObject.put("source", Constants.CALL_SOURCE);
//		searchSystemParamObject.put("receive", Constants.CALL_TO_SEARCH);
//		searchSystemParamObject.put("busiCode", requestJson.optString("busiCode"));
//		searchSystemParamObject.put("userId", requestJson.optString("userId"));
//		searchSystemParamObject.put("url", requestJson.optString("url"));
//		// 选填
//		searchSystemParamObject.put("keyWord", requestJson.optString("keyWord", ""));
//		searchSystemParamObject.put("preUrl", requestJson.optString("preUrl", ""));
//		searchSystemParamObject.put("titleTxt", requestJson.optString("titleTxt", ""));
//		searchSystemParamObject.put("abstractTxt", requestJson.optString("abstractTxt", ""));
//		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, searchSystemParamObject, requestJson);	
//	}

	/**
	 * 查询用户搜索历史
	 * @param requestJson
	 * @return
	 */
//	private JSONObject querySearchKey(JSONObject requestJson) {
//		// 将每一个接口具体的业务参数添加到searchSystemParamObject
//		if (!requestJson.has("userId") || !requestJson.has("pageNum") || !requestJson.has("currPage")) {
//			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
//		}
//		JSONObject  searchSystemParamObject = new JSONObject();
//		searchSystemParamObject.put("source", Constants.CALL_SOURCE);
//		searchSystemParamObject.put("receive", Constants.CALL_TO_SEARCH);
//		searchSystemParamObject.put("busiCode", requestJson.optString("busiCode"));
//		searchSystemParamObject.put("userId", requestJson.optString("userId"));
//		searchSystemParamObject.put("keyWord", requestJson.optString("keyWord"));
//		searchSystemParamObject.put("pageNum", requestJson.optInt("pageNum"));
//		searchSystemParamObject.put("currPage", requestJson.optInt("currPage"));
//		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, searchSystemParamObject, requestJson);	
//	}
	
	/**
	 * 查询搜索条件对应的所有链接
	 * @param requestJson
	 * @return
	 */
//	private JSONObject querySearchViewed(JSONObject requestJson) {
//		// 将每一个接口具体的业务参数添加到searchSystemParamObject
//		if (!requestJson.has("keyWord") || !requestJson.has("pageNum") || !requestJson.has("currPage")) {
//			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
//		}
//		JSONObject  searchSystemParamObject = new JSONObject();
//		searchSystemParamObject.put("source", Constants.CALL_SOURCE);
//		searchSystemParamObject.put("receive", Constants.CALL_TO_SEARCH);
//		searchSystemParamObject.put("busiCode", requestJson.optString("busiCode"));
//		searchSystemParamObject.put("userId", requestJson.optString("userId"));
//		searchSystemParamObject.put("keyWord", requestJson.optString("keyWord", ""));
//		searchSystemParamObject.put("pageNum", requestJson.optInt("pageNum"));
//		searchSystemParamObject.put("currPage", requestJson.optInt("currPage"));
//		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, searchSystemParamObject, requestJson);	
//	}
	
	/**
	 * 保存搜索记录文件
	 * @param requestJson
	 * @return
	 */
//	private JSONObject saveSearchFile(JSONObject requestJson) {
//		// 将每一个接口具体的业务参数添加到searchSystemParamObject
//		if (!requestJson.has("searchFile") || !requestJson.has("fileType")) {
//			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, requestJson.optString("language", "EN"));
//		}
//		//读取hdfs目录下当天的文件,如果没有，直接上传，如果有，将流加到文件后面即可
//		Date date = systemTimeMapper.getSystemTime();
//		String dateStr = Constants.sdf.format(date);
//		requestJson.put("day", dateStr);
//		//上传文件
//		JSONObject  uploadResult = FileUtil.upLoadFolder(requestJson);
//		if (StringUtils.equals("-1", uploadResult.getString("code"))) {
//			logger.error("上传文件异常" + new Exception(uploadResult.toString()).getMessage());
//			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_SAVE_FILE_FAIELD, null, requestJson.optString("language", "EN"));
//		}
//		if (StringUtils.equals("-2", uploadResult.getString("code"))) {
//			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_100, null, requestJson.optString("language", "EN"));
//		}
//		//组装参数，调用子系统写表				
////		searchSystemParamObject.put("searchFile", requestJson.optString("searchFile"));
////		searchSystemParamObject.put("fileType", requestJson.optString("fileType"));
//		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, requestJson.optString("language", "EN"));	
//	}
	/**
	 * 保存分享记录
	 * @param param
	 * @return
	 */
	private JSONObject saveSearchShare(JSONObject param){
		if (StringUtils.isEmpty(param.optString("userId")) || StringUtils.isEmpty(param.optString("mapId"))
				|| StringUtils.isEmpty(param.optString("shareTo")) 
				|| StringUtils.isEmpty(param.optString("picUrl")) ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, param.optString("language"));
		}
		String picName = param.optString("mapId")+(int)(Math.random()*1000)+Constants.PIC_PNG;
		String allPath = ConfigConstants.SHARE_PIC_URL+File.separator+picName;
		String httpPath = FileUtil.saveBase64Pic(param.optString("picUrl"), allPath);
		JSONObject newParam = new JSONObject();
		newParam.put("source", Constants.CALL_SOURCE);
		newParam.put("receive", Constants.CALL_TO_SEARCH);
		newParam.put("busiCode", param.optString("busiCode"));
		newParam.put("userId", param.optString("userId"));
		newParam.put("mapId", param.optString("mapId"));
		newParam.put("shareTo", param.optString("shareTo"));
		newParam.put("keyCode", SearchEnum.Share.toString());
		newParam.put("picUrl", httpPath);
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, newParam, param);	
	}
	
	private JSONObject querySearchShares(JSONObject param){
		if (StringUtils.isEmpty(param.optString("userId")) || StringUtils.isEmpty(param.optString("pageNum"))
				|| StringUtils.isEmpty(param.optString("page")) 
				|| (param.optInt("page")>0 && StringUtils.isEmpty(param.optString("firstTime")))) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, param.optString("language"));
		}
		
		JSONObject newParam = new JSONObject();
		newParam.put("source", Constants.CALL_SOURCE);
		newParam.put("receive", Constants.CALL_TO_SEARCH);
		newParam.put("busiCode", param.optString("busiCode"));
		newParam.put("userId", param.optString("userId"));
		newParam.put("pageNum", param.optString("pageNum"));
		newParam.put("page", param.optString("page"));
		newParam.put("firstTime", param.optString("firstTime"));
		newParam.put("keyCode", SearchEnum.Share.toString());
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, newParam, param);	
	}
	
	
	private JSONObject addSearchSource(JSONObject param){
		if (StringUtils.isEmpty(param.optString("userId")) || StringUtils.isEmpty(param.optString("type")) || StringUtils.isEmpty(param.optString("url"))|| StringUtils.isEmpty(param.optString("name"))) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, param.optString("language"));
		}
		
		JSONObject newParam = new JSONObject();
		newParam.put("source", Constants.CALL_SOURCE);
		newParam.put("receive", Constants.CALL_TO_SEARCH);
		newParam.put("busiCode", param.optString("busiCode"));
		newParam.put("name", param.optString("name"));
		newParam.put("url", param.optString("url"));
		newParam.put("type", param.optString("type"));
		newParam.put("userId", param.optString("userId"));
		newParam.put("keyCode", SearchEnum.Source.toString());
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, newParam, param);	
	}
	
	private JSONObject delSearchSource(JSONObject param){
		if (StringUtils.isEmpty(param.optString("id")) ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, param.optString("language"));
		}
		
		JSONObject newParam = new JSONObject();
		newParam.put("source", Constants.CALL_SOURCE);
		newParam.put("receive", Constants.CALL_TO_SEARCH);
		newParam.put("busiCode", param.optString("busiCode"));
		newParam.put("id", param.optString("id"));
		newParam.put("keyCode", SearchEnum.Source.toString());
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, newParam, param);	
	}
	
	private JSONObject querySearchSource(JSONObject param){
		if (StringUtils.isEmpty(param.optString("userId")) ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SEARCH_LACK_PARAM, null, param.optString("language"));
		}
		
		JSONObject newParam = new JSONObject();
		newParam.put("source", Constants.CALL_SOURCE);
		newParam.put("receive", Constants.CALL_TO_SEARCH);
		newParam.put("busiCode", param.optString("busiCode"));
		newParam.put("userId", param.optString("userId"));
		newParam.put("keyCode", SearchEnum.Source.toString());
		return interfaceInvoking.invoking(ConfigConstants.URL_SEARCH_SYSTEM, newParam, param);	
	}
	
	
}
