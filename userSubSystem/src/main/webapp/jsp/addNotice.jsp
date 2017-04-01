<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head lang="en">
 <meta charset="UTF-8">
 <script type="text/javascript" src="../js/jquery2.js"></script>
</head>
<body>
	<form id="addNotice">
		<div  style="margin-left:30;margin-top:30">
		<table width="400" border="0" cellspacing="5" cellpadding="0">
			<tr height="40">
				<td colspan="2" align="center">添加公告</td> 
			</tr>
			<tr>
				<td style="width:90">标题:</td>
				<td><input id="title" name="title" style="width:300" placeholder="必填"/></td> 
			</tr>
			<tr>
                <td style="width:90">有效时长:</td>
                <td><input id="effectiveTime" name="effectiveTime" style="width:300" placeholder="必填，单位秒"/></td> 
            </tr>
			<tr>
				<td valign="top" style="width:90">详情:</td>
				<td><textarea id="content" name="content" rows="10" cols="40" placeholder="必填"></textarea></td>
			</tr>
			<tr>
				<td style="width:90">&nbsp;</td>
				<td align="right"><input type="button"  onclick="save();" value="保存"/></td> 
			</tr>
		</table>
		</div>
	</form>
</body>
<script type="text/javascript">
	function save() {
		var title = $('#title').val();
		if(title==''){
			alert("标题不能为空");
			return;
		}
		var content = $('#content').val();
		if(content==''){
			alert("详情不能为空");
			return;
		}
		var effectiveTime = $('#effectiveTime').val();
		if(effectiveTime==''){
			alert("有效时长为空");
			return;
		}
		if(isNaN(effectiveTime)){
			alert("有效时长必须是数字");
			return;
		}
		var param=$('#addNotice').serialize();
		$.ajax({ 
	        type: "post", 
	        url: "../addNotice.do", 
	        cache:false, 
	        async:false, 
	        data: param, 
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