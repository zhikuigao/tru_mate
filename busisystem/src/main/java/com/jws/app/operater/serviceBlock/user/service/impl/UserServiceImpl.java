package com.jws.app.operater.serviceBlock.user.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;

import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.service.BlockEntryService;
import com.jws.app.operater.service.impl.InterfaceInvoking;
import com.jws.app.operater.serviceBlock.user.service.UserTokenService;
import com.jws.common.constant.BlockBusiCodeConstant;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.constant.ConfigConstants;
import com.jws.common.constant.Constants;
import com.jws.common.constant.UserEnum;
import com.jws.common.util.FileUtil;
import com.jws.common.util.ResultPackaging;
@Service("userService")
public class UserServiceImpl implements BlockEntryService {
//	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private InterfaceInvoking interfaceInvoking;	
	@Resource
	private SystemTimeMapper systemTimeMapper;
	@Resource
	private UserTokenService userTokenService;
	
	/**
	 * 根据busiCode分方法入口
	 * @throws IOException 
	 */
	@Override
	public JSONObject entry(JSONObject paraMap) {
		JSONObject returnObject = new JSONObject();
		if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.USER_QUERY_USER_INFO)) {
			returnObject = this.queryUserInfo(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.USER_SEND_VERIFYCODE)) {
			//发送验证码
			returnObject = this.sendVerifyCode(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.USER_REGISTER)) {
			//注册
			returnObject = this.register(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.USER_UPLOAD_HEAD_PHOTO)) {
			//上传用户头像
			returnObject = this.uploadHeadPhoto(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.USER_LOGIN)) {
			//登录
			returnObject = this.login(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.USER_LOGIN_THIRD_PARTY)) {
			//第三方登录
			returnObject = this.loginThirdParty(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.VALID_VERIFY_CODE)) {
			//验验证码校验
			returnObject = this.validVerifyCode(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.SAVE_PWD)) {
			//忘记密码/修改密码
			returnObject = this.savePwd(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.SAVE_USER_PROFESSIONAL)) {
			//保存用户行业职业
			returnObject = this.saveUserProfessional(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.UPLOAD_FILES)) {
			//上传DB文件
			returnObject = this.uploadFiles(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.VERSION_UPGRADE)) {
			//版本升级
			returnObject = this.versionUpgrade(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.QUERY_SYS_INFO)) {
			//查询系统消息
			returnObject = this.querySysInfo(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.QUERY_SYS_CONTENT)) {
			//查询系统详情
			returnObject = this.querySysContent(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.USER_FEEDBACK)) {
			//用户反馈
			returnObject = this.userFeedback(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.SELECT_PUSH_TYPE)) {
			returnObject = this.selectPushType(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.SELECT_PUSH_CONTENTALL)) {
			returnObject = this.selectPushContentAll(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.SELECT_PUSH_INNOVATIONTYPE)) {
			returnObject = this.selectPushInnovationType(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.GET_FIRST_THREE_NEWS)) {
			//查询最新的3条资讯
			returnObject = this.getFirstThreeNews(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.QUERY_PERSON)) {
			//查询个人信息
			returnObject = this.queryPerson(paraMap);
		}else if (StringUtils.equals(paraMap.get("busiCode").toString(), BusiCodeConstant.UPDATE_PERSON)) {
			//修改个人信息
			returnObject = this.updatePerson(paraMap);
		}
		else{
			//找不到对应的业务方法
			returnObject = ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_103, null, paraMap.get("language").toString());
		}
		return returnObject;
	}
	/**
	 * 忘记密码
	 * @param paraMap
	 * @return
	 */
	private JSONObject validVerifyCode(JSONObject paraMap){
		if (!paraMap.has("account") ||!paraMap.has("verifyCode") ) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		//如果是邮箱注册
		 Matcher email = Constants.EMAIL_ADDRESS.matcher( paraMap.getString("account"));
		//如果是手机注册
		Matcher phone = Constants.PHONE.matcher( paraMap.getString("account"));
		
		if (!email.matches() && !phone.matches()) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1002,Constants.RESULT_CODE_1002, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("keyCode", UserEnum.User);
		newMap.put("busiCode", BlockBusiCodeConstant.VALID_VERIFY_CODE);
		newMap.put("account", paraMap.get("account"));
		newMap.put("verifyCode", paraMap.get("verifyCode"));
		if (email.matches()) {
			newMap.put("type","email");
		}
		if (phone.matches()) {
			newMap.put("type", "phone");
		}
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
	}
	/**
	 * 忘记密码 forgotPwd/修改密码 updatePwd
	 * @param paraMap
	 * @return`
	 */
	private JSONObject savePwd(JSONObject paraMap){
		if (!paraMap.has("password") || !paraMap.has("interfacetype")) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		if ((StringUtils.equals("forgotPwd", paraMap.getString("interfacetype")) && !paraMap.has("account"))
				|| (StringUtils.equals("updatePwd", paraMap.getString("interfacetype")) && !paraMap.has("userId"))) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		JSONObject  newMap = new JSONObject();
		//忘记密码
		if (StringUtils.equals("forgotPwd", paraMap.getString("interfacetype"))) {
			//如果是邮箱注册
			 Matcher email = Constants.EMAIL_ADDRESS.matcher( paraMap.getString("account"));
			//如果是手机注册
			Matcher phone = Constants.PHONE.matcher( paraMap.getString("account"));
			
			if (!email.matches() && !phone.matches()) {
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1002,Constants.RESULT_CODE_1002, null, paraMap.getString("language"));
			}
			
			if (email.matches()) {
				newMap.put("type","email");
			}
			if (phone.matches()) {
				newMap.put("type", "phone");
			}
			newMap.put("account", paraMap.get("account"));
		}else {
			//修改密码需要验证token有效性
			Boolean valid = userTokenService.validToken(paraMap.getString("token"), paraMap.getString("device"));
			if (!valid) {
				return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_112, Constants.RESULT_CODE_112, null, paraMap.getString("language"));
			}
			newMap.put("userId", paraMap.get("userId"));
		}		
		//组装参数
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("keyCode", UserEnum.User);
		newMap.put("busiCode", BlockBusiCodeConstant.SAVE_PWD);
		newMap.put("password", paraMap.get("password"));
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
	}
	/**
	 * 保存用户行业职业
	 * @param paraMap
	 * @return
	 */
	private JSONObject saveUserProfessional(JSONObject paraMap){
		if (!paraMap.has("userId") ||!paraMap.has("industry")||!paraMap.has("professional")
				||!paraMap.has("industryId")||!paraMap.has("proId")) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("keyCode", UserEnum.User);
		newMap.put("busiCode", BlockBusiCodeConstant.SAVE_USER_PROFESSIONAL);
		newMap.put("userId", paraMap.get("userId"));
		newMap.put("industry", paraMap.get("industry"));
		newMap.put("professional", paraMap.get("professional"));
		newMap.put("industryId", paraMap.get("industryId"));
		newMap.put("proId", paraMap.get("proId"));
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
	}
	
	/**
	 * 查询系统详情
	 * @param paraMap
	 * @return
	 */
	private JSONObject querySysContent(JSONObject paraMap){
		if (!paraMap.has("sysId")) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("keyCode", UserEnum.Message);
		newMap.put("busiCode", paraMap.get("busiCode"));
		newMap.put("sysId", paraMap.get("sysId"));
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
	}
	
	/**
	 * 查询系统消息
	 * @param paraMap
	 * @return
	 */
	private JSONObject querySysInfo(JSONObject paraMap){
		if (StringUtils.equals(Constants.DEVICE_PC, paraMap.getString("device"))) {
			if (!paraMap.has("pageNum")) {
				return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
			}
		}else{
			if (!paraMap.has("page") ||!paraMap.has("pageNum")) {
				return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
			}
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("keyCode", UserEnum.Message);
		newMap.put("busiCode", paraMap.get("busiCode"));
		newMap.put("page", paraMap.optString("page"));
		newMap.put("pageNum", paraMap.get("pageNum"));
		newMap.put("lastTime", paraMap.optString("lastTime"));
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
	}
	/**
	 * 版本升级
	 * @param paraMap
	 * @return
	 */
	private JSONObject versionUpgrade(JSONObject paraMap){
		if (!paraMap.has("oldVersion") ||!paraMap.has("appName")||!paraMap.has("upgradeType")) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("keyCode", UserEnum.User);
		newMap.put("busiCode", BlockBusiCodeConstant.VERSION_UPGRADE);
		newMap.put("oldVersion", paraMap.get("oldVersion"));
		newMap.put("appName", paraMap.get("appName"));
		newMap.put("upgradeType", paraMap.get("upgradeType"));
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
	}
	
	/**
	 * 保存抓取的用户行为信息
	 * @return
	 * @throws IOException 
	 */
	private JSONObject uploadFiles(JSONObject paraMap){
		if (!paraMap.has("fileKey") || !paraMap.has("userId") ) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		JSONObject  uploadResult = null;
		Date date = systemTimeMapper.getSystemTime();
		String  timeString = Constants.dfi.format(date);
		paraMap.put("month", timeString);
		uploadResult = FileUtil.upLoadMoreFolder(paraMap);
		if (uploadResult.optInt("code") != 1) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_108, null, paraMap.getString("language"));
		} else {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, paraMap.getString("language"));
		}
	}
	
	/**
	 * 保存抓取的用户行为信息
	 * @return
	 * @throws IOException 
	 */
//	private JSONObject saveUserRecord(JSONObject paraMap){
//		System.out.println(paraMap.toString());
//		if (!paraMap.has("userRecordFile")) {
//			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
//		}
//		//读取hdfs目录下当天的文件,如果没有，直接上传，如果有，将流加到文件后面即可
////		String hdfsPath = ConfigConstants.FILE_RECORD_URL;
//		Date date = systemTimeMapper.getSystemTime();
//		String  dateString = Constants.sdf.format(date);
////		String  currRecordPath = hdfsPath+"/"+dateString;
//		paraMap.put("day", dateString);
//		JSONObject  uploadResult = FileUtil.upLoadFolder(paraMap);
//		if (StringUtils.equals("-2", uploadResult.getString("code"))) {
//			logger.error("上传文件异常"+new Exception(uploadResult.toString()).getMessage());
//			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1005, null, paraMap.getString("language"));
//		}
//		if (StringUtils.equals("-1", uploadResult.getString("code"))) {
//			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_110, null, paraMap.getString("language"));
//		}
//		//组装参数，调用子系统写表
//		JSONObject  newMap = new JSONObject();
//		newMap.put("source", Constants.CALL_SOURCE);
//		newMap.put("receive", Constants.CALL_TO_USER);
//		newMap.put("keyCode", UserEnum.Behavior);
//		newMap.put("busiCode", BlockBusiCodeConstant.USER_SAVE_RECORD);
//		if (StringUtils.equals("1", uploadResult.getString("code")) && uploadResult.getBoolean("update")) {
//			newMap.put("type", "update");
//		}else {
//			newMap.put("type","add");
//		}
//		newMap.put("recordDate", dateString);
//		newMap.put("date", Constants.df.format(date));
//		newMap.put("url", uploadResult.getString("path"));
//		//调用子系统
//		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);
//		return null;
//	}
	
	/**
	 * 第三方登录
	 * @param paraMap
	 * @return
	 */
	private JSONObject  loginThirdParty(JSONObject paraMap){
		if (!paraMap.has("openId") || !paraMap.has("token") || !paraMap.has("type")) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("keyCode", UserEnum.User);
		newMap.put("busiCode", BlockBusiCodeConstant.USER_LOGIN_THIRD_PARTY);
		newMap.put("type", paraMap.get("type"));
		newMap.put("openId", paraMap.get("openId"));
		newMap.put("token", paraMap.get("token"));
		newMap.put("nickname", paraMap.optString("nickname"));
		newMap.put("sex", paraMap.optString("sex"));
		newMap.put("userPhoto", paraMap.optString("userPhoto"));
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);		
	}
	
	
	
	private JSONObject queryPerson(JSONObject paraMap){
		if (!paraMap.has("id")) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("busiCode", BusiCodeConstant.QUERY_PERSON);
		newMap.put("id", paraMap.get("id"));
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
	}
	
	private JSONObject updatePerson(JSONObject paraMap){
		if (!paraMap.has("id") ||!paraMap.has("nickname")  ) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("busiCode", BusiCodeConstant.UPDATE_PERSON);
		newMap.put("id", paraMap.get("id"));
		newMap.put("birthday", paraMap.get("birthday"));
		newMap.put("sex", paraMap.get("sex"));
		newMap.put("industry", paraMap.get("industry"));
		newMap.put("professional", paraMap.get("professional"));
		newMap.put("nickname", paraMap.get("nickname"));
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
	}
	/**
	 * 登录
	 * @param paraMap
	 * @return
	 */
	private JSONObject  login(JSONObject paraMap){
		if (!paraMap.has("account") || !paraMap.has("password") ) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
//		newMap.put("keyCode", UserEnum.User);
		newMap.put("busiCode", BlockBusiCodeConstant.USER_LOGIN);
		newMap.put("account", paraMap.get("account"));
		newMap.put("password", paraMap.get("password"));
		//如果是邮箱注册
		 Matcher email = Constants.EMAIL_ADDRESS.matcher( paraMap.getString("account"));
		//如果是手机注册
		Matcher phone = Constants.PHONE.matcher( paraMap.getString("account"));
		 if (email.matches()) {
			 newMap.put("type", "email");
		 }else if (phone.matches()) {
			 newMap.put("type", "phone");
		}else {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1002,Constants.RESULT_CODE_1002, null, paraMap.getString("language"));
		}		
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);		
	}
	
	/**
	 * 发送验证码
	 * @param paraMap
	 * @return
	 */
	private JSONObject  sendVerifyCode(JSONObject paraMap){
		//参数验证
		if (!paraMap.has("account")) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		//如果是邮箱注册
		 Matcher email = Constants.EMAIL_ADDRESS.matcher( paraMap.getString("account"));
		//如果是手机注册
		Matcher phone = Constants.PHONE.matcher( paraMap.getString("account"));
		if (!email.matches() && !phone.matches()) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1002,Constants.RESULT_CODE_1002, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
//		newMap.put("keyCode", UserEnum.User);
		newMap.put("busiCode", BlockBusiCodeConstant.USER_SEND_VERIFY_CODE);
		newMap.put("account", paraMap.get("account"));
		if (email.matches()) {
			newMap.put("type", "email");
		}
		if (phone.matches()) {
			newMap.put("type", "phone");
		}	
		//忘记密码发送验证码
		if (paraMap.has("forgot")) {
			newMap.put("forgot", paraMap.get("forgot"));
		}
		
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
	}

	/**
	 * 注册
	 * @param paraMap
	 * @return
	 */
	private JSONObject  register(JSONObject paraMap){
		//参数验证
		//isTour 游客方式操作系统，自动新增记录，其他为正式注册页面使用
		if (!paraMap.has("isTour") &&(!paraMap.has("account") || !paraMap.has("password") || !paraMap.has("verifyCode"))) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
//		newMap.put("keyCode", UserEnum.User);
		newMap.put("busiCode", BlockBusiCodeConstant.USER_REGISTER);
		if (paraMap.has("isTour")) {
			newMap.put("isTour", "tour");
			return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
		}
		newMap.put("account", paraMap.get("account"));
		newMap.put("password", paraMap.get("password"));
		newMap.put("verifyCode", paraMap.get("verifyCode"));
		if (paraMap.has("userId")) {
			newMap.put("userId", paraMap.get("userId"));
		}
		//如果是邮箱注册
		 Matcher email = Constants.EMAIL_ADDRESS.matcher( paraMap.getString("account"));
		//如果是手机注册
		Matcher phone = Constants.PHONE.matcher( paraMap.getString("account"));
		
		if (!email.matches() && !phone.matches()) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1002,Constants.RESULT_CODE_1002, null, paraMap.getString("language"));
		}
		if (email.matches()) {
			newMap.put("type","email");
		}
		if (phone.matches()) {
			newMap.put("type", "phone");
		}	
		if (email.matches()) {
			//调用子系统
			newMap.put("type", "email");
		}
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
	}
	
	/**
	 * 上传用户头像
	 * @return
	 * @throws IOException 
	 */
	private JSONObject uploadHeadPhoto(JSONObject paraMap){
		if (!paraMap.has("fileKey") || !paraMap.has("userId") ) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		JSONObject  uploadResult = null;
		Date date = systemTimeMapper.getSystemTime();
		String  timeString = Constants.dfi.format(date);
		paraMap.put("month", timeString);
		uploadResult = FileUtil.upLoadMoreFolder(paraMap);
		if (uploadResult.optInt("code") != 1) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_108, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("keyCode", UserEnum.User);
		newMap.put("busiCode", BlockBusiCodeConstant.UPDATE_HEAD_PHOTO);
		newMap.put("headPhotoUrl", uploadResult.getJSONObject("result").getJSONArray("fileKey").get(0));
		newMap.put("userId", paraMap.get("userId"));
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);
	}
	
	private JSONObject selectPushType(JSONObject paraMap){
		//参数验证
		if (!paraMap.has("msgType") ||!paraMap.has("userId") ) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("busiCode", BlockBusiCodeConstant.SELECT_PUSH_TYPE);
		newMap.put("msgType", paraMap.get("msgType"));
		newMap.put("userId", paraMap.get("userId"));
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
	}
	
	private JSONObject selectPushContentAll(JSONObject paraMap){
		//参数验证
		if (!paraMap.has("msgType")  || !paraMap.has("msgSubType") || !paraMap.has("page")|| !paraMap.has("pageNum")|| !paraMap.has("userId") || !paraMap.has("lastTime") ) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("busiCode", BlockBusiCodeConstant.SELECT_PUSH_CONTENTALL);
		newMap.put("msgType", paraMap.get("msgType"));
		newMap.put("msgSubType", paraMap.get("msgSubType"));
		newMap.put("page", paraMap.get("page"));
		newMap.put("pageNum", paraMap.get("pageNum"));
		newMap.put("userId", paraMap.get("userId"));
		newMap.put("lastTime", paraMap.get("lastTime"));
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
}
	private JSONObject getFirstThreeNews(JSONObject paraMap){
		//参数验证
		if (!paraMap.has("userId")) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("busiCode", BlockBusiCodeConstant.GET_FIRST_THREE_NEWS);
		newMap.put("userId", paraMap.get("userId"));
		newMap.put("keyCode", UserEnum.User);
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);
	}
	
	private JSONObject selectPushInnovationType(JSONObject paraMap){
		//参数验证
		if (!paraMap.has("userId")  ) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("busiCode", BlockBusiCodeConstant.SELECT_PUSH_INNOVATIONTYPE);
		newMap.put("userId", paraMap.get("userId"));
		newMap.put("type", paraMap.get("type"));
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
}
	
	/**
	 * 用户反馈
	 * @param paraMap
	 * @return
	 */
	private JSONObject userFeedback(JSONObject paraMap) {

		//参数验证
		if (!paraMap.has("attitude") || !paraMap.has("content") || !paraMap.has("email")) {
			return  ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_1001, null, paraMap.getString("language"));
		}
//		System.out.println("paraMap="+paraMap);
		//校验邮箱格式
		Matcher email = Constants.EMAIL_ADDRESS.matcher( paraMap.getString("email"));
		Matcher phone = Constants.PHONE.matcher(paraMap.getString("email"));
		if (!email.matches() && !phone.matches()) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1002,Constants.RESULT_CODE_1002, null, paraMap.getString("language"));
		}
		JSONObject  uploadResult = null;
		JSONArray urlArray = null;
		if (paraMap.has("fileKey")) {
			// 图片张数校验
			Object fileObject = paraMap.opt("fileKey");
			if (fileObject instanceof JSONArray && ((JSONArray)fileObject).size() > 9) {
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_1006, Constants.RESULT_CODE_1006, null, paraMap.getString("language"));
			}
			Date date = systemTimeMapper.getSystemTime();
			String  monthString = Constants.sdfm.format(date);
			paraMap.put("month", monthString);
			uploadResult = FileUtil.upLoadMoreFolder(paraMap);
			if (uploadResult.optInt("code") != 1) {
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_108, null, paraMap.getString("language"));
			} else {
				urlArray = uploadResult.optJSONObject("result").optJSONArray("fileKey");
			}
		}
		//组装参数
		JSONObject  newMap = new JSONObject();
		newMap.put("source", Constants.CALL_SOURCE);
		newMap.put("receive", Constants.CALL_TO_USER);
		newMap.put("busiCode", BlockBusiCodeConstant.USER_FEEDBACK);
		newMap.put("attitude", paraMap.get("attitude"));
		newMap.put("content", paraMap.get("content"));
		newMap.put("email", paraMap.get("email"));
		newMap.put("keyCode", UserEnum.User);
		if (urlArray != null) {
			newMap.put("fileKey", uploadResult.optJSONObject("result").optJSONArray("fileKey"));
		}
		
		//调用子系统
		return interfaceInvoking.invoking(ConfigConstants.URL_USER_SYSTEM, newMap, paraMap);	
		
	}
	
	/**
	 * 仅供测试
	 * @param paraMap
	 * @return
	 */
	private  JSONObject  queryUserInfo(JSONObject paraMap){
		
		//url的相关信息，可配置到表中，暂时写死
		String requestUrl = ConfigConstants.URL_USER_SYSTEM;
		//子业务系统调用方法
		paraMap.put("busiCode", BlockBusiCodeConstant.USER_INFO);
		//调用接口
		JSONObject returnObject = interfaceInvoking.invoking(requestUrl, paraMap, paraMap);
		
		return returnObject;
	}

}
