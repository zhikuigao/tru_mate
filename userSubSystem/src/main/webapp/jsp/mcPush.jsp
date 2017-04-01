<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head lang="en">
 <meta charset="UTF-8">
 <script type="text/javascript" src="../js/jquery2.js"></script>
</head>
<body>
	<table>
		<tr>
			<td><input type="button" id="recommendNotice" onclick="recommendNotice();" value="推送系统消息"/></td> 
			<td><input type="button" id="recommendTiming" name="recommendTiming" onclick="recommendTiming();" value="监控定时发送数据"/></td> 
			<td><input type="button" id="pushFailData" onclick="pushFailData();" value="监控推送失败的消息并推送"/></td> 
		</tr>
	</table>
</body>
<script type="text/javascript">
	function recommendNotice() {
		$.ajax({ 
	        type: "post", 
	        url: "../recommendNotice.do", 
	        cache:false, 
	        async:false, 
	        data: "", 
	        dataType:"text",
	        success: function(result){
	          alert(result);
	        }, 
	        error: function(result){
	        	alert(result.status);
	        }
		});
	};
	function recommendTiming() {
		$.ajax({ 
	        type: "post", 
	        url: "../recommendTiming.do", 
	        cache:false, 
	        async:false, 
	        data: "", 
	        dataType:"text",
	        success: function(result){
	          alert(result);
	        }, 
	        error: function(result){
	        	alert(result.status);
	        }
		});
	};
	function pushFailData() {
		$.ajax({ 
	        type: "post", 
	        url: "../pushFailData.do", 
	        cache:false, 
	        async:false, 
	        data: "", 
	        dataType:"text",
	        success: function(result){
	          alert(result);
	        }, 
	        error: function(result){
	        	alert(result.status);
	        }
		});
	};
</script>
</html>