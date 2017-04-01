<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
    <link rel="shortcut icon" href="imgs/48.icon" type="image/x-icon">
    <link rel="icon" href="imgs/48.icon" type="image/x-icon">
    <link href="base.css" type="text/css" rel="stylesheet"/>
    <link href="feedback.css" type="text/css" rel="stylesheet"/>
</head>
<body>
<div  id="erwerma"></div>
<div id="head">
    <p>小美，您的情感伴侣</p>
    <span onclick="history.go(-1);">返回</span>
</div>
<div id="feedback">
    <!--<p style="margin: 0 auto;text-align: center;font-size: 20px;padding: 10px 0;">反馈</p>-->
    <div id="feedbackbody">
        <div id="feedbackoptions">
            <p><span class="optionsicon"></span><span>喜欢</span></p>
            <p><span class="optionsicon"></span><span>一般</span></p>
            <p><span class="optionsicon"></span><span>不喜欢</span></p>
        </div>
        <div id="feedbackcontent">
            <textarea placeholder="亲，有什么想告诉我的呢？(要10个字符以上哦～)" maxlength="200"></textarea>
            <p><span style="color: red;">0</span>/200</p>
        </div>
        <div id="feedbackphoto">
            <p class="clickContent" id="feedbackupdategif">上传图片</p>
            <form id="uploadFiles" target="fileFrame"
                  method="post" enctype="multipart/form-data" class="hiddenInfo">
            </form>
            <div class="hiddenInfo" id="feedback_uploadFileResult"></div>
            <iframe id="fileFrame" class="hiddenInfo" name="fileFrame"></iframe>
            <ol></ol>
            <p id="getPhotoError"></p>
        </div>
        <p style="text-align:center;font-size: 14px;color: #333333;margin-top: 26px;margin-bottom: 10px;">我们可能需要通过联系方式与你联系来获取更多的细节。</p>
        <div id="feedbackemail">
            <input type="text" value="" placeholder="请输入你的电子邮箱。"/>
            <p></p>
        </div>
        <button id="sendFeedbackButton">发送</button>
    </div>
</div>
<p id="link">
    <a href="http://www.jwis.cn/">杰为软件公司</a>
    <a href="http://market.jwis.cn">PLM应用市场</a>
</p>
<script type="text/javascript" src="jquery2.js"></script>
<script type="text/javascript" src="feedback.js"></script>
<script type="text/javascript" src="md5.js"></script>
</body>
</html>