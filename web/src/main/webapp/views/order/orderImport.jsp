<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
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
<script type="text/javascript">
 var ctx = '${ctx}';
 function uploadSubmit(){
	 //只有梓熙的用户才要修选择客户，其他公司的用户默认为自己的公司
	 if($("#customerid").val()=="" && $("#customerFlagOld").val()=="0"){
		 alert("请录入客户公司代码或名称！");
		 return false;
	 }
	 if($("#contactsid").val()=="" && $("#seachContacts").val()!=""){
		 alert("请从联系人查询的结果列表中选择！");
		 return false;
	 }
	 
	 if($("#upload").val()==""){
		 alert("请上传文件！");
		 return false;
	 }
	 if($("#upload").val().length > 1) {		
		var lexcel = $("#upload").val().lastIndexOf(".");
		var type = $("#upload").val().substring(lexcel + 1);
		
		if(type != "xls") {
			alert("请您上传excel格式文件！");
			return false;
		}		
	}
	 progress();
	 $('#inputForm').submit();
 }
 //暂时处理后台异常
 var userExp = '${userExp}';
 if(userExp!=''){ alert(userExp)}
</script>
</head>
<body>
<form id="inputForm" modelAttribute="user" action="${ctx}/order/upload" method="post" enctype="multipart/form-data" class="form-horizontal">
          <input type="hidden" id="customerFlagOld" name="customerFlagOld" value="${customerFlag}"/>
<div class="page_padding">
	<div class="content_box">
		<h2>导入单订信息</h2>
		<c:if test="${customerFlag=='0'}">
			<div class="import_box">
				<i class="icon-group"></i>(从查询的结果列表中选择)
				<br/>
			    <input type="hidden" id="customerid" name="customerid" value=""/>
			    <input type="hidden" id="customerFlag" name="customerFlag" value=""/>
			    <input class="inp_text" type="text" autocomplete="off" id="seachCustom" name="user.customer.name" value="" style="width: 240px" />请输入客户公司代码或名称。
			    <ul id="seachCustomList"></ul>
				<br/>
			    <input type="hidden" id="contactsid" name="contactsid" value=""/>
			    <input class="inp_text" type="text" autocomplete="off" id="seachContacts" name="contactsname" value="" style="width: 240px" />请输入联系人名称
			    <ul id="seachContactsList"></ul>
			</div>
		</c:if>
		<div class="import_box" style="line-height: 16px;">
			<i class="icon-upload-alt"></i>上传您的excel模板，系统将根据您导入的信息生成订单。
			<br />
			<a href="${ctx}/order/downLoad" class="down_excel">下载excel模板文件…</a>
			<br /><br />
			<div class="file_box">
				<input name="file" type="file" id="upload" /> 
			</div>
		</div>
		<div class="import_box" style="padding-bottom: 50px;">
			<button class="btn-primary submit" type="button" onclick="uploadSubmit()">生成订单</button>
		</div>
	</div>
</div>
</form>
<script src="${ctx}/static/js/vagueSeachCustom.js" ></script>
<script src="${ctx}/static/js/vagueSeachContacts.js" ></script>
</body>
</html>