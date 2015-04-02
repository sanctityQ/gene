<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
<script type="text/javascript">
var ctx = '${ctx}';
</script>
</head>
<body>
<div class="page_padding">
	<div class="content_box info margin_btoom">
		<h2>客户信息</h2>
		<table width="100%" class="order_info">
			<tr>
				<td colspan="4" height="10"></td>
			</tr>
            <tr>
                <td align="right">客户编号:</td>
                <td><input class="readonly_inp" type="text" value="${customer.code}" disabled style="width:150px" /></td>
                <td align="right">结算方式:</td>
                <td colspan="3"><input class="readonly_inp" type="text" name="customer.payWays" value="${customer.payWays}" disabled style="width:150px" /></td>
            </tr>
			<tr>
				<td align="right">姓名:</td>
				<td><input class="readonly_inp" type="text" value="${customer.name}" disabled style="width:150px" /></td>
				<td align="right">业务员:</td>
                <td><input class="readonly_inp" disabled name="customer.handlerCode" type="text" value="${customer.handlerCode}"  style="width: 150px" /></td>
			</tr>
			<tr>
				<td align="right">负责人姓名:</td>
				<td><input class="readonly_inp" type="text" value="${customer.leaderName}" disabled style="width:150px" /></td>
				<td align="right">客户单位:</td>
				<td>
                    <input class="readonly_inp" type="text" value="${customer.unit}" disabled style="width:150px" />
                </td>
			</tr>
			<tr>
				
				<td align="right">生产编号开头:</td>
				<td>
                    <input class="readonly_inp" type="text" name="customer.prefix" value="${customer.prefix}" disabled style="width:150px" />
				</td>
				<td align="right">联系电话:</td>
				<td>
                    <input class="readonly_inp" type="text" value="${customer.phoneNo}" disabled style="width:150px" />
				</td>
			</tr>
			<tr>
                <td align="right">Email:</td>
                <td><input class="readonly_inp" type="text" value="${customer.email}" disabled style="width:150px" /></td>
				<td align="right">网址:</td>
				<td>
                    <input class="readonly_inp" type="text" name="customer.webSite" value="${customer.webSite}" disabled style="width:150px" />
				</td>
            </tr>
            <tr>
                <td align="right">发票抬头:</td>
				<td><input class="readonly_inp" type="text" value="${customer.invoiceTitle}" disabled style="width: 80%" /></td>
                <td align="right">传真:</td>
				<td>
                    <input class="readonly_inp" type="text" name="customer.fax" value="${customer.fax}" disabled style="width:150px" />
				</td>
            </tr>
			<tr>
				<td align="right">客户地址:</td>
				<td colspan="3">
                    <input class="readonly_inp" type="text" value="${customer.address}" disabled style="width: 50%" />
                </td>
			</tr>
            <tr>
                <td align="right">办事处:</td>
                <td colspan="3"><input class="readonly_inp" type="text" value="${customer.office}" disabled style="width: 50%" /></td>
            </tr>
            <%-- <tr>
				<td align="right">修饰价格:</td>
				<td><input class="readonly_inp" disabled type="text" name="customer.customerPrice.modifyPrice" value="${customer.customerPrice.modifyPrice}"  style="width:150px" /></td>
				<td align="right">碱基单价:</td>
                <td><input class="readonly_inp" disabled name="customer.customerPrice.baseVal" type="text" value="${customer.customerPrice.baseVal}"  style="width:150px" /></td>
				
			</tr>
			<tr>
				<td align="right">纯化价格:</td>
				<td><input class="readonly_inp" disabled type="text" name="customer.customerPrice.purifyVal" value="${customer.customerPrice.purifyVal}"  style="width:150px" /></td>
				<td align="right">&nbsp;</td>
                <td>&nbsp;</td>
				
			</tr> --%>
			<tr>
				<td colspan="3" height="10"></td>
			</tr>
		</table>
	</div>
</div>
</body>
</html>