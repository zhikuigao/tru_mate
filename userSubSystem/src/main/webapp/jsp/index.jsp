<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head lang="en">
 <meta charset="UTF-8">
<script type="text/javascript">
</script>
<style> 
.table-c table{border-right:1px solid #F00;border-bottom:1px solid #F00} 
.table-c table td{border-left:1px solid #F00;border-top:1px solid #F00} 
/* 
css 注释： 
只对table td设置左与上边框； 
对table设置右与下边框； 
为了便于截图，我们将css 注释说明换行排版 
*/ 
</style>
</head>
<body>
	<span>消息中心：<br></span>
	<div class="table-c"> 
	<table width="400" border="0" cellspacing="1" cellpadding="0">
		<tr>
			<td><a href="../jsp/mcPush.jsp" target=_blank>推送消息</a></td> 
			<td><a href="../jsp/addMateVersion.jsp" target=_blank>添加小美版本</a></td>
			<td></td> 
		</tr>
		<tr>
			<td><a href="../jsp/addNotice.jsp" target=_blank>添加公告</a></td> 
			<td><a href="../jsp/addRecommendApply.jsp" target=_blank>添加推荐应用</a></td> 
			<td><a href="../jsp/addRecommendNews.jsp" target=_blank>添加推荐资讯</a></td>
		</tr>
	</table>
	</div>
</body>
</html>