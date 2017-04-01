<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
	<head lang="en">
	 <meta charset="UTF-8">
	 <script type="text/javascript" src="../js/jquery2.js"></script>
	 <script type="text/javascript" src="../js/html2canvas.js"></script>
	 <script type="text/javascript" src="../js/jquery.qrcode.min.js"></script>
	 <script type="text/javascript" src="../js/excanvas.compiled.js"></script>
	</head>
  
  <body onLoad="init()">
  	<input type="button" id="getPhoto" value="截图">
  	<div class="div1">
  	 	<font color="red">This is my JSP page. <br></font>
	    <font color="blue">This is my JSP page. <br></font>
	    <font color="orange">This is my JSP page. <br></font>
	    <img alt="" src="../picture/flower.jpg" style="height:100px;width:100px;"><br>
	    <font color="red">This is my JSP page. <br></font>
	    <font color="blue">This is my JSP page. <br></font>
	    <font color="orange">This is my JSP page. <br></font>
	    <div id="code">
	    		<div id="codeico"></div>
	    </div>
  	</div>  	
  </body>
   <style type="text/css">
        #codeico{
            position:fixed;//生成绝对定位的元素，相对于浏览器窗口进行定位。元素的位置通过 "left", "top", "right" 以及 "bottom" 
            z-index:9999999;
            width:84px; 
            height:84px;
            background:url(../picture/mate_logo.png) no-repeat;
        }
    </style>
  <script type="text/javascript">
  function init() {
         $("#code").qrcode({ 
		    render: "canvas", //table方式 
		    width: 200, //宽度 
		    height:200, //高度 
		    typeNumber:-1,//计算模式
            correctLevel:2,//二维码纠错级别
		    text: "http://192.168.1.153:8080/busisystem/searchMap/SearchMap.jsp?id=001467019534211&map=TRUE", //任意内容 
		    background: "#ffffff", //背景颜色 
	  		foreground: "black" //前景颜色 
		});
        var margin = ($("#code").height()-$("#codeico").height())/2;
      $("#codeico").css("margin",margin);
    }
   document.querySelector("#getPhoto").addEventListener("click", function() {
        html2canvas(document.querySelector(".div1"), {
	    allowTaint: true,
	    taintTest: false,
	    onrendered: function(canvas) {
	        //canvas.id = "mycanvas";
	        //document.body.appendChild(canvas);
	        //生成base64图片数据
	        var imgData = canvas.toDataURL();
	       // var newImg = document.createElement("img");
	        //newImg.src =  imgData;
	        //document.body.appendChild(newImg);
	       	// 加工image data，替换mime type
	       	// var type = "png";
			//imgData = imgData.replace(_fixType(type),'image/octet-stream'); 
			// download
			//alert(imgData);
			saveFile(imgData);
		    }
		});
			
    }, false);
  /**
	 * 在本地进行文件保存
	 * @param  {String} data     要保存到本地的图片数据
	 * @param  {String} filename 文件名
	 */
	var saveFile = function(data){
	var url = 'url='+ encodeURIComponent(data);
	    $.ajax({ 
	        type: "post", 
	        url: "../saveCanvas.do", 
	        cache:false, 
	        async:false, 
	        data: url, 
	        dataType:"text",
	        success: function(result){
	          alert(result);
	        }, 
	        error: function(result){
	        	alert(result);
	        }
		});
	};   
	
	</script>
</html>
