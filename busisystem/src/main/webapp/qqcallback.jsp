<!DOCTYPE html>
<html>
<head lang="en">
<meta charset="UTF-8">
<script type="text/javascript">
	var locationStr = location.href;
	var access_token = locationStr.substring(locationStr.indexOf("=") + 1, locationStr.indexOf("&"));
	window.location.href = "/busisystem/qqgetopenid.do?token="+access_token;
</script>
</head>
<body>
</body>
</html>

