package com.jws.common.constant;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public  class  ConstantZh {
	@SuppressWarnings("rawtypes")
	public final static Map map = new HashMap();  
	// 1.需吧code反馈给用户， 其他为程序问题
	static {  
		map.put(1, "成功"); 
		map.put(1100, "保存失败");
		map.put(1101, "参数转换异常"); 
	    map.put(1102, "缺少请求参数");
	    map.put(1103, "用户系统安全验证不通过");	    
	    map.put(1104, "程序异常");
	    map.put(1105, "账号已存在");//1
	    map.put(1106, "验证码发送异常");//1
	    map.put(1107, "busiCode 有误");
	    map.put(1108, "新增用户异常");
	    map.put(1109, "验证码已失效");//1
	    map.put(1110, "账号或密码有误");//1
	    map.put(1111, "用户id有误，对应记录不存在或是非游客身份");
	    map.put(1112, "不存在该应用对应的版本信息");
	    map.put(1113, "找不到旧版本对应的信息");
	    map.put(1114, "已经是最新版本");//1
	    map.put(1115, "账号未注册");//1
	    map.put(1116, "验证码错误");//1
	    map.put(1117, "邮箱或手机号格式错误");//1
	    map.put(1118, "论坛发表文章失败");//1
	    map.put(1119, "未监控到数据");
	    map.put(1120, "保存推送消息异常");
	    map.put(1121, "消息发送失败");
	    map.put(1122, "信息已删除");
	}

}
