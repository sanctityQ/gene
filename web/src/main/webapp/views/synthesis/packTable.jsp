<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
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
				<input class="inp_text" autocomplete="off" id="boardNo" type="text" name="boardNo" value="" style="width: 300px" />
				<ul id="seachBoardList"></ul>
			</div>
	
			<div class="import_box" style="padding-bottom: 50px;">
				<button class="btn-primary submit" type="button" onclick="exportPackTable()">导出分装表</button>
			</div>
		</div>
	</div>
</form>
<script src="${ctx}/static/js/packTable.js" ></script>
<script src="${ctx}/static/js/vagueSeachBoard.js"></script>
</body>
</html>