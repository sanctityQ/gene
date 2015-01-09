<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
  <head>
    <title>订单修改</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <link href="resources/css/jquery-ui-themes.css" type="text/css" rel="stylesheet"/>
    <link href="resources/css/axure_rp_page.css" type="text/css" rel="stylesheet"/>
    <link href="../data/styles.css" type="text/css" rel="stylesheet"/>
    <link href="../files/upLoadModify/styles.css" type="text/css" rel="stylesheet"/>
    <script src="../resources/scripts/jquery-1.7.1.min.js"></script>
    <script src="../resources/scripts/jquery-ui-1.8.10.custom.min.js"></script>
    <script src="../resources/scripts/axure/axQuery.js"></script>
    <script src="../resources/scripts/axure/globals.js"></script>
    <script src="../resources/scripts/axutils.js"></script>
    <script src="../resources/scripts/axure/annotation.js"></script>
    <script src="../resources/scripts/axure/axQuery.std.js"></script>
    <script src="../resources/scripts/axure/doc.js"></script>
    <script src="../data/document.js"></script>
    <script src="../resources/scripts/messagecenter.js"></script>
    <script src="../resources/scripts/axure/events.js"></script>
    <script src="../resources/scripts/axure/action.js"></script>
    <script src="../resources/scripts/axure/expr.js"></script>
    <script src="../resources/scripts/axure/geometry.js"></script>
    <script src="../resources/scripts/axure/flyout.js"></script>
    <script src="../resources/scripts/axure/ie.js"></script>
    <script src="../resources/scripts/axure/model.js"></script>
    <script src="../resources/scripts/axure/repeater.js"></script>
    <script src="../resources/scripts/axure/sto.js"></script>
    <script src="../resources/scripts/axure/utils.temp.js"></script>
    <script src="../resources/scripts/axure/variables.js"></script>
    <script src="../resources/scripts/axure/drag.js"></script>
    <script src="../resources/scripts/axure/move.js"></script>
    <script src="../resources/scripts/axure/visibility.js"></script>
    <script src="../resources/scripts/axure/style.js"></script>
    <script src="../resources/scripts/axure/adaptive.js"></script>
    <script src="../resources/scripts/axure/tree.js"></script>
    <script src="../resources/scripts/axure/init.temp.js"></script>
    <script src="../files/upLoadModify/data.js"></script>
    <script src="../resources/scripts/axure/legacy.js"></script>
    <script src="../resources/scripts/axure/viewer.js"></script>

  </head>
  <body>
  <form id="inputForm" modelAttribute="user" action="${ctx}/order/save" method="post">
  <table border="1">
    <input type="hidden" id="customerID" name="customer.id" size="50" value="${customer.id}" />
  	<tr>
  		<td>客户编号:<input type="text" id="code" name="customer.code" size="50" value="${customer.code}" /></td>
  		<td>客户姓名：<input type="text" id="name" name="customer.name" size="50" value="${customer.name}" /></td>
  	</tr>
  	<tr>
  		<td>负责人姓名：<input type="text" id="leaderName" name="customer.leaderName" size="50" value="${customer.leaderName}" /></td>
  	</tr>
  	<tr>
  		<td>发票抬头：<input type="text" id="invoiceTitle" name="customer.invoiceTitle" size="50" value="${customer.invoiceTitle}" /></td>
  		<td>结账方式：<input type="text" id="payWays" name="customer.payWays" size="50" value="${customer.payWays}" /></td>
  	</tr>
  	<tr>
  		<td>客户地址：<input type="text" id="address" name="customer.address" size="50" value="${customer.address}" /></td>
  		<td></td>
  	</tr>
  	<tr>
  		<td>联系电话：<input type="text" id="phoneNo" name="customer.phoneNo" size="50" value="${customer.phoneNo}" /></td>
  		<td>Email：<input type="text" id="email" name="customer.email" size="50" value="${customer.email}" /></td>
  	</tr>
  	<tr>
  		<td> 办事处：${customer.address}</td>
  		<td></td>
  	</tr>
  </table>

	<c:forEach  var="primerProduct" items="${primerProducts}" varStatus="status">     
	      <p>
	      <!-- 拆分的数据不显示使用是否本公司人员标识判断 -->
	      <c:if test="${empty primerProduct.fromProductNo}">
	      	<span>&#149; 
	      	<c:if test="${not empty primerProduct.productNo}">
	      		<input type="text" name="order.primerProducts[${status.index}].productNo" size="50" value="${primerProduct.productNo}" />
	      	</c:if> 
	      	<c:if test="${empty primerProduct.productNo}">
	      		<input type="text" name="order.primerProducts[${status.index}].outProductNo" size="50" value="${primerProduct.outProductNo}" />
	      	</c:if>
            <input type="text" name="order.primerProducts[${status.index}].primeName" size="50" value="${primerProduct.primeName}" />
            <input type="text" name="order.primerProducts[${status.index}].geneOrder" size="50" value="${primerProduct.geneOrder}" />
            <input type="text" name="order.primerProducts[${status.index}].purifyType" size="50" value="${primerProduct.purifyType}" />
            <input type="text" name="order.primerProducts[${status.index}].modiFiveType" size="50" value="${primerProduct.modiFiveType}" />
            <input type="text" name="order.primerProducts[${status.index}].modiThreeType" size="50" value="${primerProduct.modiThreeType}" />
            <input type="text" name="order.primerProducts[${status.index}].modiMidType" size="50" value="${primerProduct.modiMidType}" />
            <input type="text" name="order.primerProducts[${status.index}].modiSpeType" size="50" value="${primerProduct.modiSpeType}" />
            <input type="text" name="order.primerProducts[${status.index}].modiPrice" size="50" value="${primerProduct.modiPrice}" />
            <input type="text" name="order.primerProducts[${status.index}].baseVal" size="50" value="${primerProduct.baseVal}" />
            <input type="text" name="order.primerProducts[${status.index}].purifyVal" size="50" value="${primerProduct.purifyVal}" />
            <input type="text" name="order.primerProducts[${status.index}].totalVal" size="50" value="${primerProduct.totalVal}" />
            <input type="text" name="order.primerProducts[${status.index}].remark" size="50" value="${primerProduct.remark}" />
            <input type="text" name="order.primerProducts[${status.index}].nmolTotal" size="50" value="${primerProduct.nmolTotal}" />
            <input type="text" name="order.primerProducts[${status.index}].nmolTB" size="50" value="${primerProduct.nmolTB}" />
            <input type="text" name="order.primerProducts[${status.index}].odTotal" size="50" value="${primerProduct.odTotal}" />
            <input type="text" name="order.primerProducts[${status.index}].odTB" size="50" value="${primerProduct.odTB}" />
            <a href="${ctx}/order/copy/${primerProduct.productNo}" >复制</a>
</c:if>
	      	</span>
	      </p>   
	</c:forEach>
	
	<table border="1">
  	<tr>
  		<td>订单号:<input type="text" id="orderNo" name="order.orderNo" size="50" value="${order.orderNo}" /></td>
  		<td>客户代码：<input type="text" id="customerCode" name="order.customerCode" size="50" value="${order.customerCode}" /></td>
  	</tr>
  	<tr>
  		<td>姓名：<input type="text" id="customerName" name="order.customerName" size="50" value="${order.customerName}" /></td>
  		<td>归属机构：<input type="text" id="comCode" name="order.comCode" size="50" value="${order.comCode}" /></td>
  	</tr>
  	<tr>
  		<td>状态：<input type="text" id="status" name="order.status" size="50" value="${order.status}" /></td>
  		<td>类型：<input type="text" id="type" name="order.type" size="50" value="${order.type}" /></td>
  	</tr>
  	<tr>
  		<td>文件名称：<input type="text" id="fileName" name="order.fileName" size="50" value="${order.fileName}" /></td>
  		<td></td>
  	</tr>
  	<tr>
  	<td>
  	创建时间：<input type="text" id="createTime" name="order.createTime" size="50" value="<fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />" /></td>
  		<td>是否有效：<input type="text" id="validate" name="order.validate" size="50" value="${order.validate}" /></td>
  	</tr>
  </table>
	
	
	<input type="submit" style="cursor: pointer;"/>
</form>
  </body>
</html>
