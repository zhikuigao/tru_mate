package com.jws.app.operater.service.impl;

import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jws.app.operater.data.SysUserLogMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.model.SysUserLog;
import com.jws.app.operater.model.UserInfo;
import com.jws.app.operater.service.OtherService;
import com.jws.app.operater.service.UserBusiService;
import com.jws.app.operater.service.UserService;
import com.jws.app.operater.service.UserSubService;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.constant.ConfigConstants;
import com.jws.common.constant.Constants;
import com.jws.common.constant.UserEnum;
import com.jws.common.util.JiveGlobe;
import com.jws.common.util.MD5Util;
import com.jws.common.util.ResultPackaging;
@Service("userService")
public class UserServiceImpl  implements UserService{
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private UserBusiService userBusiService;
	@Resource
	private  OtherService plugInService;
	@Resource
	private  OtherService messageService;
	@Resource
	private SystemTimeMapper systemTimeMapper;
	@Resource
	private SysUserLogMapper sysUserLogMapper;
	@Resource
	private UserSubService userSubService;
	@Resource
	private UserInfoMapper userInfoMapper;
	@Override
	public JSONObject commonEntry(String param) {
		JSONObject returnJson = new JSONObject();
		//1.记录调用日志
		SysUserLog  log = this.addCallLog(param);
		//解析参数
		JSONObject  paramJson = new JSONObject();
		JSONObject  securityJson = new JSONObject();
		JSONObject  paramMap = new JSONObject();
		try {
			paramJson =JSONObject.fromObject(param);
			paramMap = JSONObject.fromObject(paramJson.get("paramMap"));
			securityJson =  (JSONObject) paramJson.get("securityJson");
		} catch (Exception e) {
			logger.error("参数转换失败："+e.getMessage());
			returnJson = ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1101, null, "EN");
			this.updateCallLog(returnJson, log, null);
			return  returnJson;
		}
		//参数验证
		if (!securityJson.has("time") || !securityJson.has("Md5Str") ||!paramMap.has("busiCode") || !paramMap.has("language") || 
				 !paramMap.has("source") || !paramMap.has("device")) {
			returnJson = ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, "EN");
			this.updateCallLog(returnJson, log, null);
			return  returnJson;
		}
		//2.安全校验
		securityJson.put("language", paramMap.get("language"));
		JSONObject securityObject = securityVerification(securityJson);
		if (null != securityObject) {
			this.updateCallLog(securityObject, log, paramMap);
			return  securityObject;
		}		
		//3.子业务跳转
		try {
			if (StringUtils.equals(paramMap.get("busiCode").toString(), BusiCodeConstant.USER_SEND_VERIFY_CODE)) {		
				//发送验证码
				returnJson = userBusiService.sendVerifyCode(paramMap);
			}else if (StringUtils.equals(paramMap.get("busiCode").toString(), BusiCodeConstant.USER_REGISTER)) {		
				//注册
				returnJson = userBusiService.register(paramMap);
			}else if (StringUtils.equals(paramMap.get("busiCode").toString(), BusiCodeConstant.USER_LOGIN)) {	
				//登录
				returnJson = userBusiService.login(paramMap);
			}else if (StringUtils.equals(paramMap.get("busiCode").toString(), BusiCodeConstant.SELECT_PUSH_TYPE)) {	
				//查询消息(废弃)
				returnJson = userSubService.selectPushType(paramMap);
			}else if (StringUtils.equals(paramMap.get("busiCode").toString(), BusiCodeConstant.SELECT_PUSH_CONTENTALL)) {	
				//查询所有消息
				returnJson = userSubService.selectPushContentAll(paramMap);
			}else if (StringUtils.equals(paramMap.get("busiCode").toString(), BusiCodeConstant.SELECT_PUSH_INNOVATIONTYPE)) {	
				//查询消息类型
				returnJson = userSubService.selectPushInnovationType(paramMap);
			}else if (StringUtils.equals(paramMap.get("busiCode").toString(), BusiCodeConstant.QUERY_PERSON)) {	
				//查询个人信息
				returnJson = queryPerson(paramMap);
			}else if (StringUtils.equals(paramMap.get("busiCode").toString(), BusiCodeConstant.UPDATE_PERSON)) {	
				//更新个人信息
				returnJson = updatePerson(paramMap);
			}else if (StringUtils.equals(paramMap.get("keyCode").toString(), UserEnum.User.toString())) {	
				//用户相关其他统一入口
				returnJson = userBusiService.entry(paramMap);
			}else if (StringUtils.equals(paramMap.get("keyCode").toString(), UserEnum.PlugIn.toString())) {	
				//第三方插件
				returnJson = plugInService.entry(paramMap);
			}else if (StringUtils.equals(paramMap.get("keyCode").toString(), UserEnum.Message.toString())) {	
				//消息
				returnJson = messageService.entry(paramMap);
			}else{
				returnJson = ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1107, null, paramMap.getString("language"));
				this.updateCallLog(returnJson, log, paramMap);
				return returnJson;
			}
		} catch (Exception e) {
			logger.error("程序异常"+e.getMessage());
			returnJson = ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1104, null, paramMap.getString("language"));
		}
		this.updateCallLog(returnJson, log, paramMap);
		return returnJson;
	}
	
	private JSONObject  securityVerification(JSONObject  securityJson){
		String time = securityJson.getString("time");
		String Md5Str = securityJson.getString("Md5Str");
		String key = ConfigConstants.SECURITY_KEY;
		String newMd5Str = MD5Util.getMD5String(key+time);
		if (!StringUtils.equals(Md5Str, newMd5Str)) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1103, null, securityJson.getString("language"));
		}
		return  null;
	}
	
	private  SysUserLog  addCallLog(String param){
		try {
			SysUserLog  log = new SysUserLog();
			log.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			log.setRequest(param);
			log.setReceive("usersystem");
//			log.setCreateTime(systemTimeMapper.getSystemTime());
			sysUserLogMapper.userLogInsert(log);
			return log;
		} catch (Exception e) {
			logger.error("记录日志异常"+e.getMessage());
		}
		return null;
	}
	
	private  void  updateCallLog(JSONObject  returnJson, SysUserLog  oldLog , JSONObject paramMap){
		if (null != oldLog.getId()) {
			try {
				oldLog.setResponse(String.valueOf(returnJson));
				if (null != paramMap) {
					oldLog.setSource(paramMap.getString("source"));
				}
				sysUserLogMapper.userLogUpdateByPrimaryKey(oldLog);
			} catch (Exception e) {
				logger.error("更新日志异常"+e.getMessage());
			}
		}
		
	}

	@Override
	public void monitorSysNotice() {
		JSONObject logParam = new JSONObject();
		logParam.put("source", "recommendNotice");
		logParam.put("receive", "recommend");
//		//1.记录调用日志
		SysUserLog  log = this.addCallLog("");
		//监控
		JSONObject result = userBusiService.monitorSysNotice();
		//更新日志
		if (null != log) {
			this.updateCallLog(result, log, logParam);
		}
	}

	@Override
	public void monitorSysUpgrade() {
		JSONObject logParam = new JSONObject();
		logParam.put("source", "recommendUpgrade");
		logParam.put("receive", "recommend");
		//1.记录调用日志
		SysUserLog  log = this.addCallLog("");
		//监控
		JSONObject result = userBusiService.monitorSysUpgrade();
		//更新日志
		if (null != log) {
			this.updateCallLog(result, log, logParam);
		}
	}

	@Override
	public void monitorTimingPush() {
		JSONObject logParam = new JSONObject();
		logParam.put("source", "recommendTimingPush");
		logParam.put("receive", "recommend");
		//1.记录调用日志
		SysUserLog  log = this.addCallLog("");
		//监控
		JSONObject result = userBusiService.monitorTimingPush();
		//更新日志
		if (null != log) {
			this.updateCallLog(result, log, logParam);
		}
	}

	@Override
	public void pushTimingData() {
		JSONObject logParam = new JSONObject();
		logParam.put("source", "pushTimingData");
		logParam.put("receive", "recommend");
		//1.记录调用日志
		SysUserLog  log = this.addCallLog("");
		//监控
		JSONObject result = userBusiService.monitorPushData();
		//更新日志
		if (null != log) {
			this.updateCallLog(result, log, logParam);
		}
	}
	
	@Override
	public void pushFailData(){
		JSONObject logParam = new JSONObject();
		logParam.put("source", "pushFailData");
		logParam.put("receive", "recommend");
		//1.记录调用日志
		SysUserLog  log = this.addCallLog("");
		//监控
		JSONObject result = userBusiService.monitorFailData();
		//更新日志
		if (null != log) {
			this.updateCallLog(result, log, logParam);
		}
	}
	
	private JSONObject  queryPerson(JSONObject  securityJson){
		if (!securityJson.has("id") ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, securityJson.getString("language"));
		}
		try {
			UserInfo ui = this.userInfoMapper.queryPerson(securityJson.getString("id"));
			if(JiveGlobe.isEmpty(ui)){
				return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_1122, Constants.RESULT_CODE_1122, ui, securityJson.getString("language"));
			}
			if (StringUtils.isNotEmpty(ui.getUserPhoto())) {
				ui.setUserPhoto(ConfigConstants.FILE_SERVER+ui.getUserPhoto());
			}
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, ui, securityJson.getString("language"));
		} catch (Exception e) {
			logger.error("获取个人信息异常"+e);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_1100, Constants.RESULT_CODE_1100, null, securityJson.getString("language"));
		}
		
	}
	
	private JSONObject  updatePerson(JSONObject  securityJson){
		if (!securityJson.has("id") ||!securityJson.has("professional") ||!securityJson.has("nickname")  ||!securityJson.has("industry")  ||!securityJson.has("sex")  ||!securityJson.has("birthday") ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, securityJson.getString("language"));
		}
		try {
			String id = securityJson.getString("id");
			String nickname = securityJson.getString("nickname");
			String professional = securityJson.getString("professional");
			String industry = securityJson.getString("industry");
			String sex=securityJson.getString("sex");
			String birthday = securityJson.getString("birthday");
			if(JiveGlobe.isEmpty(professional)){
				professional=null;
			}
			if(JiveGlobe.isEmpty(industry)){
				industry=null;
			}
			if(JiveGlobe.isEmpty(nickname)){
				nickname=null;
			}
			if(JiveGlobe.isEmpty(sex)){
				sex=null;
			}
			if(JiveGlobe.isEmpty(birthday)){
				birthday=null;
			}
			UserInfo uf = new UserInfo();
			uf.setId(id);
			uf.setSex(sex);
			uf.setBirthday(JiveGlobe.StrToDate(birthday));
			uf.setProfessional(professional);
			uf.setIndustry(industry);
			uf.setNickname(nickname);
			this.userInfoMapper.updatePerson(uf);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, securityJson.getString("language"));
		} catch (Exception e) {
			logger.error("获取个人信息异常"+e);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_1100, Constants.RESULT_CODE_1100, null, securityJson.getString("language"));
		}
		
	}
	
}
