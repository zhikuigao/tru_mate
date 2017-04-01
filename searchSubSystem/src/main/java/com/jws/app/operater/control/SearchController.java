package com.jws.app.operater.control;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jws.app.operater.service.SearchService;

@Controller("searchController")
@RequestMapping("/entry")
public class SearchController {
	@Resource
	private SearchService searchService;
	
	/**
	 * app模块api统一入口
	 * @param paramObject
	 * @return
	 */
	@RequestMapping(value = "/searchBlockEntry.do", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject  searchBlockEntry() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String param = request.getParameter("paramObject");
		return searchService.commonEntry(param);
	}
}
