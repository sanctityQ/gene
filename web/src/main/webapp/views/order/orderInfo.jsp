<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="orderNo" value="${param.orderNo}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<link href="${ctx}/static/css/easyui.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/icon.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/perfect-scrollbar.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/master.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/font-awesome.min.css" type="text/css" rel="stylesheet"/>
<!--[if IE 7]>
  <link rel="stylesheet" href="css/font-awesome-ie7.min.css">
<![endif]-->
<script src="${ctx}/static/js/jquery.min.js" ></script>
<script src="${ctx}/static/js/jquery.easyui.min.js" ></script>
<script src="${ctx}/static/js/perfect-scrollbar.min.js" ></script>
<script src="${ctx}/static/js/index.js" ></script>
<script type="text/javascript">
var orderNo = ${orderNo};
</script>
</head>
<body>
<%-- <form id="inputForm" modelAttribute="user" action="${ctx}/order/save" method="post"> --%>
<div class="page_padding">
	<div class="content_box totle margin_btoom">
	<input type="hidden" id="orderNo" name="order.orderNo" size="50" value="${order.orderNo}" />
	<input type="hidden" id="createTime" name="order.createTime" size="50" value="<fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />" />
		<b>订单号：</b>${order.orderNo}<br />
		<b>订购日期：</b><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd" /><br />
		<b class="bule">订单总计：</b>￥ ${order.totalValue}
	</div>
	<div class="content_box info margin_btoom">
		<h2>客户信息</h2>
		<table width="100%" class="order_info">
			<tr>
				<td colspan="4" height="10"></td>
			</tr>
			<tr>
				<td align="right">客户编号:</td>
				<td><input id="code" name="customer.code" class="inp_text" type="text" value="${customer.code}" style="width: 80%" /></td>
				<td align="right">客户姓名:</td>
				<td><input id="name" name="customer.name" class="inp_text" type="text" value="${customer.name}" style="width: 80%" /></td>
			</tr>
			<tr>
				<td align="right">负责人姓名:</td>
				<td><input id="leaderName" name="customer.leaderName" class="inp_text" type="text" value="${customer.leaderName}" style="width: 80%" /></td>
				<td align="right">客户单位:</td>
				<td><input class="inp_text" type="text" value="" style="width: 80%" /></td>
			</tr>
			<tr>
				<td align="right">发票抬头:</td>
				<td><input id="invoiceTitle" name="customer.invoiceTitle" class="inp_text" type="text" value="${customer.invoiceTitle}" style="width: 80%" /></td>
				<td align="right">结账方式:</td>
				<td><input id="payWays" name="customer.payWays" class="inp_text" type="text" value="${customer.payWays}" style="width: 80%" /></td>
			</tr>
			<tr>
				<td align="right">客户地址:</td>
				<td colspan="3"><input id="address" name="customer.address" class="inp_text" type="text" value="${customer.address}" style="width: 50%" /></td>
			</tr>
			<tr>
				<td align="right">联系电话:</td>
				<td><input id="phoneNo" name="customer.phoneNo" class="inp_text" type="text" value="${customer.phoneNo}" style="width: 80%" /></td>
				<td align="right">Email:</td>
				<td><input id="email" name="customer.email" class="inp_text" type="text" value="${customer.email}" style="width: 80%" /></td>
			</tr>
			<tr>
				<td align="right">办事处:</td>
				<td colspan="3"><input class="inp_text" type="text" value="" style="width: 50%" /></td>
			</tr>
			<tr>
				<td colspan="4" height="10"></td>
			</tr>
		</table>
	</div>
	<div class="content_box info margin_btoom">
		<h2><a href="javascript:;" class="right btn-primary submit" onclick="appendRow()" type="button">添加数据</a>生产数据</h2>
		<table id="bigToSmall" class="easyui-datagrid" data-options="fitColumns:true,singleSelect: true,iconCls: 'icon-save',striped:true,method: 'get',onClickRow: onClickRow">
		<thead>
			<tr>
				<th data-options="field:'productNo',width:80,sortable:true,editor:'text'">生产编号</th>
				<th data-options="field:'primeName',width:80,sortable:true,editor:'text'">引物名称</th>
				<th data-options="field:'geneOrder',width:80,sortable:true,editor:'text'">序列</th>
				<th data-options="field:'tbn',width:80,sortable:true,editor:'text'">碱基数</th>
				<th data-options="field:'purifyType',width:80,sortable:true,editor:'text'">纯化方式</th>
				<th data-options="field:'modiPrice',width:80,sortable:true,editor:'text'">修饰价格</th>
				<th data-options="field:'baseVal',width:80,sortable:true,editor:'text'">碱基单价</th>
				<th data-options="field:'purifyVal',width:80,sortable:true,editor:'text'">纯化价格</th>
				<th data-options="field:'totalVal',width:80,sortable:true,editor:'text'">总价格</th>
				<th data-options="field:'_operate',width:80,align:'center',formatter:formatOper">操作</th>
			</tr>
		</thead>
		<%-- 
		<c:forEach  var="primerProduct" items="${primerProducts}" varStatus="status">     
	      <p>
	      <c:if test="${empty primerProduct.fromProductNo}">
	      	&#149; 
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
	      </p>   
	</c:forEach> --%>
	</table>
	</div>
	<div class="tools">
		<button type="button" class="btn" onclick="goToPage('orderImport.jsp');">取 消</button>
		<button type="submit" class="btn btn-primary">保 存</button>
	</div>
</div>
<script src="${ctx}/views/order/js/orderInfo.js" ></script>
</body>
</html>