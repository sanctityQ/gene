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
				    <label for="loginName" class="control-label">公司：</label>
				    <select name="comcode" class="required" >
        					<option value="">睿博</option>
        					<option value="T">擎科</option>
        					<option value="J">金唯智</option>
        					<option value="X">梓熙</option>
        					<option value="A">A普通</option>
        					<option value="M">美吉</option>
        					<option value="D">华大</option>
        					<option value="N">华诺</option>
        			</select>
        			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        			<input id="submit" class="btn btn-primary" type="submit" value="查询" />
				</div>				
			</div>
		</fieldset>
	</form>
</body>
</html>