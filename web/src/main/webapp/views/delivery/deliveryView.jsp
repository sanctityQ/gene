<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="orderNo" value="${param.orderNo}" />
<c:set var="orderNo_con" value="${param.orderNo_con}" />
<c:set var="customerCode_con" value="${param.customerCode_con}" />
<c:set var="customerFlag_con" value="${param.customerFlag_con}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<script type="text/javascript">
var orderNo = ${orderNo};
var ctx = '${ctx}';
</script>
<%
String customerName_con = request.getParameter("customerName_con");
if(customerName_con==null || "null".equals(customerName_con)){
	customerName_con = "";
} else {
	customerName_con = new String(request.getParameter("customerName_con").getBytes("iso-8859-1"),"UTF-8");
}
%>
</head>
<body>
<!-- 页面的查询条件 -->
<input type="hidden" id="orderNo_con" name="orderNo_con" value="${orderNo_con}"/>
<input type="hidden" id="customerCode_con" name="customerCode_con" value="${customerCode_con}"/>
<input type="hidden" id="customerName_con" name="customerName_con" value="<%=customerName_con%>"/>
<input type="hidden" id="customerFlag_con" name="customerFlag_con" value="${customerFlag_con}"/>
<div class="page_padding">
	<div class="content_box totle margin_btoom">
		<b>订单号：</b>${orderNo}<br />
		<div id="createTime"></div>
		<div id="totalValue"></div>
	</div>
	<div class="content_box info margin_btoom">
		<h2>客户信息</h2>
		<table width="100%" class="order_info">
			<tr>
				<td colspan="4" height="10"></td>
			</tr>
			<tr>
				<td align="right">客户编号:</td>
				<td><input id="code" name="customer.code" class="readonly_inp" type="text"  style="width: 80%" disabled/></td>
				<td align="right">客户姓名:</td>
				<td><input id="name" name="customer.name" class="readonly_inp" type="text" style="width: 80%" disabled/></td>
			</tr>
			<tr>
				<td align="right">负责人姓名:</td>
				<td><input id="leaderName" name="customer.leaderName" class="readonly_inp" type="text"  style="width: 80%" disabled/></td>
				<td align="right">客户单位:</td>
				<td><input id="customerUnit" class="readonly_inp" type="text" value="" style="width: 80%" disabled/></td>
			</tr>
			<tr>
				<td align="right">发票抬头:</td>
				<td><input id="invoiceTitle" name="customer.invoiceTitle" class="readonly_inp" type="text"  style="width: 80%" disabled/></td>
				<td align="right">结账方式:</td>
				<td><input id="payWays" name="customer.payWays" class="readonly_inp" type="text" style="width: 80%" disabled/></td>
			</tr>
			<tr>
				<td align="right">客户地址:</td>
				<td colspan="3"><input id="address" name="customer.address" class="readonly_inp" type="text"  style="width: 50%" disabled/></td>
			</tr>
			<tr>
				<td align="right">联系电话:</td>
				<td><input id="phoneNo" name="customer.phoneNo" class="readonly_inp" type="text"  style="width: 80%" disabled/></td>
				<td align="right">Email:</td>
				<td><input id="email" name="customer.email" class="readonly_inp" type="text"  style="width: 80%" disabled/></td>
			</tr>
			<tr>
				<td align="right">网址:</td>
				<td colspan="3"><input id="webSite" class="readonly_inp" type="text" value="" style="width: 50%" disabled/></td>
			</tr>
			<tr>
				<td colspan="4" height="10"></td>
			</tr>
		</table>
	</div>
	<div class="content_box info margin_btoom">
		<h2>生产数据</h2>
		<table id="bigToSmall" class="easyui-datagrid" data-options="fitColumns:true,striped:true,method: 'post'">
		<thead>
			<tr>
			    <th data-options="field:'ck',checkbox:true"></th>
			    <th data-options="field:'id',width:80,hidden:true,sortable:true,editor:'text'">生产id</th>
				<th data-options="field:'productNo',width:80,sortable:true,editor:'text'">生产编号</th>
				<th data-options="field:'primeName',width:80,sortable:true,editor:'text'">引物名称</th>
				<th data-options="field:'geneOrder',width:80,sortable:true,editor:'text'">序列</th>
				<th data-options="field:'tbn',width:80,sortable:true,styler:cellStyler">碱基数</th>
				<th data-options="field:'nmolTotal',width:80,sortable:true,editor:'text'">nmol总量</th>
				<th data-options="field:'nmolTB',width:80,sortable:true,editor:'text'">nmol/tube</th>
				<th data-options="field:'odTotal',width:80,sortable:true,editor:'text'">OD总量</th>
				<th data-options="field:'odTB',width:80,sortable:true,editor:'text'">OD/tube</th>
				<th data-options="field:'purifyType',width:80,sortable:true,editor:'text'">纯化方式</th>
				<th data-options="field:'operationTypeDesc',width:80,sortable:true,editor:'text'">状态</th>
				<th data-options="field:'boardNo',width:80,sortable:true,editor:'text'">板号</th>
				<th data-options="field:'modiPrice',width:80,sortable:true,editor:'text'">修饰价格</th>
				<th data-options="field:'baseVal',width:80,sortable:true,editor:'text'">碱基单价</th>
				<th data-options="field:'purifyVal',width:80,sortable:true,editor:'text'">纯化价格</th>
				<th data-options="field:'totalVal',width:80,sortable:true,editor:'text'">总价格</th>
				<th data-options="field:'fromProductNo',width:80,hidden:true,sortable:true,editor:'text'">来源生产编号</th>
			</tr>
		</thead>
	</table>
	</div>
	<div class="tools_bar">
        <button type="" class="btn" onclick="window.history.back();">取 消</button>
		<button type="" class="btn btn-primary" onclick="saveDelivery();">发 货</button>
	</div>
</div>
<script src="${ctx}/views/delivery/js/delivery.js" ></script>
<script type="text/javascript">
orderDetail(${orderNo});
</script>
</body>
</html>