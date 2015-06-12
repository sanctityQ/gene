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
</script>
</head>
<body>
<form action="" id='queryForm'>
<div class="tools">
	<table width="100%">
		<tr>
			<td align="right">订单号:</td>
			<td>
			<input class="inp_text" type="text" id="seachOrder" name="orderNo" value="" style="width:80%"/>
			<ul id="seachOrderList"></ul>
			</td>
			<td>客户代码:</td>
			<td><input class="inp_text" type="text" id="seachCustom" name="customerName" style="width:150px" />
			    <input class="inp_text" type="hidden" id="customercode" name="customercode" />
			    <ul id="seachCustomList"></ul>
			</td>
		    <td align="right">订单日期:</td>
            <td>
                <input type="text" class="easyui-datebox" id="createStartTime" required="required" style="width: 90px;">
                -
                <input type="text" class="easyui-datebox" id="createEndTime" required="required" style="width: 90px;">
            </td>
			<td><button type="button" class="btn" onclick="getOrderInfo();">查询</button></td>
			<td align="right">
				<!-- <button type="button" class="btn btn-primary" onclick="goToPage('addOrder.html')">增加订单</button>
				<button type="button" class="btn btn-primary submit" onclick="DeletdRows('orderList')">批量删除</button> -->
			</td>
		</tr>
	</table>
</div>
</form>
<table id="orderList" class="easyui-datagrid" data-options="striped:true,method: 'get',pagination:true,fitColumns:true">
	<thead>
		<tr>
			<th data-options="field:'ck',checkbox:true"></th>
			<th data-options="field:'orderNo',width:80,sortable:true">订单号</th>
			<th data-options="field:'outOrderNo',width:80,sortable:true">外部订单号</th>
			<th data-options="field:'customerName',width:80,sortable:true">客户姓名</th>
			<th data-options="field:'productNoMinToMax',width:80,sortable:true">生产编号</th>
			<th data-options="field:'tbnTotal',width:40,sortable:true">碱基总数</th>
			<th data-options="field:'status',width:50,sortable:true">状态</th>
			<th data-options="field:'createTime',width:80,sortable:true">导入时间</th>
			<th data-options="field:'modifyTime',width:80,sortable:true">修改时间</th>
			<th data-options="field:'_operate',width:80,align:'center',formatter:formatOper">操作</th>
		</tr>
	</thead>
</table>
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