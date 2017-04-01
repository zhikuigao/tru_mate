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

import com.alibaba.fastjson.JSONArray;
import com.jws.app.operater.data.CategoryRecMapper;
import com.jws.app.operater.data.EmailVerifyMapper;
import com.jws.app.operater.data.McPushContentMapper;
import com.jws.app.operater.data.SysNoticeMapper;
import com.jws.app.operater.data.SysUserLogMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.data.UserInfoMapper;
import com.jws.app.operater.model.CategoryRec;
import com.jws.app.operater.model.EmailVerify;
import com.jws.app.operater.model.McPushContent;
import com.jws.app.operater.model.SysUserLog;
import com.jws.app.operater.model.UserConfig;
import com.jws.app.operater.model.UserInfo;
import com.jws.app.operater.service.UserSubService;
import com.jws.common.constant.ConfigConstants;
import com.jws.common.constant.Constants;
import com.jws.common.util.JiveGlobe;
import com.jws.common.util.MessageCenter;
import com.jws.common.util.ResultPackaging;

@Service("userSubService")
public class UserSubServiceImpl implements UserSubService{
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private SystemTimeMapper systemTimeMapper;
	@Resource
	private UserInfoMapper userInfoMapper;
	@Resource
	private EmailVerifyMapper emailVerifyMapper;
	@Resource
	private SysUserLogMapper sysUserLogMapper;
	@Resource
	private SysNoticeMapper sysNoticeMapper;
	@Resource
	private McPushContentMapper mcPushContentMapper;
	@Resource
	private CategoryRecMapper categoryRecMapper;
	
	@Override
	public JSONObject addNewUser(JSONObject param){
		UserInfo userInfo = new UserInfo();
		userInfo.setId(UUID.randomUUID().toString().replace("-", ""));
		if (!param.has("isTour")) {
			if (StringUtils.equals(param.getString("type"), "phone")) {
				userInfo.setPhone(param.getString("account"));
			}else if (StringUtils.equals(param.getString("type"), "email")){
				userInfo.setEmail(param.getString("account"));
			}
			userInfo.setPassword(param.getString("password"));
			userInfo.setIsTour("0");
		}else {
			userInfo.setIsTour("1");
		}		
		userInfo.setCreateTime(systemTimeMapper.getSystemTime());
		userInfoMapper.insertSelective(userInfo);
		//添加搜索配置
//		addConfig(userInfo.getId());
		//同步用户信息到消息中心
		synUserToMC(userInfo.getId(),param.getString("language"));
		//返回结果
		JSONObject retuJsonObject = new JSONObject();
		retuJsonObject.put("id", userInfo.getId());
		retuJsonObject.put("isTour", userInfo.getIsTour());
		return  ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, retuJsonObject, param.getString("language"));	
	}
	
	public void addConfig(String userId){
		try {
			List<UserConfig> configs = new ArrayList<>();
			//1.添加系统模块配置
			List<String> modules = userInfoMapper.queryAllModuleId();
			if (null != modules && modules.size()>0) {
				for (int i = 0; i < modules.size(); i++) {
					UserConfig config = new UserConfig();
					config.setId(UUID.randomUUID().toString().replaceAll("-", ""));
					config.setUserId(userId);
					config.setSourceId(modules.get(i));
					config.setType("1");
					configs.add(config);
				}
			}
			//2.添加系统内搜索源配置
			List<String> list = userInfoMapper.queryAllSourceId();
			if (null != list && list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					UserConfig config = new UserConfig();
					config.setId(UUID.randomUUID().toString().replaceAll("-", ""));
					config.setUserId(userId);
					config.setSourceId(list.get(i));
					config.setType("2");
					configs.add(config);
				}
			}
			if (configs.size()>0) {
				userInfoMapper.adduserConfigBatch(configs);
			}
		} catch (Exception e) {
			logger.error("初始化用户搜索配置源出错："+e.getMessage());
		}
	}

	@Override
	public int verifyIsValid(JSONObject param) {
		EmailVerify  emailVerify = emailVerifyMapper.queryNewestVerifyByEmail(param.getString("account"));
		//不是最新的验证码
		if (!StringUtils.equals(param.getString("verifyCode"), emailVerify.getVerifyCode())) {
			return 1;
		}
		int  validTime = 0;
		if (StringUtils.equals("email", param.getString("type"))) {
			validTime = Integer.valueOf(ConfigConstants.MAIL_TIME_OUT_ZH);
		}
		if (StringUtils.equals("phone", param.getString("type"))) {
			validTime = Integer.valueOf(ConfigConstants.PHONE_TIME_OUT_ZH);
		}
		Date sendTime= emailVerify.getCreateTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sendTime);
		calendar.add(Calendar.MINUTE, validTime);
		Date  newDate = systemTimeMapper.getSystemTime();
		//最新时间>发送时间+有效时长
		if (newDate.compareTo(calendar.getTime())>-1) {
			return 2;
		}
		return 0;
	}

	@Override
	public JSONObject updateNewUser(JSONObject param) {
		UserInfo oldInfo = userInfoMapper.queryById(param.getString("userId"));
		if (null==oldInfo || StringUtils.equals("0", oldInfo.getIsTour())) {
			return  ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1111, null, param.getString("language"));
		}
		oldInfo.setIsTour("0");
		if (StringUtils.equals(param.getString("type"), "phone")) {
			oldInfo.setPhone(param.getString("account"));
		}else if (StringUtils.equals(param.getString("type"), "email")){
			oldInfo.setEmail(param.getString("account"));
		}
		oldInfo.setPassword(param.getString("password"));
		oldInfo.setUpdateTime(systemTimeMapper.getSystemTime());
		userInfoMapper.updateSelective(oldInfo);
		return  ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, param.getString("language"));
	}

	@Override
	public int synUserToMC(String userId, String language) {
		Boolean flag = false;
		String result = "";
		JSONObject param = new JSONObject();
//		String  time = Constants.df.format( new Date());
//		param.put("time", time);
//		param.put("md5Str", MD5Util.getMD5String(ConfigConstants.MESSAGE_KEY+time));
		param.put("requestCode", "chater_register");
//		param.put("appId", 1);
		param.put("appToken", "83c39424-7e5b-4416-88c1-2d6d6e54b794");
		param.put("chaterId", userId);
		param.put("chaterType", 0);
		param.put("language", language);
		param.put("url", ConfigConstants.MESSAGE_CENTER);
		SysUserLog log = this.addCallLog(param.toString());
		try {
			result = MessageCenter.SynUserToMessageCenter(param);
		} catch (Exception e) {
			logger.error("同步信息到消息中心异常:"+e);
		}
		//修改用户同步标识
		if (StringUtils.isNotEmpty(result)) {
			try {
				JSONObject json = JSONObject.fromObject(result);
				JSONObject resultJson = JSONObject.fromObject(json.get("result"));
				//code = 200 同步成功 3105 已同步过
				if (StringUtils.equals(resultJson.get("code").toString(), "200")||
						StringUtils.equals(resultJson.get("code").toString(), "3105")) {
					UserInfo user = new UserInfo();
					user.setId(userId);
					user.setSynFlag("1");
					userInfoMapper.updateSelective(user);
					flag = true;
				}
			} catch (Exception e) {
				logger.error("更新同步标识异常："+e);
			}
		}
		this.updateCallLog(result, log);
		if (flag) {
			return 1;
		}
		return 0;
	}
	
	private  SysUserLog  addCallLog(String param){
		try {
			SysUserLog  log = new SysUserLog();
			log.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			log.setRequest(param);
			log.setReceive("messageCenter");
			sysUserLogMapper.userLogInsert(log);
			return log;
		} catch (Exception e) {
			logger.error("记录日志异常"+e.getMessage());
		}
		return null;
	}
	
	private  void  updateCallLog(String  result, SysUserLog  oldLog){
		if (null != oldLog.getId()) {
			try {
				oldLog.setResponse(result);
				oldLog.setSource("usersystem");
				sysUserLogMapper.userLogUpdateByPrimaryKey(oldLog);
			} catch (Exception e) {
				logger.error("更新日志异常"+e.getMessage());
			}
		}
		
	}

	/**
	 * 推送消息
	 * @param param
	 * @return
	 */
	@Override
	public String sendMsg(JSONObject param){
		JSONObject result = new JSONObject();
		//记日志
		SysUserLog log = this.addCallLog(param.toString());
		//推送消息
		try {
			result = MessageCenter.PushMessage(param);
		} catch (Exception e) {
			logger.error("同步信息到消息中心异常:"+e);
			return "0";
		}
		//更新日志
		this.updateCallLog(result.getString("result"), log);
		//返回推送结果
		return result.getString("code");
	}
	/**
	 * 
	 * @param list
	 * @param msgType 消息类型 1.资讯2.应用3.系统4.聊天
	 * @param pushType 推送类型  0定时 1实时
	 * @return
	 */
	@SuppressWarnings("static-access")
	@Override
	public HashMap<String, Object> batchAddPushMsg(List<HashMap<String, Object>> list, String msgType, String pushType){
		HashMap<String, Object> json = new HashMap<>();
		List<McPushContent> contList = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, Object> map = list.get(i);
			//消息类型
			map.put("msgType", msgType);
			if (map.containsKey("url")) {
				if (StringUtils.equals("3", msgType)) {
					map.put("url", ConfigConstants.FTP_SERVER+map.get("url"));
				}
			}else{
				map.put("url", "");
			}
			//设置推送时间 3.系统4.聊天 实时发送  1.资讯2.应用 定时发送
			Date pushTime = null;
			Date currentTime = systemTimeMapper.getSystemTime();
			//如果是实时发送，推送时间为当前时间
			if (StringUtils.equals("1", pushType)) {
				pushTime = currentTime;
			}else{
				//定时发送，1.资讯 10点发送   2应用	下午3点发送
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(currentTime);
				//1.资讯
				if (StringUtils.equals("1", msgType)) {
//					calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DATE), ConfigConstants.MESSAGE_PUSH_TIME1, 0, 0);
//					//如果当前时间在约定的发送时间前，就设置为当天发送，否则，时间+1
//					if (currentTime.before(calendar.getTime())) {
//						pushTime = calendar.getTime();
//					}else{
//						calendar.add(calendar.DATE, 1);
//						pushTime = calendar.getTime();
//					}
					pushTime = calendar.getTime();
				}
				//2应用
				if (StringUtils.equals("2", msgType)) {
//					calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DATE), ConfigConstants.MESSAGE_PUSH_TIME2, 0, 0);
//					//如果当前时间在约定的发送时间前，就设置为当天发送，否则，时间+1
//					if (currentTime.before(calendar.getTime())) {
//						pushTime = calendar.getTime();
//					}else{
//						calendar.add(calendar.DATE, 1);
//						pushTime = calendar.getTime();
//					}
					pushTime = calendar.getTime();
				}
			}			
			map.put("pushTime", Constants.df.format(pushTime));
			
			ids.add(map.get("id").toString());
			if (StringUtils.equals("2", msgType)) {
				map.put("id", map.get("appId"));
			}
			McPushContent content = new McPushContent();
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			content.setId(id);
			map.put("msgId", id);
			if (map.containsKey("userId")) {
				content.setUserId(map.get("userId").toString());
			}else{
				content.setUserId("");
			}
			if (map.containsKey("category")) {
				content.setMsgSubType(map.get("category").toString());
			}
			if (map.containsKey("categoryId")) {
				content.setSubTypeId(map.get("categoryId").toString());
			}
			content.setMsgContent(JSONObject.fromObject(map).toString());
			content.setMsgType(msgType);
			content.setPushType(pushType);
			content.setPlanTime(pushTime);
			contList.add(content);
		}
		try {
			mcPushContentMapper.batchInsertPushMsg(contList);
		} catch (Exception e) {
			logger.error("保存推送消息异常："+e.getMessage());
			return null;
		}
		json.put("list", contList);
		json.put("ids", ids);
		return json;
	}

	public JSONObject selectPushType(JSONObject param){
		//验证参数
		if (!param.has("msgType") || !param.has("userId")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}	
		try {
			 String userid = param.getString("userId").toString();
				if(JiveGlobe.isEmpty(param.get("userId").toString())){
					userid = null;
				}
			List<McPushContent> listMcpush = this.mcPushContentMapper.selectPushType(param.getString("msgType").toString(),userid);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, listMcpush, param.getString("language"));
		} catch (Exception e) {
			logger.error("获取消息类型异常"+e);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_1104, Constants.RESULT_CODE_1104, null, param.getString("language"));
		}
	}


	public JSONObject selectPushContentAll(JSONObject param){
		//验证参数
		if (!param.has("msgType")  || !param.has("msgSubType") || !param.has("page") 
				|| !param.has("pageNum") || !param.has("userId") || !param.has("lastTime")  ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
		}	
		try {
			 Integer page = Integer.valueOf(param.getString("page").toString());
			 Integer pageNum = Integer.valueOf(param.getString("pageNum").toString());
			 String msgType = param.getString("msgType").toString();
			 String msgSubType = param.getString("msgSubType").toString();
			 String userid = param.getString("userId").toString();
			 String lasttime = param.getString("lastTime").toString();
			if(JiveGlobe.isEmpty(param.get("lastTime").toString())){
				 lasttime = null;
			}
			if(JiveGlobe.isEmpty(param.get("msgType").toString())){
				msgType = null;
			}
			if(JiveGlobe.isEmpty(param.get("msgSubType").toString())){
				msgSubType = null;
			}
			if(JiveGlobe.isEmpty(param.get("userId").toString())){
				userid = null;
			}
			List<McPushContent> listMcpush = new ArrayList<McPushContent>();
			if(null == msgType){
				if(page<1){	
					listMcpush = this.mcPushContentMapper.selectSystemContentAll(page, pageNum, userid,lasttime);
				}else{
					page = 0;
					listMcpush = this.mcPushContentMapper.selectSystemContentNoAll(page, pageNum, userid,lasttime);
				}
			}else{
				if (StringUtils.equals(Constants.GIT_LAB, msgType)) {
					msgType = "4";
				}
				if (StringUtils.equals(Constants.RED_MINE, msgType)) {
					msgType = "5";
				}
				//allSystem 包含系统消息、第三方应用消息
				if (StringUtils.equals("allSystem", msgType)) {
					if (page>0) {
						page = 0;
						listMcpush = mcPushContentMapper.selectSystemMore(page, pageNum, userid, lasttime);
					}else{
						listMcpush =mcPushContentMapper.selectSystemFirst(page, pageNum, userid, lasttime);
					}
				}else{
					if (StringUtils.equals("system", msgType)) {
						listMcpush = this.mcPushContentMapper.getFirstSystem();
					}else{
						page=0;
						if (StringUtils.equals("3", msgType)) {
							userid = null;
						}
						listMcpush = this.mcPushContentMapper.selectPushContentAll(msgType,msgSubType,page,pageNum,userid,lasttime);
					}
				}
			}
			//排序
			JSONArray ja  = new JSONArray();
			if(!JiveGlobe.isEmpty(listMcpush)){
				for(int i=0;i<listMcpush.size();i++){
					McPushContent mc = new McPushContent();
					mc = listMcpush.get(i);
					ja.add(mc.getMsgText());
				}
			}
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, ja, param.getString("language"));
		} catch (Exception e) {
			logger.error("获取消息类型异常"+e);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_1104, Constants.RESULT_CODE_1104, null, param.getString("language"));
		}
		
	 }

	@Override
	public JSONObject selectPushInnovationType(JSONObject param) {
		try {
			//验证参数
			if (!param.has("userId")   || !param.has("type")  ) {		
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
			}	
			 String userid = param.getString("userId").toString();
				if(JiveGlobe.isEmpty(param.get("userId").toString())){
					userid = null;
				}
			String type = param.getString("type").toString();
			List<CategoryRec> listMcpush = new ArrayList<>();
			//资讯类型
			if(type.equals("news")){
				listMcpush = this.categoryRecMapper.selectPushInnovationType(userid);
			}else if(type.equals("app")){
				//应用类型
				listMcpush = this.categoryRecMapper.selectPushAppType(userid);
			}else if(type.equals("plugIn")){
				//插件类型
				List<HashMap<String, Object>> list = this.categoryRecMapper.selectPlugInType(userid);
				return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, list, param.getString("language"));
			}else{
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1102, null, param.getString("language"));
			}
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, listMcpush, param.getString("language"));
		} catch (Exception e) {
			logger.error("获取消息类型异常"+e);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_1104, Constants.RESULT_CODE_1104, null, param.getString("language"));
		}
	}
	
}
