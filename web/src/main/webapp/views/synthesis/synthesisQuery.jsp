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
	<form id="inputForm" action="${ctx}/synthesis/synthesisQuery/" method="post" class="form-horizontal">
		<fieldset>
			<legend><small>录入合成查询</small></legend>
	
			<div class="control-group">
				<label for="loginName" class="control-label">板号:</label>
				<input type="text" id="boardNo" name="boardNo" size="50" value="" class="required"/>
				<input id="submit" class="btn btn-primary" type="submit" value="查询" />&nbsp;
			</div>
		</fieldset>
		
	</form>
	
</body>
</html>
