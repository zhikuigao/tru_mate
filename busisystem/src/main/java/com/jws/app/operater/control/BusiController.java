package com.jws.app.operater.control;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.jws.app.operater.service.BusiService;

@Controller("busiController")
@RequestMapping("/entry")
public class BusiController {
	@Resource
	private BusiService busiService;
	

	/**
	 * 统一post请求(不含文件)
	 * @param 
	 * @return
	 */	
	@RequestMapping(value = "/busiEntry.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject busiSystemInterface(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return busiService.commonEntry(request);
	}
	
	/**
	 * 统一post请求(含文件)
	 * @param 
	 * @return
	 */	
	@RequestMapping(value = "/busiFileEntry.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject busiFileSystemInterface(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return busiService.commonFileEntry(request);
	}
	
	/**
	 * 上传文件接口
	 * @return
	 */
//	@RequestMapping(value = "/uploadFile.do",produces="text/html;charset=UTF-8")
//	@ResponseBody
//	public String uploadForIFrame() {
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//		JSONObject returnObject = busiService.commonFileEntry(request);
//		String urls = returnObject.optString("resultData");
////		return "<script type=\"text/javascript\">localStorage.uploadCallbackText = " + urls + "</script>";
//		return "<script type=\"text/javascript\">parent.document.getElementById(\"feedback_uploadFileResult\").innerText =" + urls + "</script>";
//	}
	/**
	 * 获取系统时间
	 * @param 
	 * @return
	 */	
	@RequestMapping(value = "/getSysTime.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public long getSysTime(){
		return busiService.getSysTime();
	}
	
	
//	public static void main(String[] args) {
//		String path="D:\\works\\mate\\temp\\b111945c1ddd446bb7af74313fa4f74e\\123.txt";
//		File file= new File(path);
//		System.out.println(file.getParentFile().getPath());
//		FileUtil.deleteFile(file.getParentFile());
//	}
}
