<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="orderNo" value="${order.orderNo}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<script type="text/javascript">
var orderNo = ${orderNo};
var ctx = '${ctx}';
//var reSultdata = ${reSultdata};
//var total = ${total};
</script>
</head>
<body>
<div class="page_padding">
	<div class="content_box totle margin_btoom">
		<b>订单号：</b>${order.orderNo}<br />
		<b>订购日期：</b><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /><br />
		<%-- <b class="bule">订单总计：</b>￥ ${order.totalValue}<br /> --%>
		<div id='totalValue'></div>
		<b>订单类型：</b>${order.orderUpType} 类型
	</div>
	<div class="content_box info margin_btoom">
		<h2>客户信息</h2>
		<table width="100%" class="order_info">
			<tr>
				<td colspan="4" height="10"></td>
			</tr>
			<tr>
				<td align="right">客户编号:</td>
				<td><input id="code" name="customer.code" class="readonly_inp" type="text"  style="width: 80%" value="${customer.code}" disabled/></td>
				<td align="right">客户姓名:</td>
				<td><input id="name" name="customer.name" class="readonly_inp" type="text" style="width: 80%" value="${customer.name}" disabled/></td>
			</tr>
			<tr>
				<td align="right">负责人姓名:</td>
				<td><input id="leaderName" name="customer.leaderName" class="readonly_inp" type="text"  style="width: 80%" value="${customer.leaderName}" disabled/></td>
				<td align="right">客户单位:</td>
				<td><input id="customerUnit" class="readonly_inp" type="text" style="width: 80%" value="${customer.invoiceTitle}" disabled/></td>
			</tr>
			<tr>
				<td align="right">发票抬头:</td>
				<td><input id="invoiceTitle" name="customer.invoiceTitle" class="readonly_inp" type="text"  style="width: 80%" value="${customer.invoiceTitle}" disabled/></td>
				<td align="right">结账方式:</td>
				<td><input id="payWays" name="customer.payWays" class="readonly_inp" type="text"  style="width: 80%" value="${customer.payWays}" disabled/></td>
			</tr>
			<tr>
				<td align="right">客户地址:</td>
				<td><input id="address" name="customer.address" class="readonly_inp" type="text"  style="width: 50%" value="${customer.address}" disabled/></td>
				<td align="right">传真:</td>
				<td><input id="fax" name="customer.fax" class="readonly_inp" type="text"  style="width: 50%" value="${customer.fax}" disabled/></td>
			</tr>
			<tr>
				<td align="right">联系电话:</td>
				<td><input id="phoneNo" name="customer.phoneNo" class="readonly_inp" type="text" style="width: 80%" value="${customer.phoneNo}" disabled/></td>
				<td align="right">Email:</td>
				<td><input id="email" name="customer.email" class="readonly_inp" type="text" style="width: 80%" value="${customer.email}" disabled/></td>
			</tr>
			<tr>
				<td align="right">网址:</td>
				<td><input id="webSite" class="readonly_inp" type="text" style="width: 50%" value="${customer.webSite}" disabled/></td>
				<td align="right">业务员:</td>
				<td><input id="handlerCode" class="readonly_inp" type="text" style="width: 80%" value="${customer.handlerCode}" disabled/></td>
			</tr>
			<tr>
				<td colspan="4" height="10"></td>
			</tr>
		</table>
	</div>
	<div class="content_box info margin_btoom">
		<h2><a href="javascript:;" class="right btn-primary submit" onclick="appendRow()" type="button">添加数据</a>生产数据</h2>
		<table id="bigToSmall" class="easyui-datagrid" data-options="fitColumns:true,singleSelect: true,striped:true,method: 'get',onClickRow: onClickRow">
		<thead>
			<tr>
				<th data-options="field:'productNo',width:80,sortable:true,editor:'text'">生产编号</th>
				<th data-options="field:'primeName',width:80,sortable:true,editor:'text'">引物名称</th>
				<th data-options="field:'geneOrderMidi',width:220,sortable:true,styler:cellStyler1,editor:'text'">序列</th>
				<th data-options="field:'geneOrder',width:80,hidden:true,sortable:true,editor:'text'">序列</th>
				<th data-options="field:'tbn',width:40,sortable:true,editor:'numberbox'">碱基数</th>
				<c:if test="${order.orderUpType=='nmol'}">
				<th data-options="field:'nmolTotal',width:40,sortable:true,min:0,precision:2,editor:{type:'numberbox',options:{precision:2}}">nmol总量</th>
				<th data-options="field:'nmolTB',width:40,sortable:true,editor:{type:'numberbox',options:{precision:2}}">nmol/tube</th>
				</c:if>
				<c:if test="${order.orderUpType=='od'}">
				<th data-options="field:'odTotal',width:40,sortable:true,editor:{type:'numberbox',options:{precision:2}}">OD总量</th>
				<th data-options="field:'odTB',width:40,sortable:true,editor:{type:'numberbox',options:{precision:2}}">OD/tube</th>
				</c:if>
				<th data-options="field:'purifyType',width:60,sortable:true,editor:{
							type:'combobox',
							options:{
							    panelHeight:100,
								 valueField: 'label',
                            textField: 'value',
							    data: [
							        {label: 'OPC',value: 'OPC'},
							        {label: 'PAGE',value: 'PAGE'},
							        {label: 'HPLC',value: 'HPLC'}
							    ]
							}}">纯化方式</th>
				<!-- <th data-options="field:'midi',width:200,sortable:true,styler:cellStyler">修饰</th> -->
				<th data-options="field:'modiFiveType',width:80,sortable:true,styler:cellStyler,editor:'text'">5修饰</th>
				<th data-options="field:'modiThreeType',width:80,sortable:true,styler:cellStyler,editor:'text'">3修饰</th>
				<th data-options="field:'modiMidType',width:80,sortable:true,styler:cellStyler,editor:'text'">中间修饰</th>
				<th data-options="field:'modiSpeType',width:80,sortable:true,styler:cellStyler,editor:'text'">特殊单体</th>
				<th data-options="field:'modiPrice',width:40,sortable:true">修饰价格</th>
				<th data-options="field:'baseVal',width:40,sortable:true">碱基单价</th>
				<th data-options="field:'purifyVal',width:40,sortable:true">纯化价格</th>
				<th data-options="field:'totalVal',width:80,sortable:true,editor:{type:'numberbox',options:{precision:2}}">总价格</th>
				<th data-options="field:'remark',width:80,sortable:true,editor:'text'">备注</th>
				<th data-options="field:'fromProductNo',width:80,hidden:true,sortable:true,editor:'text'">来源生产编号</th>
				<th data-options="field:'_operate',width:40,align:'center',formatter:formatOper">操作</th>
			</tr>
		</thead>
	</table>
	</div>
	<div class="tools_bar">
		<button type="" class="btn" onclick="goToPage('${ctx}/order/import');">取 消</button>
		<button type="" class="btn btn-primary" onclick="getChanesSave();">保 存</button>
	</div>
</div>
<script src="${ctx}/views/order/js/orderInfo.js" ></script>
<script type="text/javascript">
getProduct();
</script>
</body>
</html>