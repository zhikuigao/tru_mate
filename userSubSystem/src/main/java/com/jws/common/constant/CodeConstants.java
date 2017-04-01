package com.jws.common.constant;


/**
 * 系统常量
 * @author
 *
 */
public class CodeConstants {
	//变量命名请参考以下内容
	public static final int CODE_USER_1001 = 1001;
	public static final String DESC_USER_1001 ="not found this infomation";
	
	//article 相关
	public static final int CODE_ARTICLE_8001 = 8001;
	public static final String DESC_ARTICLE_8001 ="The parent comment information has been deleted";
	public static final int CODE_ARTICLE_8002 = 8002;
	public static final String DESC_ARTICLE_8002 ="This infomation has been deleted";
	
	//频道相关
	public static final int CODE_CHANNEL_2001 = 2001;
	public static final String DESC_CHANNEL_2001 ="The channel information has been deleted";
	public static final String DESC_CHANNEL_2002 ="The channel name is has been";
	public static final String DESC_CHANNEL_2003 ="The jpush is Error";
	
	//注册相关
	public static final String CODE_DIGEST_1001 ="The Validation failure";   //验证码失败
	public static final String CODE_DIGEST_1002 ="The request timeout";   //超时
	
	//文摘相关
	public static final String CODE_COLLECTIONS_9001 ="The collection has been deleted";   //文摘已经删除
	//推送消息相关
	public static final String CODE_JPUSH_MESSAGETYPEEXAMINE ="examine";   //推送消息类型为审核
	public static final String CODE_JPUSH_MESAGEEXAMINE="Hello, you have a user subscription audit!";   //消息类型审核内容
	public static final String CODE_JPUSH_MESSAGETYPENOTICE ="notice";   //推送消息类型为通知
	public static final String CODE_JPUSH_MESAGENOTICESUCCESS="Hello, The administrator has agreed to your application.";       //管理员同意
	//public static final String CODE_JPUSH_MESAGEUSERREJECT="hello,Your friend refused to your invitation.";       //受邀请人不同意加入频道
	public static final String CODE_JPUSH_MESAGENOTICESUCCESSNO="Hello, The administrator has not agreed to   your application.";       //不管理员同意
	public static final String CODE_JPUSH_MESAGENOTICEFAILE="I am sorry, administrators do not agree with your application";       //管理员失败
	public static final String CODE_JPUSH_MESSAGETYPINVITE ="invite";   //推送消息类型为邀请
	public static final String CODE_JPUSH_MESSAGETYPINVITEMESSAGE ="Your friends invite you to join the channel";   //邀请信息
	public static final String CODE_JPUSH_MESSAGETYPENOTICE_DELETE ="You have been removed from the channel.";   //推送消息用户被T除
	public static final String CODE_JPUSH_MESSAGETYPENOTICE_ALLDELETE ="Channel has been dissolved";   //频道已经被解散
	public static final String CODE_JPUSH_MESAGEUSERREJECTFRIEND="Your friends has refuse your invitation.";       //受邀人不同意
	public static final String CODE_JPUSH_MESAGEUSERREJECTFRIENDYES="hello,Your friends  has agreed your invitation.";       //受邀人同意
	public static final String CODE_JPUSH_MESAGEMEMBEREXIT=" has quit the channel.";       //退出频道
	
}
