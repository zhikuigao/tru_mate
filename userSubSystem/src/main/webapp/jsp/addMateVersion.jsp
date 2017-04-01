<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head lang="en">
 <meta charset="UTF-8">
 <script type="text/javascript" src="../js/jquery2.js"></script>
</head>
<body>
	<form id="addAppVersion">
		<div  style="margin-left:30;margin-top:30">
		<table width="430" border="0" cellspacing="5" cellpadding="0">
			<tr height="40">
				<td colspan="2" align="center">添加小美版本</td> 
			</tr>
			<tr>
				<td style="width:110">应用名称:</td>
				<td><input id="appName" name="appName" style="width:300" value="mate"/></td> 
			</tr>
			<tr>
				<td style="width:110">适用设备:</td>
				<td>
					<select name="useType">
					  <option  value ="PC">PC</option>
					  <option  value ="ANDROID">ANDROID</option>
					  <option  value="IOS">IOS</option>
					</select>
				</td> 
			</tr>
			<tr>
				<td valign="top" style="width:110">安装包名称:</td>
				<td><input id="fileUrl" name="fileUrl" style="width:300" placeholder="必填"/></td> 
			</tr>
			<tr>
				<td valign="top" style="width:110">版本号:</td>
				<td><input id="versionNum" name="versionNum" style="width:300" placeholder="必填,格式如 1.1.0"/></td> 
			</tr>
			<tr>
				<td valign="top" style="width:110">版本描述中文:</td>
				<td><textarea id="versionDescZh" name="versionDescZh" rows="10" cols="40" placeholder="必填"></textarea></td>
			</tr>
			<tr>
				<td valign="top" style="width:110">版本描述中文(含图片):</td>
				<td><textarea id="versionDescImgZh" name="versionDescImgZh" rows="10" cols="40" placeholder="必填"></textarea></td>
			</tr>
			<tr>
				<td valign="top" style="width:110">版本描述英文:</td>
				<td><textarea id="versionDescEn" name="versionDescEn" rows="10" cols="40" ></textarea></td>
			</tr>
			<tr>
				<td valign="top" style="width:110">版本描述英文(含图片):</td>
				<td><textarea id="versionDescImgEn" name="versionDescImgEn" rows="10" cols="40" ></textarea></td>
			</tr>
			<td valign="top" style="width:100">更新标记:</td>
				<td><label><input name="updateFlag" type="radio" value="0" checked/>选择升级</label> 
					<label><input name="updateFlag" type="radio" value="1" />强制更新 </label> 
				</td>
			<tr>
				<td style="width:110"><input type="button"  onclick="save();" value="&nbsp;&nbsp;保存&nbsp;&nbsp;"/></td> 
				<td align="right"><input type="button"  onclick="getNewestVersion();" value="查看最新版本信息"/></td>
			</tr>
		</table>
		</div>
	</form>
</body>
<script type="text/javascript">
	function save() {
		var fileUrl = $('#fileUrl').val();
		var versionNum = $('#versionNum').val();
		var versionDescZh = $('#versionDescZh').val();
		if(fileUrl.trim()=='' || versionNum.trim()==''|| versionDescZh.trim()==''){
			alert("请完善必填项");
			return;
		}
		var param=$('#addAppVersion').serialize();
		$.ajax({ 
	        type: "post", 
	        url: "../addAppVersion.do", 
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
	function getNewestVersion() {
		var param=$('#addAppVersion').serialize();
		$.ajax({ 
	        type: "post", 
	        url: "../getNewestVersion.do", 
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