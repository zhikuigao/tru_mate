package com.jws.app.operater.serviceBlock.plugIn.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jws.app.operater.service.impl.InterfaceInvoking;
import com.jws.app.operater.serviceBlock.plugIn.service.PlugInService;
import com.jws.common.constant.BlockBusiCodeConstant;
import com.jws.common.constant.ConfigConstants;
import com.jws.common.constant.Constants;
import com.jws.common.constant.PlugInEnum;
import com.jws.common.constant.UserEnum;
import com.jws.common.util.AESUtil;
import com.jws.common.util.DateUtil;
import com.jws.common.util.ResultPackaging;
@Service("plugInService")
public class PlugInServiceImpl implements PlugInService{
	
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private InterfaceInvoking interfaceInvoking;

	@Override
	public JSONObject entry(JSONObject param) throws Exception {
		if (StringUtils.equals(param.optString("busiCode"), BlockBusiCodeConstant.CREATE_URL)) {
			return createUrl(param);
		}else{
			//找不到对应的业务方法
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_100, null, param.optString("language", "ZH"));
		}
	}
	
	private JSONObject createUrl(JSONObject param) throws Exception {
		if (!param.has("userId") || !param.has("plugName") || (!StringUtils.equals(param.optString("plugName"), PlugInEnum.gitLab.toString())
				&& !StringUtils.equals(param.optString("plugName"), PlugInEnum.redMine.toString()))) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_111, null, param.optString("language", "ZH"));
		}
		StringBuffer buffer = new StringBuffer();
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append(param.get("plugName")).append("/")
		.append(ConfigConstants.MC_APP_ID).append("/")
		.append(param.get("userId"));
		//参数加密
		String encrypt = null;
		try {
			encrypt = AESUtil.encrypt(paramBuff.toString(), param.get("plugName").toString());
		} catch (Exception e) {
			logger.error("url加密异常:"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_FAIL, null, param.optString("language", "ZH"));
		}
		if (StringUtils.equals(param.getString("plugName"), Constants.GIT_LAB)) {
			buffer.append(ConfigConstants.PLUG_IN_GITLAB);
		}
		if (StringUtils.equals(param.getString("plugName"), Constants.RED_MINE)) {
			buffer.append(ConfigConstants.PLUG_IN_REDMINE);
		}
		buffer.append("?")
		.append(URLEncoder.encode(encrypt,"utf-8"));
		return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, buffer, param.optString("language", "ZH"));
	}

	@Override
	public JSONObject accessPoint(HttpServletRequest request, String plugName) throws IOException {
		String path =  request.getQueryString();
		try {
			path = AESUtil.decrypt(URLDecoder.decode(path, "utf-8"), plugName);
		} catch (Exception e) {
			logger.error("url参数解密异常:"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_FAIL, null, "ZH");
		}
		String[] pathInfo = path.split("/");
		if (pathInfo.length != 3) {
			return ResultPackaging.dealJsonObject( Constants.URL_ERROR, Constants.URL_ERROR, null, "ZH");
		}
		InputStream is= null; 
		String contentStr=""; 
		try {
			is = request.getInputStream();
			contentStr= IOUtils.toString(is, "utf-8");
		} catch (IOException e) {
			logger.error("获取body内信息失败:"+e.getMessage());
		}
		if (null == contentStr || StringUtils.isEmpty(contentStr)) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.GET_BODY_FAIL, null, "ZH");
		}
		//获取有用的字段值
		JSONObject bodyJson = new JSONObject();
		if (StringUtils.equals(plugName, Constants.GIT_LAB)) {
			bodyJson = packageGitLibData(contentStr, plugName);
		}
		if (StringUtils.equals(plugName, Constants.RED_MINE)) {
			bodyJson = packageRedMineData(contentStr, plugName);
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("keyCode", UserEnum.PlugIn);
		newMap.put("busiCode", BlockBusiCodeConstant.TO_SEND_MESSAGE);
		newMap.put("pathInfo", pathInfo);
		newMap.put("bodyJson", bodyJson);
		newMap.put("device", "pc");
		newMap.put("language", "ZH");
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, null);	
	}
	
	private JSONObject packageRedMineData(String hook, String plugName) throws IOException{
		JSONObject hookObject = JSONObject.fromObject(hook);
		JSONObject payload = hookObject.optJSONObject("msgText").optJSONObject("payload");
		String assignee = "";
		if (payload.has("assignee") && StringUtils.isNotEmpty(payload.optJSONObject("assignee").getString("lastname"))) {
			assignee = payload.optJSONObject("assignee").getString("firstname")+payload.optJSONObject("assignee").getString("lastname");
		}else{
			assignee = payload.optJSONObject("author").getString("firstname")+payload.optJSONObject("author").getString("lastname");
		}
		StringBuffer  content = new StringBuffer();
		content.append("项目: ")
		.append(payload.optJSONObject("project").get("name")).append("\n ")
		.append("主题: ")
		.append(payload.optJSONObject("issue").get("subject")).append("\n ")
		.append("负责人: ")
		.append(assignee).append("\n ")
		.append("任务状态: ")
		.append(payload.optJSONObject("status").get("name")).append("\n ");
		// 拼装message的文字内容
		JSONObject gitlabMessage = new JSONObject();
		gitlabMessage.put("title", Constants.RED_MINE+"机器人");
		gitlabMessage.put("url", payload.get("url"));
		gitlabMessage.put("plugName", plugName);
		gitlabMessage.put("content", content.toString());
		return gitlabMessage;
	}
	
	private JSONObject packageGitLibData(String hook, String plugName) throws IOException{
		JSONObject hookObject = JSONObject.fromObject(hook);
		JSONObject projectObject = hookObject.optJSONObject("project");
		JSONArray commitsArray = hookObject.getJSONArray("commits");
		// 拼装message的文字内容
		JSONObject gitlabMessage = new JSONObject();
		gitlabMessage.put("title", Constants.GIT_LAB+"机器人");
		StringBuffer  content = new StringBuffer();
		content.append("提交人:")
		.append(hookObject.optString("user_name"))
		.append("\n项目名称:")
		.append(projectObject.optString("name"));
		// commit信息
		if (!commitsArray.isEmpty()) {
			for (int i = 0, size = commitsArray.size(); i < size; i++) {
				JSONObject commitObject = commitsArray.optJSONObject(i);
				if (i==0) {
					gitlabMessage.put("url", commitObject.optString("url"));
					content.append("\n提交时间:")
					.append(DateUtil.getDateString(DateUtil.getUtilDate(commitObject.optString("timestamp"), "yyyy-MM-dd'T'HH:mm:ss")))
					.append("\n");
				}
				int j=i+1;
				content.append("备注")
				.append(j)
				.append(":")
				.append(commitObject.optString("message"));
				if (i<size-1) {
					content.append("\n");
				}
			}
		}else{
			gitlabMessage.put("url", "");
		}
		gitlabMessage.put("plugName", plugName);
		gitlabMessage.put("content", content.toString());
		return gitlabMessage;
	}
	
//	private JSONObject packageData(String hook){
//		JSONObject hookObject = JSONObject.fromObject(hook);
//		JSONObject projectObject = hookObject.optJSONObject("project");
//		JSONArray commitsArray = hookObject.getJSONArray("commits");
//		// 拼装message的文字内容
//		JSONObject gitlabMessage = new JSONObject();
//		
//		gitlabMessage.put("user_name", hookObject.optString("user_name"));
//		gitlabMessage.put("object_kind", hookObject.optString("object_kind"));
//		gitlabMessage.put("total_commits_count", hookObject.optInt("total_commits_count"));
//		
//		// 分支信息
//		JSONObject refObj = new JSONObject();
//		String refName = StringUtils.substringAfterLast(hookObject.optString("ref"), "/");
//		refObj.put("name", refName);
//		refObj.put("ref_url", projectObject.optString("web_url") + "/commits/" + refName);
//		gitlabMessage.put("ref", refObj);
//		
//		// 项目信息
//		JSONObject projectObj = new JSONObject();
//		projectObj.put("name", projectObject.optString("name"));
//		projectObj.put("web_url", projectObject.optString("web_url"));
//		gitlabMessage.put("project", projectObj);
//		
//		// commit信息
//		JSONArray commitArr = new JSONArray();
//		JSONObject commitObj = new JSONObject();
//		JSONObject commitObject = new JSONObject();
//		for (int i = 0, size = commitsArray.size(); i < size; i++) {
//			commitObject = commitsArray.optJSONObject(i);
//			commitObj.put("id", StringUtils.substring(commitObject.optString("id"), 0, 8));
//			commitObj.put("message",commitObject.optString("message"));
//			commitObj.put("url", commitObject.optString("url"));
//			commitObj.put("timestamp", DateUtil.getDateString(DateUtil.getUtilDate(commitObject.optString("timestamp"), "yyyy-MM-dd'T'HH:mm:ss")));
//			commitObj.put("author_name", commitObject.optJSONObject("author").optString("name"));
//			commitArr.add(commitObj);
//		}
//		gitlabMessage.put("commits", commitArr);
//		return gitlabMessage;
//	}

}
