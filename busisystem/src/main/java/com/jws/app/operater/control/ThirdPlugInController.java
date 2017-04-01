package com.jws.app.operater.control;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jws.app.operater.serviceBlock.plugIn.service.PlugInService;
import com.jws.common.constant.Constants;

/**
 * 第三方插件接入控制层
 * @author ljw
 *
 */
@Controller("thirdPlugInController")
@RequestMapping("/plugIn")
public class ThirdPlugInController {
	@Resource
	private PlugInService plugInService;
	
	@RequestMapping(value = "/gitLab.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject gitLab() throws IOException{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return plugInService.accessPoint(request, Constants.GIT_LAB);
	}
	@RequestMapping(value = "/redMine.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject redMine() throws IOException{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return plugInService.accessPoint(request, Constants.RED_MINE);
	}
	

}
