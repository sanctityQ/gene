<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<script type="text/javascript">
function reportQuery(){
	if( form.orderNo.value == "" && form.customerCode.value == "" ){
		
		alert("请填写订单号或客户代码。");
		return false;
	}
	form.submit();
}
</script>
</head>

<body>
	<div class="container">
		<%@ include file="/static/layouts/header.jsp"%>
		<div id="content" class="span12">
	<form id="form" action="${ctx}/print/printReportQuery/" method="post" class="">
    <table>
    	<tr>
    		<td>
    			订单号：<input type="text" name="orderNo"/>
    		</td>
    		<td>
    			客户代码：<input type="text" name="customerCode"/>
    		</td>
    		<td>
        			<input id="query" class="btn btn-primary" type="button" value="查询" onclick="reportQuery()"/>
    		</td>
    	</tr>
    </table>	
		<fieldset>
			</div>
		</fieldset>
	</form>
</body>
</html>