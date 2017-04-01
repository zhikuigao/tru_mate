package com.jws.common.constant;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public  class  ConstantZh {
	@SuppressWarnings("rawtypes")
	public final static Map map = new HashMap();  
	// 1.需把code反馈给用户， 其他为程序问题
	static {  
		map.put(1, "成功"); 
	    map.put(2001, "busiCode 有误");
	    map.put(2002, "参数转换异常"); 
	    map.put(2003, "缺少请求参数");
	    map.put(2004, "用户系统安全验证不通过");
	    
	    map.put(2101, "查询所有应用种类失败");
	    map.put(2201, "查询所有应用信息失败");
	    map.put(2301, "查询应用详情失败");
	    map.put(2601, "应用点赞失败");
	    map.put(2602, "已赞啦，实在太喜欢可以去评论区看看哦");
	    map.put(2603, "未找到对应的信息");
	    map.put(2604, "用户信息待完善");
	}

}
