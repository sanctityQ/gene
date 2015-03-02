<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
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
</head>
<body>
<form id="form" modelAttribute="user" action="${ctx}/synthesis/uploadMeasure" method="post" enctype="multipart/form-data" class="form-horizontal">
<div class="page_padding">
	<div class="content_box">
	    <input type="hidden" id="operationType" name="operationType" value="measure"/>
		<h2>测值结果</h2>
		<div class="import_box">
			<i class="icon-group"></i>请输入需要导入的板号。
			<br />
			<input class="inp_text" type="text" autocomplete="off" id="boardNo" name="boardNo" value="" style="width: 300px" />
			<ul id="seachBoardList"></ul>
		</div>
		<div class="import_box" style="line-height: 16px;">
			<i class="icon-upload-alt"></i>上传您的测值模板，系统将根据您导入的信息关联至板号。
			<br />
			<a href="${templateFilePath}" class="down_excel">下载excel模板文件…</a>
			<br /><br />
			<div class="file_box">
				<input name="file" type="file" onchange="document.getElementById('viewfile').value=this.value;" class="file" id="upload" /> 
				<input name="file" type="text" id="viewfile" class="inp_text" value="请选择文件…" style="width: 220px;" /> 
				<label class="btn" for="upload">浏览…</label>
			</div>
		</div>
		<div class="import_box" style="padding-bottom: 50px;">
			<button class="btn-primary submit" type="button" onclick="goToResultsBoard()">测值结果</button>
		</div>
	</div>
</div>
</form>
<script src="${ctx}/static/js/vagueSeachBoard.js"></script>
<script type="text/javascript">
function goToResultsBoard(){
	var boardNo = $.trim($('#boardNo').val());
	var viewfile = $.trim($('#viewfile').val());

	if(boardNo == ""){
		alert("请输入板号。");
		return false;
	}
	if(viewfile == "请选择文件…"){
		alert("请选择文件。");
		return false;
	}
	if($("#upload").val().length > 1) {
		var lexcel = $("#upload").val().lastIndexOf(".");
		var type = $("#upload").val().substring(lexcel + 1);
		
		if(type != "xls" && type != "xlsx") {
			alert("请您上传excel格式文件！");
			return false;
		}		
	}
	$('#form').submit();
}
</script>
</body>
</html>