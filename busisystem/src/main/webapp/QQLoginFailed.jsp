<!DOCTYPE html>
<html>
<head lang="en">
<meta charset="UTF-8">
<script type="text/javascript">
	function jump(){
		var ip = document.getElementById("ip").innerText.trim();	
		location.href = ip+"/login";
	};
</script>
</head>
<body onload="jump();"> 
	<span style="display:none;" id="ip">${ip}</span>
</body>
</html>
