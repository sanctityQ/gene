<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
<script type="text/javascript">
var orderNo = '${orderNo}';
var ctx = '${ctx}';
//暂时处理后台异常
var userExp = '${userExp}';
if(userExp!=''){ alert(userExp)}
</script>
<%
ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
String customerFlag = user.getUser().getCustomer().getCustomerFlag();
%>
</head>
<body>
<form id="ChunHuaBiaofm"  name="form"  action="${ctx}/statistics/exHplcChunHuaBiao" method="post" class="form-horizontal">
<input type="hidden" id="btnFlag" name="btnFlag" value="ChunHuaBiao"/>
<input type="hidden" id="customerFlag" name="customerFlag" value="<%=customerFlag %>"/>
<div class="page_padding">
	<div class="content_box">
		<h2>HPLC进度表</h2>
		<div class="import_box">
			<i class="icon-pencil"></i>请输入板号或生产编号进行查询。
			<br />
			<input class="inp_text" type="text" id="boardNoOrProductNo" value="" style="width: 300px" />
			<input type="hidden" id="noType" value=""/>
			<ul id="seachBoardList"></ul>
		</div>

		<div class="import_box" style="padding-bottom: 50px;">
			<button class="btn-primary submit" type="button" onclick="deliveryLabelPrint();">查询</button>
		</div>
	</div>
</div>
<script src="${ctx}/static/js/vagueSeachBoardOrproductNo.js"></script>
<script type="text/javascript">
var deliveryLabelPrint = function(){
	var boardNo = $.trim($('#boardNoOrProductNo').val());
	
	if(boardNo == ""){
		alert("请输入板号或生产编号。");
		return;
	}
	var url = $('#ChunHuaBiaofm').attr("action");
	var path = url+'?boardNo='+boardNo; 
	$('#ChunHuaBiaofm').attr("action", path).submit();
	$('#ChunHuaBiaofm').attr("action", url);
	
}
</script>
</form>
</body>
</html>