package com.jws.app.operater.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jws.app.operater.data.AppVersionMapper;
import com.jws.app.operater.data.EmailVerifyMapper;
import com.jws.app.operater.data.McPushContentMapper;
import com.jws.app.operater.data.SysNoticeMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.model.AppRecommend;
import com.jws.app.operater.model.AppVersion;
import com.jws.app.operater.model.EmailVerify;
import com.jws.app.operater.model.McPushContent;
import com.jws.app.operater.model.NewsRecommend;
import com.jws.app.operater.model.SysNotice;
import com.jws.app.operater.model.UserInfo;
import com.jws.app.operater.service.UserBusiService;
import com.jws.app.operater.service.UserSubService;
import com.jws.app.operater.service.UserTokenService;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.constant.ConfigConstants;
import com.jws.common.constant.Constants;
import com.jws.common.util.DiscuzUtil;
import com.jws.common.util.JiveGlobe;
import com.jws.common.util.RandomUtil;
import com.jws.common.util.ResultPackaging;
import com.jws.common.util.SendEmailVerifyCode;
import com.jws.common.util.SendPhoneVerifyCode;
@Service("userBusiService")
public class UserBusiServiceImpl implements UserBusiService{
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private UserInfoMapper userInfoMapper;
	@Resource
	private EmailVerifyMapper emailVerifyMapper;
	@Resource
	private SystemTimeMapper systemTimeMapper;
	@Resource
	private UserSubService userSubService;
	@Resource
	private AppVersionMapper appVersionMapper;
	@Resource
	private SysNoticeMapper sysNoticeMapper;
	@Resource
	private McPushContentMapper mcPushContentMapper;
	@Resource
	private UserTokenService userTokenService;
	
	private static String language = "ZH";

	/**
	 * 发送验证码
	 */
	@Override
	public JSONObject sendVerifyCode(JSONObject param) {
		//验证参数
		if (!param.has("account") || !param.has("type") ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}
		//忘记密码  无需校验
		//是否已注册
		UserInfo userInfo = new UserInfo();
		userInfo.setType(param.getString("type"));
		if (StringUtils.equals("email", param.getString("type"))) {
			userInfo.setEmail(param.getString("account"));
		}
		if (StringUtils.equals("phone", param.getString("type"))) {
			userInfo.setPhone(param.getString("account"));
		}
		int  countAccount =  userInfoMapper.countAccount(userInfo);
		if (param.has("forgot")) {
			if (countAccount==0) {
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1115, Constants.RESULT_CODE_1115, null, param.getString("language"));
			}
		}else{
			if (countAccount>0) {
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1105, Constants.RESULT_CODE_1105, null, param.getString("language"));
			}
		}
		String code = "";
		try {
			//生成验证码
			code = RandomUtil.toFixdLengthString(6);
			if (StringUtils.equals("email", param.getString("type"))) {
				//发送邮件
				SendEmailVerifyCode.SendEmailVerifyCode(param.getString("account"), code, param.getString("language"));
			}
			if (StringUtils.equals("phone", param.getString("type"))) {
				//发送手机短信
				int  returnCode = SendPhoneVerifyCode.SendPhoneVerifyCode(param.getString("account"), code, param.getString("language"));
				if (returnCode != 0) {
					return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_1106, Constants.RESULT_CODE_1106, null, param.getString("language"));
				}
			}
			//新增注册码发送记录	
			EmailVerify verify = new EmailVerify();
			verify.setId(UUID.randomUUID().toString().replace("-", ""));
			verify.setEmail(param.getString("account"));
			verify.setVerifyCode(code);
			verify.setCreateTime(systemTimeMapper.getSystemTime());
			emailVerifyMapper.insert(verify);
		} catch (Exception e) {
			logger.error("发送验证码异常"+e);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_1106, Constants.RESULT_CODE_1106, null, param.getString("language"));
		}
		return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, param.getString("language"));
	}

	@Override
	public JSONObject register(JSONObject param) {
		if (!param.has("isTour") && (!param.has("account") || !param.has("password") || !param.has("verifyCode") || !param.has("type"))) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}
		if (param.has("isTour") && StringUtils.equals("tour", param.getString("isTour"))) {
			return userSubService.addNewUser(param);
		}
		//是否已注册
		UserInfo userInfo = new UserInfo();
		userInfo.setType(param.getString("type"));
		if (StringUtils.equals("email", param.getString("type"))) {
			userInfo.setEmail(param.getString("account"));
		}
		if (StringUtils.equals("phone", param.getString("type"))) {
			userInfo.setPhone(param.getString("account"));
		}
		int  countAccount =  userInfoMapper.countAccount(userInfo);
		if (countAccount>0) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1105, Constants.RESULT_CODE_1105, null, param.getString("language"));
		}
		//验证码是否有效
		int isValid = userSubService.verifyIsValid(param);
		if (isValid==0) {
			if (param.has("userId")) {
				return userSubService.updateNewUser(param);
			}else {
				return userSubService.addNewUser(param);
			}
		}else if(isValid==2){
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1109, Constants.RESULT_CODE_1109, null, param.getString("language"));
		}else{
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1116, Constants.RESULT_CODE_1116, null, param.getString("language"));
		}
	}

	@Override
	public JSONObject login(JSONObject param) {
		if (!param.has("account") || !param.has("password")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}
		UserInfo record = new UserInfo();
		record.setPassword(param.getString("password"));
		if (StringUtils.equals(param.getString("type"), "email")) {
			record.setEmail(param.getString("account"));
		}
		if (StringUtils.equals(param.getString("type"), "phone")){
			record.setPhone(param.getString("account"));
		}		
		HashMap<String, Object>  user = userInfoMapper.queryByAccount(record);
		//账号或密码有误
		if (null == user || null == user.get("id")) {
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_1110,  Constants.RESULT_CODE_1110, null, param.getString("language"));
		}
		//同步用户信息到消息中心
		if (StringUtils.equals(user.get("synFlag").toString(), "0")) {
			userSubService.synUserToMC(user.get("id").toString(),param.getString("language"));
		}
		//组装返回信息
		JSONObject returnJson = new JSONObject();
		String token = userTokenService.addToken(user.get("id").toString(), param.getString("device"));
		returnJson.put("id", user.get("id").toString());
		returnJson.put("token", token);
		if (StringUtils.equals(Constants.DEVICE_PC, param.getString("device"))) {
			returnJson.put("email", user.get("email").toString());
			returnJson.put("phone", user.get("phone").toString());
			returnJson.put("password", user.get("password").toString());
		}
		return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, returnJson, param.getString("language"));
	}

	@Override
	public JSONObject entry(JSONObject param) {
		JSONObject returnObject = new JSONObject();
		if (StringUtils.equals(param.get("busiCode").toString(), BusiCodeConstant.VERSION_UPGRADE)) {
			//版本升级
			returnObject = this.versionUpgrade(param);
		}else if (StringUtils.equals(param.get("busiCode").toString(), BusiCodeConstant.SAVE_USER_PROFESSIONAL)) {
			//保存用户职业等信息
			returnObject = this.saveUserProfessional(param);
		}else if (StringUtils.equals(param.get("busiCode").toString(), BusiCodeConstant.VALID_VERIFY_CODE)) {
			//验证码校验
			returnObject = this.validVerifyCode(param);
		}else if (StringUtils.equals(param.get("busiCode").toString(), BusiCodeConstant.SAVE_PWD)) {
			//忘记密码、修改密码
			returnObject = this.savePwd(param);
		}else if (StringUtils.equals(param.get("busiCode").toString(), BusiCodeConstant.UPDATE_HEAD_PHOTO)) {
			//修改用户头像
			returnObject = this.updateHeadPhoto(param);
		}else if (StringUtils.equals(param.get("busiCode").toString(), BusiCodeConstant.USER_LOGIN_THIRD_PARTY)) {
			//第三方登录
			returnObject = this.loginThirdParty(param);
		}else if (StringUtils.equals(param.get("busiCode").toString(), BusiCodeConstant.USER_FEEDBACK)) {
			//用户反馈
			returnObject = this.userFeedback(param);
		}else if (StringUtils.equals(param.get("busiCode").toString(), BusiCodeConstant.GET_FIRST_THREE_NEWS)) {
			//获取前3条最热资讯
			returnObject = this.getFirstThreeNews(param);
		}
		else {
			returnObject = ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1107, null, param.getString("language"));
		}
		return returnObject;
	}
	
	private JSONObject getFirstThreeNews(JSONObject param){
		//验证参数
		if (!param.has("userId")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}	
		try {
			String userId = param.getString("userId");
			List<McPushContent> listMcpush = this.mcPushContentMapper.selectPushContentAll("1",null,0,3,userId,null);
			JSONArray ja  = new JSONArray();
			if(!JiveGlobe.isEmpty(listMcpush)){
				for(int i=0;i<listMcpush.size();i++){
					ja.add(listMcpush.get(i).getMsgText());
				}
			}
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, ja, param.getString("language"));
		} catch (Exception e) {
			logger.error("获取最新的3条资讯信息异常:"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_1104, Constants.RESULT_CODE_1104, null, param.getString("language"));
		}
	}

	private JSONObject loginThirdParty(JSONObject param){
		if (!param.has("type") ||!param.has("openId") || !param.has("token")) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}
		UserInfo record = new UserInfo();
		//qq
		if (StringUtils.equals("qq", param.getString("type"))) {
			record.setQqOpenId(param.getString("openId"));
			UserInfo oldUser = userInfoMapper.queryUserInfo(record);
			//更新
			if (null != oldUser) {
				if (!StringUtils.equals(param.getString("token"), oldUser.getQqToken())) {
					oldUser.setQqToken(param.getString("token"));
					//更新token
					userInfoMapper.updateSelective(oldUser);
				}
				//添加登录历史
				String sessionId = userTokenService.addToken(oldUser.getId(), param.getString("device"));
				JSONObject json = new JSONObject();
				json.put("id", oldUser.getId());
				json.put("token", sessionId);
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, json, param.getString("language"));
			}
			//新增
			record.setQqToken(param.getString("token"));
			if (StringUtils.isNotEmpty(param.optString("nickname"))) {
				record.setNickname(param.optString("nickname"));
			}
			if (StringUtils.isNotEmpty(param.optString("userPhoto"))) {
				record.setUserPhoto(param.optString("userPhoto"));
			}
			if (StringUtils.isNotEmpty(param.optString("sex"))) {
				if (StringUtils.equals("男", param.optString("sex"))) {
					record.setSex("1");
				}else{
					record.setSex("2");
				}
			}
		}
		//sina
		if (StringUtils.equals("sina", param.getString("type"))) {
			record.setSinaOpenId(param.getString("openId"));
			UserInfo oldUser = userInfoMapper.queryUserInfo(record);
			if (null != oldUser) {
				if (!StringUtils.equals(param.getString("token"), oldUser.getSinaToken())) {
					oldUser.setSinaToken(param.getString("token"));
					userInfoMapper.updateSelective(oldUser);
				}
				//添加登录历史
				String sessionId = userTokenService.addToken(record.getId(), param.getString("device"));
				JSONObject json = new JSONObject();
				json.put("id", oldUser.getId());
				json.put("token", sessionId);
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, json, param.getString("language"));
			}
			//新增
			record.setSinaToken(param.getString("token"));
		}
		//wchat
		if (StringUtils.equals("wchat", param.getString("type"))) {
			record.setWchatOpenId(param.getString("openId"));
			UserInfo oldUser = userInfoMapper.queryUserInfo(record);
			if (null != oldUser) {
				if (!StringUtils.equals(param.getString("token"), oldUser.getWchatToken())) {
					oldUser.setWchatToken(param.getString("token"));
					userInfoMapper.updateSelective(oldUser);
				}
				//添加登录历史
				String sessionId = userTokenService.addToken(record.getId(), param.getString("device"));
				JSONObject json = new JSONObject();
				json.put("id", oldUser.getId());
				json.put("token", sessionId);
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, json, param.getString("language"));
			}
			//新增
			record.setWchatToken(param.getString("token"));
		}
		record.setIsTour("0");
		record.setId(UUID.randomUUID().toString().replace("-", ""));
		userInfoMapper.insertSelective(record);
		//添加搜索配置
//		userSubService.addConfig(record.getId());
		//同步用户信息到消息中心
		if (StringUtils.equals(record.getSynFlag(), "0")) {
			int flag =userSubService.synUserToMC(record.getId(),param.getString("language"));
			if (flag == 1) {
				record.setSynFlag("1");
			}
		}
		//添加登录历史
		String sessionId = userTokenService.addToken(record.getId(), param.getString("device"));
//		System.out.println("id="+record.getId());
//		System.out.println("sessionId="+sessionId);
		JSONObject json = new JSONObject();
		json.put("id", record.getId());
		json.put("token", sessionId);
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, json, param.getString("language"));
	}
	
	private JSONObject validVerifyCode(JSONObject param){
		//验证参数
		if (!param.has("account")||!param.has("verifyCode")|| !param.has("type") ) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}
		//验证码是否有效
		int isValid = userSubService.verifyIsValid(param);
		if (isValid== 2) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1109, Constants.RESULT_CODE_1109, null, param.getString("language"));
		}
		if (isValid== 1) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1116, Constants.RESULT_CODE_1116, null, param.getString("language"));
		}		
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, param.getString("language"));
	}
	/**
	 * 修改用户头像
	 * @param param
	 * @return
	 */
	private JSONObject updateHeadPhoto(JSONObject param){
		//验证参数
		if (!param.has("userId")||!param.has("headPhotoUrl")) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}
		UserInfo record = new UserInfo();
		record.setId(param.getString("userId"));
		record.setUserPhoto(param.getString("headPhotoUrl"));
		int resultCount = userInfoMapper.updateSelective(record);
		if (resultCount>0) {
			JSONObject json = new JSONObject();
			json.put("id", param.getString("userId"));
			json.put("userPhoto", ConfigConstants.FILE_SERVER+param.getString("headPhotoUrl"));
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, json, param.getString("language"));
		}
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1111, null, param.getString("language"));
	}
	
	private JSONObject savePwd(JSONObject param){
		//验证参数
		if (((!param.has("account")||!param.has("type"))&&!param.has("userId"))||!param.has("password")) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}
		UserInfo record = new UserInfo();
		if (param.has("userId")) {
			record.setId(param.getString("userId"));
		}else {
			if (StringUtils.equals("email", param.getString("type"))) {
				record.setEmail(param.getString("account"));
			}else{
				record.setPhone(param.getString("account"));
			}
		}
		record.setPassword(param.getString("password"));
		//修改密码
		int resultCount = userInfoMapper.updateSelective(record);
		if (resultCount>0) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, param.getString("language"));
		}
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1100, null, param.getString("language"));
	}
	
	
	/**
	 * 保存用户职业等
	 * @param param
	 * @return
	 */
	private JSONObject saveUserProfessional(JSONObject param){
		if (!param.has("userId") ||!param.has("industry")||!param.has("professional")
				||!param.has("industryId")||!param.has("proId")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}
		UserInfo record = new UserInfo();
		record.setId(param.getString("userId"));
		record.setIndustry(param.getString("industry").trim());
		record.setProfessional(param.getString("professional").trim());
		record.setIndustryId(param.getString("industryId").trim());
		record.setProId(param.getString("proId").trim());
		int resultCount = userInfoMapper.updateSelective(record);
		if (resultCount >0) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, param.getString("language"));
		}
		
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1100, null, param.getString("language"));
	}
	
	/**
	 * 版本升级
	 * @param param
	 * @return
	 */
	private JSONObject versionUpgrade(JSONObject param){
		if (!param.has("oldVersion") ||!param.has("appName")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}
		AppVersion version = new AppVersion();
		version.setAppName(param.getString("appName"));
		version.setUseType(param.getString("device"));
		//查询最新版本信息
		List<HashMap<String, Object>> newAppVersions = appVersionMapper.selectNewestVersions(version);
		if (null == newAppVersions || newAppVersions.size()==0) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1112, null, param.getString("language"));
		}
		Integer oldSerial = 0;
		//查询旧版本号对应的序号
		if (!StringUtils.equals("0.2", param.getString("oldVersion"))) {
			version.setVersionNum(param.getString("oldVersion"));
			oldSerial = appVersionMapper.selectVersionSerial(version);
			if (null == oldSerial) {
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1113, null, param.getString("language"));
			}
			if (oldSerial ==  Integer.valueOf(newAppVersions.get(0).get("versionSerial").toString())) {
				//已是最新版本
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1114, Constants.RESULT_CODE_1114, null, param.getString("language"));
			}
		}
		
//		if (oldSerial ==  Integer.valueOf(newAppVersions.get(0).get("versionSerial").toString())) {
//			//已是最新版本
//			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1114, Constants.RESULT_CODE_1114, null, param.getString("language"));
//		}else{
			HashMap<String, Object> allPackage = new HashMap<>();
			HashMap<String, Object> incrementPackage = new HashMap<>();
			//过滤升级包 fileType 0 总包 1 增量包
			for (int i = 0; i < newAppVersions.size(); i++) {
				if (newAppVersions.get(i).get("fileType").equals("0")) {
					allPackage = newAppVersions.get(i);
				}else {
					incrementPackage = newAppVersions.get(i);
				}
			}
//			System.out.println(newAppVersions.get(0).get("versionSerial").toString());
			if (oldSerial==0 || oldSerial == Integer.valueOf(newAppVersions.get(0).get("versionSerial").toString())-1) {
				incrementPackage.put("fileUrl", ConfigConstants.FTP_SERVER+incrementPackage.get("fileUrl"));
				if (StringUtils.equals(language, param.getString("language"))) {
					incrementPackage.put("desc", incrementPackage.get("versionDescZh"));
				}else {
					incrementPackage.put("desc", incrementPackage.get("versionDescEn"));
				}
				incrementPackage.remove("versionDescZh");
				incrementPackage.remove("versionDescEn");
				incrementPackage.remove("versionSerial");
				//增量升级
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, incrementPackage, param.getString("language"));
			}else{
				allPackage.put("fileUrl", ConfigConstants.FTP_SERVER+allPackage.get("fileUrl"));
				if (StringUtils.equals(language, param.getString("language"))) {
					allPackage.put("desc", allPackage.get("versionDescZh"));
				}else {
					allPackage.put("desc", allPackage.get("versionDescEn"));
				}
				allPackage.remove("versionDescZh");
				allPackage.remove("versionDescEn");
				allPackage.remove("versionSerial");
				//整包升级
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, allPackage, param.getString("language"));
			}
//		} 
	}
			
	/**
	 * 检查版本更新(安卓)
	 */
				
	/**
	 * @param param
	 * @return
	 */
	private JSONObject userFeedback(JSONObject param) {
		//参数验证
		if (!param.has("attitude") || !param.has("content") || !param.has("email")) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}
		//校验邮箱格式
		Matcher email = Constants.EMAIL_ADDRESS.matcher( param.getString("email"));
		Matcher phone = Constants.PHONE.matcher(param.getString("email"));
		if (!email.matches() && !phone.matches()) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1117, Constants.RESULT_CODE_1117, null, param.getString("language"));
		}
		String title = "【" + param.optString("attitude") +  "】" + param.optString("email") + "的反馈";
		StringBuffer contentBuffer = new StringBuffer();
		contentBuffer.append(param.optString("content"));
		if (param.has("fileKey")) {
			contentBuffer.append("\n");
			JSONArray fileKeys = param.optJSONArray("fileKey");
			for (int i = 0; i < fileKeys.size(); i++) {
				String url = fileKeys.getString(i);
				contentBuffer.append("[img]").append(url).append("[/img]");
			}
		}
		try {
			int resultCode = DiscuzUtil.publishArticle(ConfigConstants.DISCUZ_BBS_URL, ConfigConstants.DISCUZ_PALTE_ID, title, contentBuffer.toString());
			if (resultCode == 200) {
				return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, param.getString("language"));
			}
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1118, null, param.getString("language"));
		} catch (Exception e) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1118, null, param.getString("language"));
		}
	}
	/**
	 * 监控公告消息，并实时推送
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject monitorSysNotice(){
		try {
			//查询未监控的系统消息
			SysNotice notice = new SysNotice();
			notice.setMonitorStatus("0");//0未监控 1已监控
			List<HashMap<String, Object>> noticeList = sysNoticeMapper.queryNoticeList(notice);
			//若未监控到新数据，直接返回
			if (null == noticeList || noticeList.size() == 0) {
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_1119, null, language);
			}
			//批量添加推送消息
			HashMap<String, Object> json = userSubService.batchAddPushMsg(noticeList, "3", "1");
			if (null == json) {
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1120, null, language);
			}
			List<McPushContent> contList = (List<McPushContent>) json.get("list");
			List<String> ids = (List<String>) json.get("ids");
			//更新监控状态
			sysNoticeMapper.batchUpdateStatus(ids);
			//推送消息
			this.packageMsgAndSend(contList);
		} catch (Exception e) {
			logger.error("监控公告消息异常"+e.getMessage());
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_FAIL, null, language);
		}
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, language);		
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject monitorSysUpgrade() {
		try {
			//查询未监控的系统消息
			AppVersion version = new AppVersion();
			version.setMonitorStatus("0");
			version.setFileType("0");//0 总包 1 增量包
			version.setUseType("PC");
			List<HashMap<String, Object>> appList = appVersionMapper.queryVersionList(version);
			//若未监控到新数据，直接返回
			if (null == appList || appList.size() == 0) {
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_1119, null, language);
			}
			//批量添加推送消息
			HashMap<String, Object> json = userSubService.batchAddPushMsg(appList, "3", "1");
			if (null == json) {
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1120, null, language);
			}
			List<McPushContent> contList = (List<McPushContent>) json.get("list");
			List<String> ids = (List<String>) json.get("ids");
			//更新监控状态
			appVersionMapper.batchUpdateStatus(ids);
			//推送消息
			this.packageMsgAndSend(contList);
		} catch (Exception e) {
			logger.error("监控系统版本信息异常"+e.getMessage());
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_FAIL, null, language);
		}
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, language);		
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject monitorTimingPush() {
		try {
			//查询未监控的推荐的应用、资讯消息
			AppRecommend appRec = new AppRecommend();
			appRec.setMonitorStatus("0");
			List<HashMap<String, Object>> appList = sysNoticeMapper.queryAppRecList(appRec);
			NewsRecommend newsRec = new NewsRecommend();
			newsRec.setMonitorStatus("0");
			List<HashMap<String, Object>> newsList = sysNoticeMapper.queryNewsRecList(newsRec);
			//若未监控到新数据，直接返回
			if ((null == appList || appList.size() == 0)&&(null == newsList || newsList.size() == 0)) {
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_1119, null, language);
			}
			HashMap<String, Object> appjson = new HashMap<>();
			HashMap<String, Object> newsjson = new HashMap<>();
			//批量添加定时推送消息
			if (appList.size()>0) {
				appjson = userSubService.batchAddPushMsg(appList, "2", "0");
				
			}
			if (newsList.size()>0) {
				newsjson = userSubService.batchAddPushMsg(newsList, "1", "0");
				
			}	
			if (appList.size()>0 && null != appjson) {
				List<String> ids = (List<String>) appjson.get("ids");
				//更新监控状态
				sysNoticeMapper.batchUpdateAppStatus(ids);
			}
			if (newsList.size()>0 && null != newsjson) {
				List<String> ids = (List<String>) newsjson.get("ids");
				//更新监控状态
				sysNoticeMapper.batchUpdateNewsStatus(ids);
			}
			if (null == appjson || null ==newsjson) {
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1120, null, language);
			}
		} catch (Exception e) {
			logger.error("监控定时发动信息异常"+e.getMessage());
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_FAIL, null, language);
		}
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, language);		
	}

	@SuppressWarnings("static-access")
	@Override
	public JSONObject monitorPushData() {
		Date currentTime = systemTimeMapper.getSystemTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentTime);
		if (calendar.get(Calendar.HOUR)+1==ConfigConstants.MESSAGE_PUSH_TIME1) {
			calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DATE), ConfigConstants.MESSAGE_PUSH_TIME1, 0, 0);
		}else{
			calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DATE), ConfigConstants.MESSAGE_PUSH_TIME2, 0, 0);
		}
		McPushContent content = new McPushContent();
		content.setPlanTime(calendar.getTime());
		List<McPushContent> contList = mcPushContentMapper.queryTimingData(content);
		if (null ==contList || contList.size()==0) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_1119, null, language);
		}
		try {
			//推送消息
			this.packageMsgAndSend(contList);
		}catch (Exception e) {
			logger.error("处理发送失败的数据异常"+e.getMessage());
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_FAIL, null, language);
		}
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, language);		
	}

	@Override
	public JSONObject monitorFailData() {
		List<McPushContent> contList = mcPushContentMapper.queryPushFailData();
		if (null == contList || contList.size()==0) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_1119, null, language);
		}
		try {
			//推送消息
			this.packageMsgAndSend(contList);
		}catch (Exception e) {
			logger.error("处理发送失败的数据异常"+e.getMessage());
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_FAIL, null, language);
		}
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, language);		
	}
	/**
	 * 组装参数并推送消息
	 * @param contList
	 */
	private void packageMsgAndSend(List<McPushContent> contList){
		List<String> pushIds = new ArrayList<>();
		String flag = "";
		JSONObject param = new JSONObject();
		param.put("requestCode", "message_send");
//		param.put("appId", 1);
		param.put("appToken", "83c39424-7e5b-4416-88c1-2d6d6e54b794");
		param.put("receiverToken", "");
		param.put("sendChaterId", "");
		param.put("msgQos", "1");
		param.put("msgRetain", "1");
		for (int i = 0; i < contList.size(); i++) {
			param.put("url", ConfigConstants.MESSAGE_CENTER);
			param.put("language", language);
			McPushContent pushContent = contList.get(i);
			if (StringUtils.isNotEmpty(pushContent.getUserId())) {
				param.put("msgTopic", pushContent.getUserId());
			}else{
				param.put("msgTopic", "mate");
			}
			//组装消息内容
			param.put("msgId", pushContent.getId());
			param.put("msgText", pushContent.getMsgContent());
			//推送消息
			flag = userSubService.sendMsg(param);
			//根据推送结果，处理数据
			if (StringUtils.equals(flag, "200")) {
				pushIds.add(pushContent.getId());
			}
			if (StringUtils.equals(flag, "4104") || StringUtils.equals(flag, "4105")) {
				break;
			}
			if (StringUtils.equals(flag, "0")) {
				continue;
			}
		}
		//更改推送状态
		if (pushIds.size()>0) {
			mcPushContentMapper.batchUpdatePushStatus(pushIds);
		}
	}
	
	
}
