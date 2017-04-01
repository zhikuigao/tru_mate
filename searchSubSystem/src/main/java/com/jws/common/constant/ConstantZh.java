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
		map.put(3000, "程序异常");
	    map.put(3001, "busiCode 有误");
	    map.put(3002, "参数转换异常"); 
	    map.put(3003, "缺少请求参数");
	    map.put(3004, "用户系统安全验证不通过");
	    map.put(3005, "未找到对应的地图信息");
	    map.put(3006, "用户信息有误");
	    map.put(3007, "数据id有误");
	    map.put(3101, "保存搜索条件失败");
	    map.put(3102, "查询搜索地图失败");
	    map.put(3201, "保存搜索的查看记录失败");
	    map.put(3301, "查询用户搜索历史失败");
	    map.put(3401, "查询搜索条件对应的所有链接失败");
	    map.put(3501, "保存搜索记录失败");
	    map.put(3601, "参数有误");
	    map.put(3602, "名称或链接已经存在");
	}

}
