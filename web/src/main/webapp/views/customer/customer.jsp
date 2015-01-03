<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<body>
  <form id="customerfm" action="${ctx}/customer/save" method="post">
  <table border="1">
  	<tr>
  		<td>客户编号:<input type="text" name="customer.code" size="50" value="${customer.code}" /></td>
  		<td>客户姓名：<input type="text"  name="customer.name" size="50" value="${customer.name}" /></td>
  	</tr>
  	<tr>
  		<td>负责人姓名：<input type="text" name="customer.leaderName" size="50" value="${customer.leaderName}" /></td>
  		<td>客户单位：<input type="text" name="customer.comName" size="50" value="${customer.comName}" /></td>
  	</tr>
  	<tr>
  		<td>发票抬头：<input type="text" name="customer.invoiceTitle" size="50" value="${customer.invoiceTitle}" /></td>
  		<td>结账方式：<input type="text"  name="customer.payWays" size="50" value="${customer.payWays}" /></td>
  	</tr>
  	<tr>
  		<td>客户地址：<input type="text" name="customer.address" size="50" value="${customer.address}" /></td>
  		<td></td>
  	</tr>
  	<tr>
  		<td>联系电话：<input type="text" name="customer.phoneNo" size="50" value="${customer.phoneNo}" /></td>
  		<td>Email：<input type="text" name="customer.email" size="50" value="${customer.email}" /></td>
  	</tr>
  	<tr>
  		<td> 网址：<input type="text"  name="customer.webSite" size="50" value="${customer.webSite}" /></td>
  		<td> 公司logo上传：
  		<input type="file" id="file" name="file" style="cursor: pointer;"/>
  		</td>
  	</tr>
  </table>
	
	<input type="submit" style="cursor: pointer;"/>
</form>
  </body>
</html>
