<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%> 
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
    </form>
  </body>
</html>
