package com.jws.app.operater.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import sun.misc.BASE64Decoder;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jws.app.operater.model.Canvas;


@Controller("html2canvasController")
public class Html2canvasController {
	
	@SuppressWarnings("restriction")
	@RequestMapping(value = "/saveCanvas.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String  saveCanvas(@ModelAttribute Canvas canvas){
		if (StringUtils.isEmpty(canvas.getUrl())) {
			return "获取参数为空";
		}
        try {
        	OutputStream os = null;
        	BASE64Decoder d = new BASE64Decoder();
            byte[] bs = d.decodeBuffer(canvas.getUrl().split(",")[1]);
            //配置文件路径，比如：D:\\works\\test\\，正式代码将该路径配置到dataConfig.properties文件内 
            os = new FileOutputStream(new File("D:\\works\\test\\test.png"));
			os.write(bs);
			os.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		return "保存成功";
	}
}
