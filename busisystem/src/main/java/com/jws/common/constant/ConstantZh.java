package com.jws.common.constant;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public  class  ConstantZh {
	@SuppressWarnings("rawtypes")
	public final static Map map = new HashMap();  
	static {  
		map.put(1, "成功"); 
		// 业务系统100
		map.put(100, "请求参数为空"); 
	    map.put(101, "缺少请求参数");  
	    map.put(102, "busiBlock 有误");  
	    map.put(103, "busiCode 有误");  
	    map.put(104, "调用子系统出错");
	    map.put(105, "json格式有误");
	    map.put(106, "程序异常");
	    map.put(107, "转换文件流失败");
	    map.put(108, "保存流文件异常");
	    map.put(109, "安全验证不通过"); 
	    map.put(110, "上传文件时未配置对应的路径"); 
	    map.put(111, "请求参数有误");
	    map.put(112, "登录已过期");
		//用户子系统1001
	    map.put(1001, "缺少请求参数");  
	    map.put(1002, "账号格式有误,请输入正确的邮箱或手机号");  
	    map.put(1005, "抓取用户行为上传文件异常");
	    map.put(1006, "图片数量过多");
	    // 应用子系统2001
	    map.put(2001, "缺少请求参数"); 
	    // 搜索子系统3001
	    map.put(3001, "缺少请求参数");
	    map.put(3002, "搜索记录文件上传异常");
	    // 第三方应用接入
	    map.put(4001, "请求参数有误");
	    map.put(4002, "获取body信息失败");
	}

}
