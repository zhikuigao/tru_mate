package com.jws.app.operater.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jws.app.operater.data.AppVersionMapper;
import com.jws.app.operater.data.McPushContentMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.AppVersion;
import com.jws.app.operater.service.OtherService;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.constant.Constants;
import com.jws.common.util.ResultPackaging;
@Service("messageService")
public class MessageServiceImpl implements OtherService{
	
	@Resource
	private SystemTimeMapper systemTimeMapper;
	@Resource
	private AppVersionMapper appVersionMapper;
	@Resource
	private McPushContentMapper mcPushContentMapper;

	@Override
	public JSONObject entry(JSONObject param) {
		JSONObject returnObject = new JSONObject();
		if (StringUtils.equals(param.get("busiCode").toString(), BusiCodeConstant.QUERY_SYS_INFO)) {
			//查询系统消息
			returnObject =this.querySysInfo(param); 
		}else if(StringUtils.equals(param.get("busiCode").toString(), BusiCodeConstant.QUERY_SYS_CONTENT)){
			//查询系统消息详情
			returnObject =this.querySysContent(param); 
		}
		return returnObject;
	}
	
	private JSONObject querySysContent(JSONObject param){
		if (!param.has("sysId") ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}
		HashMap<String, Object> sysInfo = appVersionMapper.getVersionInfo(param.getString("sysId"));
		if (null==sysInfo || StringUtils.equals("1", sysInfo.get("status").toString())) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1122, Constants.RESULT_CODE_1122, null, param.getString("language"));
		}
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, sysInfo, param.getString("language"));
	}
	
	private JSONObject querySysInfo(JSONObject param){
		if (!param.has("page") || !param.has("pageNum") ||  !param.has("lastTime") ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}
		Integer pageNum = param.getInt("pageNum");
		JSONObject result = new JSONObject();
		//1.pc
		if (StringUtils.equals(Constants.DEVICE_PC, param.getString("device"))) {
			String lastTime = param.getString("lastTime");
			List<HashMap<String, Object>> list = mcPushContentMapper.getSystemByPage(0, pageNum, lastTime);
			if (list.size()>0) {
				JSONArray array = new JSONArray();
				for (int i = 0; i < list.size(); i++) {
					array.add(list.get(i).get("content"));
				}
				result.put("list", array);
			}else{
				result.put("list", list);
			}
		}else{
			//2.android移动端
			Integer page = param.getInt("page");
			if (page<0) {
				page=0;
			}
			Integer pageFrom = page*pageNum;
			List<HashMap<String, Object>> list = new ArrayList<>();
			AppVersion version= new AppVersion();
			version.setUseType(param.getString("device"));
			version.setFileType("0");
			int total = appVersionMapper.getTotalVersion(version);
			result.put("total", total);
			version.setPageFrom(pageFrom);
			version.setPageNum(pageNum);
			list = appVersionMapper.getVersionByPage(version);
			if (list.size()>0) {
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
				result.put("list", gson.toJson(list));
			}else{
				result.put("list", list);
			}
			result.put("page", ++page);
		}
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, result, param.getString("language"));
	}

}
