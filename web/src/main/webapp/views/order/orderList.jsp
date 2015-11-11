<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
<script type="text/javascript">
var ctx = '${ctx}';
var userExp = '${userExp}';
if(userExp!=''){ alert(userExp)}
</script>
</head>
<body>
<form action="/gene/order/primerProductInfo" id='queryForm' method="post">
<input type="hidden" id="orderStatus" value="${orderStatus}"/>

<div class="tools">
	<table width="100%">
		<tr>
			<td align="right">订单号:</td>
			<td>
			<input class="inp_text" type="text" id="seachOrder" name="orderNo" value="" style="width:120px"/>
			<ul id="seachOrderList"></ul>
			</td>
			<c:if test="${customerFlag=='0'}">
				<td>客户公司名称:</td>
				<td><input class="inp_text" type="text" id="seachCustom" name="customerName" style="width:150px" />
				    <input class="inp_text" type="hidden" id="customerCode" name="customerCode" />
				    <ul id="seachCustomList"></ul>
				</td>
			</c:if>
		    <td align="right">订单日期:</td>
            <td>
                <input type="text" class="easyui-datebox" id="createStartTime" required="required" style="width: 90px;">
                -
                <input type="text" class="easyui-datebox" id="createEndTime" required="required" style="width: 90px;">
            </td>
            <c:if test="${orderStatus!='1'}">
	            <td>生产编号:</td>
				<td><input class="inp_text" type="text" id="productNo" name="productNo" style="width:120px" /></td>
            </c:if>
			<td><button type="button" class="btn" onclick="getOrderInfo();">查询</button></td>
			<td align="right">
			</td>
		</tr>
	</table>
</div>
</form>
<div id="orderDiv">
	<table id="orderList" class="easyui-datagrid" data-options="striped:true,method: 'get',pagination:true,fitColumns:true,sortOrder:'desc',remoteSort:false">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'orderNo',width:70,sortable:true">订单号</th>
				<th data-options="field:'outOrderNo',width:70,sortable:true">外部订单号</th>
				<th data-options="field:'customerName',width:80,sortable:true">客户公司名称</th>
				<th data-options="field:'contactsName',width:50,sortable:true">客户联系人</th>
				<th data-options="field:'productNoMinToMax',width:90,sortable:true">生产编号</th>
				<th data-options="field:'tbnTotal',width:40,sortable:true">碱基总数</th>
				<th data-options="field:'status',width:50,sortable:true">状态</th>
				<th data-options="field:'createTime',width:80,sortable:true">导入时间</th>
				<th data-options="field:'modifyTime',width:80,sortable:true">修改时间</th>
				<th data-options="field:'_operate',width:80,align:'center',formatter:formatOper">操作</th>
			</tr>
		</thead>
	</table>
</div>
<div id="inputCause" class="easyui-dialog" data-options="closed:true"><textarea class="inp_text" style="width: 376px;height: 102px;"></textarea></div>
<script src="${ctx}/views/order/js/orderList.js" ></script>
<script src="${ctx}/static/js/vagueSeachOrder.js" ></script>
<script src="${ctx}/static/js/vagueSeachCustom.js" ></script>
<script type="text/javascript">

$(function(){
    var dg = $('#orderList');
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        pageSize:20,
        onSelectPage:function(pageNum, pageSize){
            opts.pageNumber = pageNum;
            opts.pageSize = pageSize;

            pager.pagination('refresh',{
                pageNumber:pageNum,
                pageSize:pageSize
            });
            getOrderInfo();
        }
    });
})
 var orderNo = '${param.orderNo}';
 if(orderNo!=''){
 	$("#seachOrder").val(orderNo);
 	orderInfoIni();
 }
</script>
</body>
</html>