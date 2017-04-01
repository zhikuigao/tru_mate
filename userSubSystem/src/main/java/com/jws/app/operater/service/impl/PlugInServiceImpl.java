package com.jws.app.operater.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jws.app.operater.data.McPushContentMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.McPushContent;
import com.jws.app.operater.service.OtherService;
import com.jws.app.operater.service.UserSubService;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.constant.ConfigConstants;
import com.jws.common.constant.Constants;
import com.jws.common.util.ResultPackaging;
@Service("plugInService")
public class PlugInServiceImpl implements OtherService{
	
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private UserSubService userSubService;
	@Resource
	private SystemTimeMapper systemTimeMapper;
	@Resource
	private McPushContentMapper mcPushContentMapper;

	@Override
	public JSONObject entry(JSONObject param) {
		JSONObject returnObject = new JSONObject();
		if (StringUtils.equals(param.get("busiCode").toString(), BusiCodeConstant.TO_SEND_MESSAGE)) {
			returnObject =this.toSendMessage(param); 
		}
		return returnObject;
	}
	
	private JSONObject toSendMessage(JSONObject param){
		JSONArray pathInfo  = param.getJSONArray("pathInfo");
		JSONObject contentJson = param.getJSONObject("bodyJson");
		if (StringUtils.equals(pathInfo.getString(0), Constants.GIT_LAB)) {
			contentJson.put("msgType", "4");
		}
		if (StringUtils.equals(pathInfo.getString(0), Constants.RED_MINE)) {
			contentJson.put("msgType", "5");
		}
		Date currentTime = systemTimeMapper.getSystemTime();
		contentJson.put("pushTime", Constants.df.format(currentTime));
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		contentJson.put("msgId", id);
//		contentJson.put("id", id);
		contentJson.put("effectiveTime", ConfigConstants.PLUGIN_EFFEC_TIME);
		McPushContent content = new McPushContent();
		content.setId(id);
		content.setUserId(pathInfo.getString(2));
		content.setMsgContent(contentJson.toString());
		content.setPlanTime(currentTime);
		content.setMsgSubType(pathInfo.getString(0));
		content.setMsgType(contentJson.getString("msgType"));
		content.setPushType("1");
		List<McPushContent> list = new ArrayList<>();
		list.add(content);
		try {
			mcPushContentMapper.batchInsertPushMsg(list);
		} catch (Exception e) {
			logger.error("第三方接入消息保存异常："+e.getMessage());
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_FAIL, null, "ZH");
		}
		//处理消息
		JSONObject msgParam = new JSONObject();
		msgParam.put("requestCode", "message_send");
		msgParam.put("appToken", "83c39424-7e5b-4416-88c1-2d6d6e54b794");
		msgParam.put("receiverToken", "");
		msgParam.put("sendChaterId", "");
		msgParam.put("msgQos", "1");
		msgParam.put("msgRetain", "1");
		msgParam.put("url", ConfigConstants.MESSAGE_CENTER);
		msgParam.put("language", "ZH");
		msgParam.put("msgTopic", pathInfo.getString(2));
		msgParam.put("msgText", contentJson.toString());
		msgParam.put("msgId", id);
		//推送消息
		String flag = userSubService.sendMsg(msgParam);
		//根据推送结果，处理数据
		if (StringUtils.equals(flag, "200")) {
			List<String> ids = new ArrayList<>();
			ids.add(id);
			mcPushContentMapper.batchUpdatePushStatus(ids);
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, "ZH");
		}
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1121, null, "ZH");
	}

}
