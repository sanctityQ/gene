<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<link href="${ctx}/static/css/easyui.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/icon.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/perfect-scrollbar.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/master.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/font-awesome.min.css" />
<script src="${ctx}/static/js/jquery.min.js" ></script>
<script src="${ctx}/static/js/jquery.easyui.min.js" ></script>
<script src="${ctx}/static/js/perfect-scrollbar.min.js" ></script>
<script src="${ctx}/static/js/index.js" ></script>
<script src="${ctx}/static/js/json2.js"></script>
</head>
<body>
<form name="form" action="${ctx}/synthesis/exportPackTable/" method="post">
	<div class="page_padding">
		<div class="content_box">
			<h2>导出分装表</h2>
			<div class="import_box">
				<i class="icon-inbox"></i>请选择板号后导出相应分装表。
				<br />
				<input class="inp_text" id="boardNo" type="text" name="boardNo" value="" style="width: 300px" />
				<ul id="seachList"></ul>
			</div>
	
			<div class="import_box" style="padding-bottom: 50px;">
				<button class="btn-primary submit" type="button" onclick="exportPackTable()">导出分装表</button>
			</div>
		</div>
	</div>
</form>
<script src="${ctx}/static/js/packTable.js" ></script>
</body>
</html>