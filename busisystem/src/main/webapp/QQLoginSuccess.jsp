<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head lang="en">
<meta charset="UTF-8">
<script type="text/javascript">
	function QQloinBack() {
		var ip = document.getElementById("ip").innerText.trim();
		var userId = document.getElementById("userId").innerText.trim();
		var token = document.getElementById("token").innerText.trim();		
		location.href = ip+"users/signin?loginFlag=QQLogin&userId=" + userId+"&token="+token;
	};
</script>
</head>
<body onload="QQloinBack();" >
	<span style="display:none;" id="userId">${userId}</span>
	<span style="display:none;" id="ip">${ip}</span>
	<span style="display:none;" id="token">${token}</span>
</body>
</html>