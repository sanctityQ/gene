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
	action="${ctx}/synthesis/makeTableQuery/" method="post" class="form-horizontal">
		<fieldset>
			<legend><small>安排合成查询</small></legend>
	
			<div class="control-group">
				<div class="controls">
				    <label for="loginName" class="control-label">客户代码:</label>
					<input type="text" id="customer_code" name="customer_code" size="50" value="1101" class="required"/>
				</div>
				<div class="controls">
     				<label for="loginName" class="control-label">碱基数范围:</label>
					<input type="text" id="tbn1" name="tbn1" size="50" value="1" class="required"/> - 
					<input type="text" id="tbn2" name="tbn2" size="50" value="10" class="required"/>
				</div>				
			</div>
			<div class="control-group">
				<div class="controls">
					<input type="checkbox" id="modiFlag" name="modiFlag" size="50" value="" class="required"/>
				    <label for="loginName" class="control-label">有/无修饰过滤</label>
		             &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				    <label for="loginName" class="control-label">纯化方式：</label>
				    <select name="purifytype" class="required" >
        					<option value="OPC">OPC</option>
        					<option value="PAGE">PAGE</option>
        					<option value="HPLC">HPLC</option>
        			</select>
				</div>				
			</div>
			
			<div class="form-actions">
				<input id="submit" class="btn btn-primary" type="submit" value="查询" />&nbsp;
				<input id="cancel" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
		</fieldset>
		
	</form>
	
</body>
</html>
