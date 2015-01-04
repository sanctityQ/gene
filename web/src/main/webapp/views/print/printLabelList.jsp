<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<script type="text/javascript">

	function exportPrintLabel(customerCode){
			fm.action= "${ctx}/print/exportLabel/"+customerCode;
	        fm.submit();
	}

</script>

</head>

<body topmargin="0" leftmargin="0" rightmargin="0" onload="">
  <form name="fm" action="${ctx}/print/exportLabel/" method="post" class="form-horizontal">
     <table border="1" cellpadding="3" cellspacing="1" width="100%" style="background-color: #b9d8f3;" id="contentTable" class="common">
     
<c:forEach items="${primerProducts}" var="primerProduct"  varStatus="status">
  <input type="hidden" name="primerProductList.primerProducts[${status.index}].id" value="${primerProduct.id}"/>
  <input type="hidden" name="primerProductList.primerProducts[${status.index}].productNo" value="${primerProduct.productNo}"/>
  <input type="hidden" name="primerProductList.primerProducts[${status.index}].outProductNo"    value="${primerProduct.outProductNo}"/>
  <input type="hidden" name="primerProductList.primerProducts[${status.index}].primeName"    value="${primerProduct.primeName}"/>
  <input type="hidden" name="primerProductList.primerProducts[${status.index}].order.id" value="${primerProduct.order.id}" />
  <input type="hidden" name="primerProductList.primerProducts[${status.index}].order.orderNo" value="${primerProduct.order.orderNo}" />
  <input type="hidden" name="primerProductList.primerProducts[${status.index}].order.customerCode" value="${primerProduct.order.customerCode}" />
  <input type="hidden" name="primerProductList.primerProducts[${status.index}].order.customerName" value="${primerProduct.order.customerName}" />
</c:forEach>

		<thead>
		  <tr><%@ include file="/static/layouts/header.jsp"%></tr>
		</thead>
		<tbody>
    		<tr bgcolor='#F4FAFF'>
			<c:forEach items="${orders}" var="order"  varStatus="status1">
                 <td><input class="btn btn-primary" type="button" value="${order.customerName}" onclick="exportPrintLabel(${order.customerCode})"/></td>
			</c:forEach>
			</tr>
		</tbody>

	  </table>
	  <br>
	</form>
</body>
</html>
