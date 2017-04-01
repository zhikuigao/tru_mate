package com.jws.app.operater.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.jws.app.operater.data.SourceMatesearchMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.SourceMatesearch;
import com.jws.app.operater.service.SourceMatesearchService;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.constant.Constants;
import com.jws.common.util.JiveGlobe;
import com.jws.common.util.ResultPackaging;
@Service("SourceMatesearchService")
public class SourceMatesearchServiceImpl implements SourceMatesearchService {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private SystemTimeMapper systemTimeMapper;
	@Resource
	private SourceMatesearchMapper sourceMatesearchMapper;

	@Override
	public JSONObject entry(JSONObject param) throws Exception {
		if (StringUtils.equals(BusiCodeConstant.ADD_MATE_SOURCE, param.optString("busiCode"))) {
			return addSearchSource(param);
		}else if (StringUtils.equals(BusiCodeConstant.QUERY_MATE_SOURCE, param.optString("busiCode"))) {
			return querySearchSource(param);
		}else if (StringUtils.equals(BusiCodeConstant.DELETE_MATE_SOURCE, param.optString("busiCode"))) {
			return delSearchSource(param);
		}
		return null;
	}

	private JSONObject delSearchSource(JSONObject param) {
		if (!param.has("id")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, param.optString("language", "EN"));
		}
		try { 
			String id = param.getString("id");
			 this.sourceMatesearchMapper.delSearchSource(id);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, param.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("删除源异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_KEY_FAILED, null, param.optString("language", "ZH"));
		}
	}

	private JSONObject querySearchSource(JSONObject param) {
		if (!param.has("userId")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, param.optString("language", "EN"));
		}
		try { 
			String userId = param.getString("userId");
			//查询出用户是否已经存在重复的源
			List<SourceMatesearch> sm = this.sourceMatesearchMapper.querySearchSource(userId);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, sm, param.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("查询源异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_KEY_FAILED, null, param.optString("language", "ZH"));
		}
	}

	private JSONObject addSearchSource(JSONObject param) {
		if (!param.has("name")||StringUtils.isEmpty(param.getString("url")) ||StringUtils.isEmpty(param.getString("type"))||StringUtils.isEmpty(param.getString("userId"))) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, param.optString("language", "EN"));
		}
		try { 
			String name = param.getString("name");
			String url = param.getString("url");
			String type = param.getString("type");
			String userId = param.getString("userId");
			//查询出用户是否已经存在重复的源
			int count   = this.sourceMatesearchMapper.userSourceCount(name, userId);
			if(count>0){
				return ResultPackaging.dealJsonObject( Constants.EXIST_DEFINED, Constants.EXIST_DEFINED, null, param.optString("language", "ZH"));
			}
			SourceMatesearch sm = new SourceMatesearch();
			String romString = JiveGlobe.getFromRom();
			sm.setCreateTime(systemTimeMapper.getSystemTime());
			sm.setDeleteFlag("1");
			sm.setId(romString);
			sm.setName(name);
			sm.setType(Integer.valueOf(type));
			sm.setUrl(url);
			sm.setUserId(userId);
			this.sourceMatesearchMapper.insert(sm);
			return ResultPackaging.dealJsonObjectString( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, romString, param.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("增加源异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_KEY_FAILED, null, param.optString("language", "ZH"));
		}
	}
}