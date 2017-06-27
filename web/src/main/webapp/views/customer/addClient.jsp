<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser"%>
<%@page import="org.apache.shiro.SecurityUtils"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
<script type="text/javascript">
var ctx = '${ctx}';
</script>
<%
ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
String customerFlag = user.getUser().getCustomer().getCustomerFlag();
String comCode = user.getUser().getCompany().getComCode();
//System.out.println("============"+user.getUser().getCompany().getComCode());

%>
</head>
<body>
<form id="customerfm" action="${ctx}/customer/save" method="post">
<div class="page_padding">
	<div class="easyui-tabs">
		<div title="客户信息" class="content_box info margin_btoom">
			<h2>客户信息</h2>

			<table width="100%" class="order_info">
				<tr>
					<td colspan="4" height="10"></td>
				</tr>
				<tr>
					<td align="right">客户编号:</td>
					<td><input class="inp_text" type="text" id="customercode" name="customer.code" value="${customer.code}"  style="width:150px" /></td>
					<td align="right">客户公司:</td>
					<td colspan="3"><input class="inp_text" type="text" id="customername" name="customer.name" value="${customer.name}"  style="width:200px" /></td>
				</tr>
				<tr>
					<td align="right">客户性质:</td>
					<td>
						<select id="customerFlag" name="customer.customerFlag" value="${customer.customerFlag}" class="my_select" style="width: 100px;" >
							<option value="">请选择</option>
							<option value="1">代理公司</option>
							<option value="2">直接客户</option>
							 <% //总公司才能选择和维护梓熙
								 if(comCode.startsWith("0")){%>
								<option value="0">梓熙</option>
								<% }%>
						</select>
					</td>
					<td align="right">联系人姓名:</td>
					<td><input class="inp_text" type="text" name="customer.leaderName" value="${customer.leaderName}"  style="width:150px" /></td>
				</tr>
				 <tr>
					<td align="right">业务员姓名:</td>
					<td><input class="inp_text" type="text" autocomplete="off" id="seachUserName" name="customer.handlerName" value="${customer.handlerName}" style="width: 150px" />
					<input type="hidden" id="vagueUserCode" name="customer.handlerCode" value="${customer.handlerCode}"/>
					<ul id="seachUserList"></ul>
				</tr>
				<tr>
					 <td align="right">结算方式:</td>
					<td ><input class="inp_text" type="text" name="customer.payWays" value="${customer.payWays}"  style="width:150px" /></td>
					<td align="right">发票抬头:</td>
					<td><input class="inp_text" type="text" name="customer.invoiceTitle" value="${customer.invoiceTitle}"  style="width: 80%" /></td>

				</tr>
				<tr>
					<td align="right" title="例：X501120004为生产编号，录入生产编号开头标识X开头。">生产编号开头:</td>
					<td>
						<input class="inp_text" type="text" id="customerPrefix" name="customer.prefix" value="${customer.prefix}"  style="width:50px" />
					</td>
					<td align="right">联系电话:</td>
					<td>
						<input class="inp_text" type="text" name="customer.phoneNo" value="${customer.phoneNo}"  style="width:150px" />
					</td>
				</tr>
				<tr>
					<td align="right">&nbsp;</td>
					<td align="right">&nbsp;</td>
					<td align="right">网址:</td>
					<td>
						<input class="inp_text" type="text" name="customer.webSite" value="${customer.webSite}"  style="width:150px" />
					</td>
				</tr>
				<tr>
					<td align="right">办事处:</td>
					<td ><input class="inp_text" name="customer.office" type="text" value="${customer.office}"  style="width: 50%" /></td>
					<td align="right">传真:</td>
					<td>
						<input class="inp_text" type="text" name="customer.fax" value="${customer.fax}"  style="width:150px" />
					</td>
				</tr>
				<tr>
					<td align="right">客户地址:</td>
					<td>
						<input class="inp_text" type="text" name="customer.address" value="${customer.address}"  style="width: 80%" />
					</td>
					<td align="right">Email:</td>
					<td><input class="inp_text" name="customer.email" type="text" value="${customer.email}"  style="width:150px" /></td>
				</tr>

				<tr>
					<td colspan="3" height="10"></td>
				</tr>
				<input type="hidden" name="customer.createTime" value="<fmt:formatDate value="${customer.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />"/>
				<input type="hidden" id="customerid" name="customer.id" value="${customer.id}"/>
				<input type="hidden" id="haveZiXi" value="${haveZiXi}"/>
			</table>
		</div>
		<div title="联系人信息" class="content_box info margin_btoom">
			 <h2>联系人信息<td>&nbsp;</td><td><button id="addID" type="button" class="btn btn-primary" onclick="addLinker();">增加</button></td></h2>
			 <table id="tableLinker" width="100%" align="center" class="order_info" style="BORDER-COLLAPSE: collapse" borderColor=#000000 border="0">
				 <tr>
					 <td align="center" width="15%">姓名</td>
					 <td align="center" width="20%">联系电话</td>
					 <td align="center" width="20%">电子邮箱</td>
					 <td align="center" width="8%">操作</td>
					 <input type="hidden" id="maxLinkerIndex" name="maxLinkerIndex" value="${customer.customerContactss.size()}">
				 </tr>
				 <tbody id="tbodyLinker">
				   <c:forEach var="cc" items="${customer.customerContactss}" varStatus="status">
					   <tr>
						 <td>
						 <input type="hidden" name="customer.customerContactss[${status.index}].id" value="${cc.id}">
						 <input class="inp_text" type="text" name="customer.customerContactss[${status.index}].name" value="${cc.name}" style="width: 90%">
						 </td>
						 <td><input class="inp_text" type="text" name="customer.customerContactss[${status.index}].phoneNo" value="${cc.phoneNo}" style="width: 90%"></td>
						 <td><input class="inp_text" type="text" name="customer.customerContactss[${status.index}].email" value="${cc.email}"  style="width: 90%"></td>
						 <td align="center"><button type="button" class="btn" onclick="delRow(this);">删除</button></td>
					  </tr>
				   </c:forEach>
				 </tbody>
			 </table>
		</div>
		<div title="价格信息" class="content_box info margin_btoom">
			 <h2>价格信息<td>&nbsp;</td><td><button id="addPriceBN" type="button" class="btn btn-primary" onclick="addPrice('0.0','0.0','0.0','0.0','0.0','0.0','0.0');">增加</button></td>
			 <a href="javascript:;" class="right btn-primary submit" onclick="iniPrice()" type="button">初始化数据</a>
			 </h2>
			 <table id="tablePrice" width="100%" align="center" class="order_info" style="BORDER-COLLAPSE: collapse" borderColor=#000000 border="0">
				 <tr>
					 <td align="center" width="8%">最小碱基数</td>
					 <td align="center" width="8%">最大碱基数</td>
					 <td align="center" width="8%">最小OD数</td>
					 <td align="center" width="8%">最大OD数</td>
					 <td align="center" width="8%">纯化方式</td>
					 <td align="center" width="8%">单价/元</td>
					 <td align="center" width="8%">纯化费</td>
					 <td align="center" width="8%">操作</td>
					 <input type="hidden" id="maxPriceIndex" name="maxPriceIndex" value="${customer.customerPrices.size()}">
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
						 <td>
							<select name="customer.customerPrices[${status.index}].purifyType" value="${cp.purifyType}" class="my_select" style="width:  90%" >
								<option value="OPC" <c:if test="${cp.purifyType=='OPC'}">selected</c:if> >OPC</option>
								<option value="PAGE" <c:if test="${cp.purifyType=='PAGE'}">selected</c:if> >PAGE</option>
								<option value="page" <c:if test="${cp.purifyType=='page'}">selected</c:if> >page</option>
								<option value="HPLC" <c:if test="${cp.purifyType=='HPLC'}">selected</c:if> >HPLC</option>
							</select>
						 </td>
						 <td><input class="inp_text" type="text" name="customer.customerPrices[${status.index}].baseVal" value="${cp.baseVal}"  style="width: 90%"></td>
						 <td><input class="inp_text" type="text" name="customer.customerPrices[${status.index}].purifyVal" value="${cp.purifyVal}"  style="width: 90%">
							 <input type="hidden" name="customerPriceFlag">
						 </td>

						 <td align="center"><button type="button" class="btn" onclick="delRow(this);">删除</button></td>
					  </tr>
				   </c:forEach>
				 </tbody>
			 </table>
		</div>
	</div>
	<div class="tools_bar">
		<button type="button" class="btn" onclick="goToPage('${ctx}/customer/clientManage');">取 消</button>
		<button type="button" class="btn btn-primary" onclick="customerSave();">保 存</button>
	</div>
</div>
</form>
<script src="${ctx}/static/js/vagueSeachUser.js" ></script>
<script src="${ctx}/views/customer/js/customer.js" ></script>
<script type="text/javascript">
$(document).ready(function(){
    var customerFlag = "${customer.customerFlag}";
    if(customerFlag!=""){
   	 $("#customerFlag").val(customerFlag);
    }
})
</script>                                      
</body>
</html>