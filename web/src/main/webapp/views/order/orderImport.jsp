<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<link href="${ctx}/static/css/easyui.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/icon.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/perfect-scrollbar.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/master.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/font-awesome.min.css" />
  <link rel="stylesheet" href="${ctx}/static/css/font-awesome-ie7.min.css">
<script src="${ctx}/static/js/jquery.min.js" ></script>
<script src="${ctx}/static/js/jquery.easyui.min.js" ></script>
<script src="${ctx}/static/js/perfect-scrollbar.min.js" ></script>
<script src="${ctx}/static/js/ajaxfileupload.js" ></script>
<script src="${ctx}/static/js/index.js" ></script>
<title>订单导入</title>
</head>
<body>
<form id="fm"  method="post" enctype="multipart/form-data" class="form-horizontal">
<div class="page_padding">
	<div class="content_box">
		<h2>导入单订信息</h2>
		<div class="import_box">
			<i class="icon-group"></i>请输入客户姓名或客户代码。
			<br />
			<input class="inp_text" id="customerCode" type="text" name="customerCode" value="" style="width: 300px" />
			<ul id="seachList"></ul>
		</div>
		<div class="import_box" style="line-height: 16px;">
			<i class="icon-upload-alt"></i>上传您的excel模板，系统将根据您导入的信息生成订单。
			<br />
			<a href="${ctx}/order/downLoad" class="down_excel">下载excel模板文件…</a>
			<br /><br />
			<div class="file_box">
				<input name="file" type="file" onchange="document.getElementById('viewfile').value=this.value;" class="file" id="upload" /> 
				<input name="file" type="text" id="viewfile" class="inp_text" value="请选择文件…" style="width: 220px;" /> 
				<label class="btn" for="unload">浏览…</label>
			</div>
		</div>
		<div class="import_box" style="padding-bottom: 50px;">
			<button class="btn-primary submit" type="" onclick="ajaxFileUpload();">生成订单</button>
		</div>
	</div>
</div>
<script src="${ctx}/static/js/vagueSeach.js" ></script>
</form>
</body>
</html>