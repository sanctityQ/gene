<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>失败原因</title>
<script type="text/javascript">
  function submitReason(){
	  var reason = document.getElementById("reason").value;
	  var productNo = window.opener.document.getElementById("failReason").value;
	  
	  if( reason.length ==0){
		  alert("请输入失败原因。");
		  return false;
	  }
	  
	  window.opener.document.getElementById("failReason").value = reason ;
	  window.opener.setValue("2");
	  window.close();
  }
</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/synthesis/synthesisQuery/" method="post" class="form-horizontal">
		<table style="width:90%">
		  <tr>
		     <td colspan="2">请输入合成失败原因：  </td>
		     <td colspan="2">  </td>
		  </tr>
		  <tr>
		     <td colspan="4"><textarea style="overflow:auto;width:90%" rows="10" value="" id="reason"></textarea>  </td>
		  </tr>
		  <tr>
		      <td colspan="2"></td>
		     <td><input class="btn btn-primary" type="button" value="取消" onclick="window.close()"/></td>
		     <td><input class="btn btn-primary" type="button" value="确定" onclick="submitReason()"/></td>
		  </tr>
		</table>
	</form>
</body>
</html>
