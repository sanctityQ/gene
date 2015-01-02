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

</script>
</head>

<body>
	<div class="container">
		<%@ include file="/static/layouts/header.jsp"%>
		<div id="content" class="span12">
	<form id="inputForm"
	action="${ctx}/print/printLabelQuery/" method="post" class="form-horizontal">
		<fieldset>
			<legend><small>打印标签查询</small></legend>
			<div class="control-group">
				<div class="controls">
     				<label for="loginName" class="control-label">订单号:</label>
					<input type="text" id="orderNo" name="orderNo" size="20" value="" class="required"/>
				    <label for="loginName" class="control-label">客户代码:</label>
					<input type="text" id="customer_code" name="customer_code" size="20" value="" class="required"/>
				</div>
				<div class="controls">
     				<label for="loginName" class="control-label">生产数据:</label>
					<input type="text" id="productNo" name="productNo" size="20" value="" class="required"/>
				    <label for="loginName" class="control-label">订单导入日期</label>从
					<input type="text" id="create_time_start" name="create_time_start" size="10" value="" class="required"/>到
					<input type="text" id="create_time_end" name="create_time_end" size="10" value="" class="required"/>
				</div>				
				<div class="controls">
        			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        			<input id="submit" class="btn btn-primary" type="submit" value="查询" />
				</div>				
			</div>
		</fieldset>
	</form>
</body>
</html>