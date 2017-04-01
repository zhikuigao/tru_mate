package com.jws.app.operater.control;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jws.app.operater.service.ViewService;
/**
 * 第三方登录回调控制层
 * @author ljw
 *
 */
@Controller("viewControl")
public class ViewControl {
	
	@Resource
	private ViewService viewService;
	
	/**
	 * qq回调 pc端使用
	 * @param 
	 * @return
	 */	
	@RequestMapping(value = "/entry/qqcallback.do",produces="application/json;charset=UTF-8")
	public String  qqcallback(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return viewService.qqcallback(request);
	}
	/**
	 * qq回调 页面跳转 pc端使用
	 * @param 
	 * @return
	 */	
	@RequestMapping(value = "/qqgetopenid.do",produces="application/json;charset=UTF-8")
	public String  qqgetopenid(ModelMap model){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return viewService.qqgetopenid(request, model);
	}

}
