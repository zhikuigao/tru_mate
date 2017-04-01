<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head lang="en">
 <meta charset="UTF-8">
 <script type="text/javascript" src="../js/jquery2.js"></script>
</head>
<body>
	<form id="addRecApp">
		<div  style="margin-left:30;margin-top:30">
		<table width="415" border="0" cellspacing="5" cellpadding="0">
			<tr height="40">
				<td colspan="2" align="center">添加推荐应用</td> 
			</tr>
			<tr>
				<td style="width:100">用户id:</td>
				<td><input id="userId" name="userId" style="width:300" placeholder="必填"/></td> 
			</tr>
			<tr>
				<td valign="top" style="width:100">应用id:</td>
				<td><input id="id" name="id" style="width:300" placeholder="必填"/></td> 
			</tr>
			<tr>
				<td valign="top" style="width:100">标记:</td>
				<td><label><input name="flag" type="radio" value="0" checked/>下载 </label> 
					<label><input name="flag" type="radio" value="1" />升级 </label> 
				</td> 
			</tr>
			<tr>
				<td style="width:100">&nbsp;</td>
				<td align="right"><input type="button"  onclick="save();" value="保存"/></td> 
			</tr>
		</table>
		</div>
	</form>
</body>
<script type="text/javascript">
	function save() {
		var userId = $('#userId').val();
		var id = $('#id').val();
		if(id.trim()=='' || userId.trim()==''){
			alert("存在空数据");
			return;
		}
		
		var param=$('#addRecApp').serialize();
		$.ajax({ 
	        type: "post", 
	        url: "../addRecApp.do", 
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