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
<form id="deliveryLabelfm"  name="form"  action="${ctx}/delivery/deliveryLabelPrint" method="post" class="form-horizontal">
<input type="hidden" id="btnFlag" name="btnFlag" value="DeliveryLabel"/>
<input type="hidden" id="customerFlag" name="customerFlag" value="<%=customerFlag %>"/>
<div class="page_padding">
	<div class="content_box">
		<h2>发货标签</h2>
		<div class="import_box">
			<i class="icon-pencil"></i>请输入需导出的板号或生产编号，点击“发货标签”按钮下载文件。
			<br />
			<input class="inp_text" type="text" id="boardNo" value="" style="width: 300px" />
		</div>

		<div class="import_box" style="padding-bottom: 50px;">
			<button class="btn-primary submit" type="button" onclick="deliveryLabelPrint();">发货标签</button>
		</div>
	</div>
	<%@ include file="/views/synthesis/initBoardNo.jsp"%> <!--  未处理板号初始化页面 -->
</div>
<script src="${ctx}/views/delivery/js/deliveryLabel.js" ></script>
</form>
</body>
</html>