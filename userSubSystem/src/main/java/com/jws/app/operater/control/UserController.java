package com.jws.app.operater.control;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jws.app.operater.service.ITemplateService;
import com.jws.app.operater.service.UserService;

@Controller("userController")
@RequestMapping("/entry")
public class UserController {
	@Resource
	private  ITemplateService templateService;
	@Resource
	private UserService userService;
	
	/**
	 * 用户模块api统一入口
	 * @param paramObject
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/userBlockEntry.do", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject  userBlockEntry(String paramObject) throws IOException {
//		paramObject = new String(paramObject.getBytes("ISO-8859-1"),"utf-8");
		return userService.commonEntry(paramObject);
	}
	
	
	/**
	 * test
	 * 
	 * @param paramObject
	 *            json格式
	 * @return
	 */
	@RequestMapping(value = "/interfaceEntry.do", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<HashMap<String, Object>> interfaceEntry(String paramObject) {
		List<HashMap<String, Object>> resultHashMap = templateService.selectAll();
		return resultHashMap;
	}
	
	
	

	public static void main(String[] args) {
		
	}
}
