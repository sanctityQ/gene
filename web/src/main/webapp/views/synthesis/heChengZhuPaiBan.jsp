<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<%@page import="org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<script src="${ctx}/static/js/json2.js"></script>
<%
ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
String customerFlag = user.getUser().getCustomer().getCustomerFlag();
%>
</head>
<body>
<form name="form" action="${ctx}/synthesis/exportHeChengZhuPaiBan/" method="post">
<input type="hidden" id="btnFlag" name="btnFlag" value="HeChengZhuPaiBan"/>
<input type="hidden" id="customerFlag" name="customerFlag" value="<%=customerFlag %>"/>
	<div class="page_padding">
		<div class="content_box">
			<h2>导出合成柱排版</h2>
			<div class="import_box">
				<i class="icon-inbox"></i>请选择板号后导出相应合成柱排版。
				<br />
				<input class="inp_text" autocomplete="off" id="boardNo" type="text" name="boardNo" value="" style="width: 300px" />
				<ul id="seachBoardList"></ul>
			</div>
	
			<div class="import_box" style="padding-bottom: 50px;">
				<button class="btn-primary submit" type="button" onclick="exportFile()">导出合成柱排版</button>
			</div>
		</div>
	</div>
	<%@ include file="/views/synthesis/initBoardNo.jsp"%> <!--  未处理板号初始化页面 -->
</form>
<script src="${ctx}/static/js/vagueSeachBoard.js"></script>
<script type="text/javascript">
function exportFile(){
	
	var boardNo = $.trim($('#boardNo').val());
	
	if(boardNo == ""){
		alert("请输入板号。");
		return;
	}
	
	document.form.submit();

}
</script>
</body>
</html>