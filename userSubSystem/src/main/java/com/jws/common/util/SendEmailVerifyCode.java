package com.jws.common.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;

import com.jws.common.constant.ConfigConstants;


public  class SendEmailVerifyCode {
	
	  public static int SendEmailVerifyCode(String email, String VerifyCode, String language) throws  Exception {
		        // 配置发送邮件的环境属性
		        final Properties props = new Properties();
		        // 表示SMTP发送邮件，需要进行身份验证
		        props.put("mail.smtp.auth", "true");
		        props.put("mail.smtp.host", "smtp.jwis.cn");
		        // 发件人的账号
		        props.put("mail.user", ConfigConstants.MAIL_USER);
		        // 访问SMTP服务时需要提供的密码
		        props.put("mail.password", ConfigConstants.MAIL_PWD);
		        // 构建授权信息，用于进行SMTP进行身份验证
		        Authenticator authenticator = new Authenticator() {
		            @Override
		            protected PasswordAuthentication getPasswordAuthentication() {
		                return new PasswordAuthentication(ConfigConstants.MAIL_USER, ConfigConstants.MAIL_PWD);
		            }
		        };
		        // 使用环境属性和授权信息，创建邮件会话
		        Session mailSession = Session.getInstance(props, authenticator);
		        // 创建邮件消息
		        MimeMessage message = new MimeMessage(mailSession);
		        // 设置发件人
		        String address = "";
		        if (StringUtils.equals(language, "ZH")) {
		        	address = "杰为软件 小美";
				}else {
					address = "JWIS MATE";
				}
		        InternetAddress form = new InternetAddress(ConfigConstants.MAIL_USER,MimeUtility.encodeText(address,MimeUtility.mimeCharset("gb2312"),null));
		        message.setFrom(form);
		        // 设置收件人
		        InternetAddress to = new InternetAddress(email);
		        message.setRecipient(RecipientType.TO, to);
		        if (StringUtils.equals(language, "ZH")) {
		        	 message.setSubject(MimeUtility.encodeText("小美系统用户验证码",MimeUtility.mimeCharset("gb2312"), null));
		        	 // 设置邮件的内容体
				        message.setContent("<html><body><p align=\"center\"> <span style=\"font-size:32px;\">小&nbsp; 美</span></p><p align=\"center\"><br />"
				        		+ "<span style=\"color:#aaaaaa;\"> &nbsp;##-&nbsp; 小美应用注册中 -##</span></p><div style=\"width:540px; margin: 0px auto 20px; padding: 15px; line-height: 18px; font-family: Helvetica,Arial,sans-serif; font-size: 12px; color:#444444; background: #ffffff; border-radius: 5px; border: 1px solid #eeeeee\">"
				        		+ "您好：<br />感谢您使用人见人爱的小美,您正在进行邮箱验证，验证码：<span style=\"font-weight:bold;color:#00ff00;\">"+VerifyCode+"</span>.(请在 "+ConfigConstants.MAIL_TIME_OUT_ZH+" 分钟内完成验证)</br></br>	祝您好运,<br/>杰为软件 小美"
				        		+ "<p align=\"center\"><span style=\"color:#aaaaaa;\"><br /></span> </div><br /></p></body></html>", 
				        		"text/html;charset=UTF-8");
		        }else {
		        	 message.setSubject(MimeUtility.encodeText("User verification code by mate system",MimeUtility.mimeCharset("gb2312"), null));
		        	 // 设置邮件的内容体
				        message.setContent("<html><body><p align=\"center\"> <span style=\"font-size:32px;\">M&nbsp; A&nbsp; T&nbsp; E</span></p><p align=\"center\"><br />"
				        		+ "<span style=\"color:#aaaaaa;\"> &nbsp;##-&nbsp; Mate system registration process -##</span></p><div style=\"width:540px; margin: 0px auto 20px; padding: 15px; line-height: 18px; font-family: Helvetica,Arial,sans-serif; font-size: 12px; color:#444444; background: #ffffff; border-radius: 5px; border: 1px solid #eeeeee\">"
				        		+ "Hi dear;<br />Thank you for using lovely mate,you are in the process of validation，The verification code for this request：<span style=\"font-weight:bold;color:#00ff00;\">"+VerifyCode+"</span>.(please complete the verification in "+ConfigConstants.MAIL_TIME_OUT_EN+" minutes)</br></br>	Best,<br/>Jwis mate"
				        		+ "<p align=\"center\"><span style=\"color:#aaaaaa;\"><br /></span> </div><br /></p></body></html>", 
				        		"text/html;charset=UTF-8");
				}
		        // 发送邮件
		        Transport.send(message);
		  return 1;
	  }

}
