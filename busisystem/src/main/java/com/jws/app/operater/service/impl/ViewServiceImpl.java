package com.jws.app.operater.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.jws.app.operater.service.BlockEntryService;
import com.jws.app.operater.service.ViewService;
import com.jws.common.constant.ConfigConstants;
import com.jws.common.util.GetOpenId;

@Service("viewService")
public class ViewServiceImpl implements ViewService{
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private BlockEntryService  userService;
	
	@Override
	public String qqcallback(HttpServletRequest request) {
		return "qqcallback";
	}

	@Override
	public String qqgetopenid(HttpServletRequest request, ModelMap model) {
		model.put("ip",ConfigConstants.QQ_CALLBACK_IP);
		//提供给腾讯审核的实现
//		try {
//			String openid = GetOpenId.GetOpenId(request.getParameter("token"));
//			if (StringUtils.isEmpty(openid)) {
//				return "mateIndex";
//			}
//			String nickname = GetOpenId.GetNickName(request.getParameter("token"), openid);
//			model.put("nickname",nickname);
//			System.out.println("openid="+openid);
//			System.out.println("nickname="+nickname);
//			System.out.println("token="+request.getParameter("token"));
//		} catch (Exception e) {
//			logger.error("调用腾讯接口异常:"+e.getMessage());
//		}
//		return "mateIndex";
		
		
		//正常登陆实现
		try {
			String openid = GetOpenId.GetOpenId(request.getParameter("token"));
			System.out.println("request token = "+request.getParameter("token"));
			if (StringUtils.isEmpty(openid)) {
				return "QQLoginFailed";
			}
			JSONObject param = new JSONObject();
			param.put("busiCode", "loginThirdParty");
			param.put("token", request.getParameter("token"));
			param.put("openId", openid);
			param.put("language", "EN");
			param.put("type", "qq");
			param.put("device", "pc");
			JSONObject json = userService.entry(param);
			if (null != json && json.has("result") && json.has("resultData")) {
				JSONObject data = json.getJSONObject("resultData");
				model.put("userId",data.getString("id"));
				model.put("token",data.getString("token"));
				System.out.println("token = "+data.getString("token"));
				return "QQLoginSuccess";
			}
		} catch (Exception e) {
			System.out.println("调用QQ接口异常"+e.getMessage());
			logger.error("调用QQ接口异常"+e.getMessage());
			return "QQLoginFailed";
		}
		return "QQLoginFailed";
	}
	

}
