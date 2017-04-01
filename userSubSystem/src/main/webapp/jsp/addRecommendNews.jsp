<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head lang="en">
 <meta charset="UTF-8">
 <script type="text/javascript" src="../js/jquery2.js"></script>
</head>
<body>
	<form id="addRecNews">
		<div  style="margin-left:30;margin-top:30">
		<table width="415" border="0" cellspacing="5" cellpadding="0">
			<tr height="40">
				<td colspan="2" align="center">添加推荐资讯</td> 
			</tr>
			<tr>
				<td style="width:100">用户id:</td>
				<td><input id="userId" name="userId" style="width:300" placeholder="必填"/></td> 
			</tr>
			<tr>
				<td valign="top" style="width:100">来源:</td>
				<td><input id="source" name="source" style="width:300" placeholder="必填小于等于10个字符"/></td> 
			</tr>
			<tr>
				<td style="width:100">标题:</td>
				<td><input id="title" name="title" style="width:300" placeholder="必填"/></td> 
			</tr>
			<tr>
				<td valign="top" style="width:100">详情url:</td>
				<td><input id="url" name="url" style="width:300" placeholder="必填"/></td> 
			</tr>
			<tr>
				<td valign="top" style="width:100">分类:</td>
				<td><input id="categoryName" name="categoryName" style="width:300" placeholder="必填小于等于20个字符"/></td> 
			</tr>
			<tr>
				<td valign="top" style="width:100">阅读量:</td>
				<td><input id="readNum" name="readNum" style="width:300"placeholder="必填" /></td> 
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
		var title = $('#title').val();
		var userId = $('#userId').val();
		var source = $('#source').val();
		var url = $('#url').val();
		var categoryName = $('#categoryName').val();
		var readNum = $('#readNum').val();
		if(title.trim()=='' || userId.trim()==''|| url.trim()==''
			|| source.trim()==''|| categoryName.trim()==''|| readNum.trim()==''){
			alert("存在空数据");
			return;
		}
		if(source.length > 10){
			alert("来源最大字符长度为10");
			return;
		}
		if(categoryName.length > 20){
			alert("类别最大字符长度为20");
			return;
		}		
		var param=$('#addRecNews').serialize();
		$.ajax({ 
	        type: "post", 
	        url: "../addRecNews.do", 
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