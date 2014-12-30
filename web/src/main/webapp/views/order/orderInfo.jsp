<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
  <head>
    <title>订单信息</title>
  </head>
  <body>
  <form id="orderInfoForm"  action="${ctx}/order/query" method="post">
    <table>
    	<tr>
    		<td>
    			订单号：<input type="text" name="orderNo"/>
    		</td>
    		<td>
    			客户代码：<input type="text" name="customerCode"/>
    		</td>
    	</tr>
    </table>
    <table>
    <thead></thead>
    <tbody>
    <c:forEach  var="order"  items="${page.content}" varStatus="status">
	      <p>
	      	<span>&#149; 
	      	<a href="${ctx}/order/modify/${order.orderNo}" >${order.orderNo}</a>
<input type="text"  name="orderInfos[${status.index}].orderNo" size="50" value="${order.orderNo}" />
<input type="text"  name="orderInfos[${status.index}].customerName" size="50" value="${order.customerName}" />
<input type="text"  name="orderInfos[${status.index}].status" size="50" value="${order.status}" />
<input type="text"  name="orderInfos[${status.index}].productNoMinToMax" size="50" value="${order.productNoMinToMax}" />
<input type="text"  name="orderInfos[${status.index}].tbnTotal" size="50" value="${order.tbnTotal}" />
<input type="text"  name="orderInfos[${status.index}].createTime" size="50" value="${order.createTime}" />
<input type="text"  name="orderInfos[${status.index}].modifyTime" size="50" value="${order.modifyTime}" />

	      	</span>
	      </p>   
	</c:forEach>
	</tbody>
	</table>	
    <input type="submit" style="cursor: pointer;"/>
    </form>
  </body>
</html>
