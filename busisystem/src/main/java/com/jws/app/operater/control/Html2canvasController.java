package com.jws.app.operater.control;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jws.app.operater.model.Canvas;
import com.jws.app.operater.service.BlockEntryService;


@Controller("html2canvasController")
public class Html2canvasController {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private BlockEntryService  searchService;
	
	@RequestMapping(value = "/saveCanvas.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String  saveCanvas(@ModelAttribute Canvas canvas){
		if (null == canvas) {
			return "获取参数失败";
		}
		try {
//			System.out.println(canvas.getPicUrl());
			JSONObject param = JSONObject.fromObject(canvas);
			JSONObject result = searchService.entry(param);
			if(null != result){
				return result.getJSONObject("result").getString("desc");
			}
		} catch (Exception e) {
			logger.error("html2canvas获取图片异常"+e.getMessage());
		}
		
		return "保存失败";
	}
}
