package com.jws.app.operater.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jws.app.operater.data.SearchMapShareMapper;
import com.jws.app.operater.data.SearchMapsMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.SearchMapShare;
import com.jws.app.operater.model.SearchMaps;
import com.jws.app.operater.service.SearchMapShareService;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.constant.ConfigConstants;
import com.jws.common.constant.Constants;
import com.jws.common.util.ResultPackaging;
@Service("searchMapShareService")
public class SearchMapShareServiceImpl implements SearchMapShareService{
	@Resource
	private SearchMapShareMapper searchMapShareMapper;
	@Resource
	private SearchMapsMapper searchMapsMapper;
	@Resource
	private SystemTimeMapper systemTimeMapper;

	@Override
	public JSONObject entry(JSONObject param) throws Exception {
		if (StringUtils.equals(BusiCodeConstant.SAVE_SEARCH_SHARE, param.optString("busiCode"))) {
			return saveSearchShare(param);
		}else if (StringUtils.equals(BusiCodeConstant.QUERY_SEARCH_SHARES, param.optString("busiCode"))) {
			return querySearchShares(param);
		}
		return null;
	}
	
	private JSONObject querySearchShares(JSONObject param) throws Exception{
		if (StringUtils.isEmpty(param.optString("userId")) || StringUtils.isEmpty(param.optString("pageNum"))
				|| StringUtils.isEmpty(param.optString("page")) 
				|| (param.optInt("page")>0 && StringUtils.isEmpty(param.optString("firstTime")))) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, param.optString("language"));
		}
		JSONObject result = new JSONObject();
		String userId = param.getString("userId");
		Integer page = param.optInt("page");
		List<SearchMapShare> list = new ArrayList<>();
		SearchMapShare record = new SearchMapShare();
		record.setPageNum(param.optInt("pageNum"));
		record.setUserId(userId);
		if (page<0) {
			page=0;
		}
		record.setPageFrom(page*param.optInt("pageNum"));
		Integer total = 0;
		String firstTime = param.optString("firstTime");
		Date newTime = null;
		if (page>0) {
			total = searchMapShareMapper.getTotalNumber(userId,Constants.df.parse(firstTime));
			result.put("firstTime", param.optString("firstTime"));
		}else{
			newTime = systemTimeMapper.getSystemTime();
			total = searchMapShareMapper.getTotalNumber(userId,newTime);
			result.put("firstTime", Constants.df.format(newTime));
		}
		list = searchMapShareMapper.queryShareByPage(record);
		result.put("page", ++page);
		result.put("total", total);
		if (list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				if (StringUtils.isNotEmpty(list.get(i).getThumbnailUrl())) {
					list.get(i).setThumbnailUrl(ConfigConstants.HTTP_VISIT+list.get(i).getThumbnailUrl());
				}
			}
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			String gsont = gson.toJson(list);	
			result.put("list", gsont);
		}else{
			result.put("list", list);
		}
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, result, param.optString("language"));
	}
	
	private JSONObject saveSearchShare(JSONObject param){
		if (!param.has("userId") || !param.has("mapId") || !param.has("shareTo") || !param.has("picUrl")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, param.optString("language"));
		}
		//查询对应的用户信息
		int count = systemTimeMapper.existUser(param.getString("userId"));
		if (count<1) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.NO_USER, null, param.optString("language"));
		}
		//查询对应的地图信息
		SearchMaps map = searchMapsMapper.queryMapById(param.getString("mapId"));
		if (null == map) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.NOT_FOUND_MAP, null, param.optString("language"));
		}
		SearchMapShare share = new SearchMapShare();
		share.setUserId(param.getString("userId"));
		share.setFirstKeyword(map.getFirstKeyword());
		share.setMapId(param.getString("mapId"));
		share.setShareTo(param.getString("shareTo"));
		if (StringUtils.equals(Constants.DEVICE_PC, param.optString("device"))) {
			share.setShareDevice("1");
		}else if(StringUtils.equals(Constants.DEVICE_ANDROID, param.optString("device"))){
			share.setShareDevice("2");
		}else{
			share.setShareDevice("3");
		}
		share.setPicUrl(param.optString("picUrl"));
		share.setThumbnailUrl(param.optString("picUrl"));
		//保存分享信息
		searchMapShareMapper.insert(share);
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, param.optString("language"));
	}
	

}
