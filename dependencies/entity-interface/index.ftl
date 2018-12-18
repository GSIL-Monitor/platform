<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>.ftl</title>
</head>
<body>
<#list appInsList as appIns>
	<#list groupList as group>
		<#if appIns.groupId == group.groupId>
			<li><a id="groupName">${group.groupName!}</a></li>
		</#if>
	</#list>
</#list>
</body>
</html>