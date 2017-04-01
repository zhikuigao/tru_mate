package com.jws.app.operater.control;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jws.app.operater.service.FactoryService;

@Controller("factoryController")
@RequestMapping("/factory")
public class FactoryController {
	@Resource
	private FactoryService factoryService;
	
	@RequestMapping(value = "/upload.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String upload(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return factoryService.upload(request);
	}
	
	@RequestMapping(value = "/saveFactory.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String saveFactory(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return factoryService.saveFactory(request);
	}
	
	@RequestMapping(value = "/getFactory.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getFactory(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return factoryService.getFactory(request);
	}

}
