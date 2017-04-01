<script type="text/javascript"
src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script type="text/javascript"
src="http://html2canvas.hertzen.com/build/html2canvas.js"></script>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
  	<div>
  	 This is my JSP page. <br>
    <input type = 'button' onclick="html2canvas();" name="截图">
  	</div>
   
  </body>
  <script type="text/javascript">
  	function html2canvas(){
  		html2canvas(document.body.div, {
	    allowTaint: true,
	    taintTest: false,
	    onrendered: function(canvas) {
	        canvas.id = "mycanvas";
	        document.body.appendChild(canvas);
	        //生成base64图片数据
	        var dataUrl = canvas.toDataURL();
	        var newImg = document.createElement("img");
	        newImg.src =  dataUrl;
	        document.body.appendChild(newImg);
		    }
		});  	
  	}
  	
  </script>
</html>
