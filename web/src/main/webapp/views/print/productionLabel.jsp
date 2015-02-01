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
<div class="page_padding">
	<div class="content_box">
		<h2>生产标签</h2>
		<div class="import_box">
			<i class="icon-pencil"></i>请输入板号或生产编号进行查询。
			<br />
			<input class="inp_text" type="text" id="seachPrimerProduct" name="boardNo" value="" style="width: 300px" />
			<ul id="seachPrimerProductList"></ul>
		</div>
		<div class="import_box" style="padding-bottom: 50px;">
			<button class="btn-primary submit" type="button" onclick="searchLabel();">查询标签</button>
		</div>
        <div id="downList"></div>
	</div>
</div>
<script src="${ctx}/static/js/vagueSeachBoard.js"></script>
<script src="${ctx}/static/js/productionLabel.js"></script>
<script type="text/javascript">
</script>
</body>
</html>