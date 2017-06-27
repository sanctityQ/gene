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
	<div class="easyui-tabs">
		<div title="客户信息" class="content_box info margin_btoom">
			<table width="100%" class="order_info">
				<tr>
					<td colspan="4" height="10"></td>
				</tr>
				<tr>
					<td align="right">客户编号:</td>
					<td><input class="readonly_inp" type="text" value="${customer.code}" disabled style="width:150px" /></td>
					<td align="right">客户公司:</td>
					<td colspan="3"><input class="readonly_inp" type="text" value="${customer.name}" disabled style="width:200px" /></td>
				</tr>
				<tr>
					<td align="right">客户性质:</td>
					<td>
						<input class="readonly_inp" type="text" value="${customer.customerFlag}" disabled style="width:150px" />
					</td>
					<td align="right">联系人姓名:</td>
					<td><input class="readonly_inp" type="text" value="${customer.leaderName}" disabled style="width:150px" /></td>
				</tr>
				<tr>
					<td align="right">业务员代码:</td>
					<td><input class="readonly_inp" disabled name="customer.handlerCode" type="text" value="${customer.handlerCode}"  style="width: 150px" /></td>
					<td align="right">业务员姓名:</td>
					<td><input class="readonly_inp" disabled name="customer.handlerName" type="text" value="${customer.handlerName}"  style="width: 150px" /></td>
				</tr>
				<tr>
					<td align="right">结算方式:</td>
					<td><input class="readonly_inp" type="text" name="customer.payWays" value="${customer.payWays}" disabled style="width:150px" /></td>
					<td align="right">发票抬头:</td>
					<td><input class="readonly_inp" type="text" value="${customer.invoiceTitle}" disabled style="width: 80%" /></td>
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
					<td align="right">办事处:</td>
					<td ><input class="readonly_inp" type="text" value="${customer.office}" disabled style="width: 50%" /></td>
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
			</table>
		</div>
		<div title="联系人信息" class="content_box info margin_btoom">
			 <table id="tableLinker" width="100%" align="center" class="order_info" style="BORDER-COLLAPSE: collapse" borderColor=#000000 border="0">
				 <tr>
					 <td align="center" width="15%">姓名</td>
					 <td align="center" width="20%">联系电话</td>
					 <td align="center" width="20%">电子邮箱</td>
				 </tr>
				 <tbody id="tbodyLinker">
				   <c:forEach var="cc" items="${customer.customerContactss}" varStatus="status">
					   <tr>
						 <td><input class="inp_text" type="text" name="customer.customerContactss[${status.index}].name" value="${cc.name}" style="width: 90%"></td>
						 <td><input class="inp_text" type="text" name="customer.customerContactss[${status.index}].phoneNo" value="${cc.phoneNo}" style="width: 90%"></td>
						 <td><input class="inp_text" type="text" name="customer.customerContactss[${status.index}].email" value="${cc.email}"  style="width: 90%"></td>
					  </tr>
				   </c:forEach>
				 </tbody>
			 </table>
		</div>
		<div title="价格信息" class="content_box info margin_btoom">
         <table id="tablePrice" width="100%" align="center" class="order_info" style="BORDER-COLLAPSE: collapse" borderColor=#000000 border="0">
             <tr>
                 <td align="center" width="8%">最小碱基数</td>
                 <td align="center" width="8%">最大碱基数</td>
                 <td align="center" width="8%">最小OD数</td>
                 <td align="center" width="8%">最大OD数</td>
                 <td align="center" width="8%">纯化方式</td>
                 <td align="center" width="8%">单价/元</td>
                 <td align="center" width="8%">纯化费</td>
             </tr>
             <tbody id="tbodyPrice">
	           <c:forEach var="cp" items="${customer.customerPrices}" varStatus="status">
		           <tr>
	                 <td>
	                 <input type="hidden" name="customer.customerPrices[${status.index}].id" value="${cp.id}">
	                 <input class="inp_text" type="text" name="customer.customerPrices[${status.index}].minTbn" value="${cp.minTbn}" style="width: 90%">
	                 </td>
	                 <td><input class="inp_text" type="text" name="customer.customerPrices[${status.index}].maxTbn" value="${cp.maxTbn}" style="width: 90%"></td>
	                 <td><input class="inp_text" type="text" name="customer.customerPrices[${status.index}].minOd" value="${cp.minOd}"  style="width: 90%"></td>
	                 <td><input class="inp_text" type="text" name="customer.customerPrices[${status.index}].maxOd" value="${cp.maxOd}"  style="width: 90%"></td>
	                 <td><input class="inp_text" type="text" name="customer.customerPrices[${status.index}].purifyType" value="${cp.purifyType}"  style="width: 90%"></td>
	                 <td><input class="inp_text" type="text" name="customer.customerPrices[${status.index}].baseVal" value="${cp.baseVal}"  style="width: 90%"></td>
	                 <td><input class="inp_text" type="text" name="customer.customerPrices[${status.index}].purifyVal" value="${cp.purifyVal}"  style="width: 90%"></td>
				  </tr>
	           </c:forEach>
             </tbody>
         </table>
    </div>
	</div>
</div>
</body>
</html>