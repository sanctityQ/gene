<%@ page contentType="text/html;charset=UTF-8"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
<link href="${ctx}/static/css/easyui.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/icon.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/perfect-scrollbar.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/master.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/font-awesome.min.css" />
<!--[if IE 7]>
  <link rel="stylesheet" href="css/font-awesome-ie7.min.css">
<![endif]-->
<script src="${ctx}/static/js/jquery.min.js" ></script>
<script src="${ctx}/static/js/jquery.easyui.min.js" ></script>
<script src="${ctx}/static/js/perfect-scrollbar.min.js" ></script>
<script src="${ctx}/static/js/index.js" ></script>
</head>
<body>
<div class="tools">
	<table width="100%">
		<tr>
			<td align="right">订单号:</td>
			<td><input id="orderNo" class="inp_text" type="text" value="" style="width: 60%" /></td>
			<td>客户代码:</td>
			<td><input id="customerCode" class="inp_text" type="text" value="" style="width: 60%" /></td>
			<td><button type="button" class="btn">查询</button></td>
			<td align="right">
				<button type="button" class="btn btn-primary" onclick="goToPage('addOrder.html')">增加订单</button>
				<button type="button" class="btn btn-primary submit" onclick="DeletdRows('orderList')">批量删除</button>
			</td>
		</tr>
	</table>
</div>
<table id="orderList" class="easyui-datagrid" data-options="striped:true,method: 'get',pagination:true,fitColumns:true">
	<thead>
		<tr>
			<th data-options="field:'ck',checkbox:true"></th>
			<th data-options="field:'itemid',width:80,sortable:true">订单号</th>
			<th data-options="field:'productid',width:80,sortable:true">客户姓名</th>
			<th data-options="field:'listprice',width:80,sortable:true">生产编号</th>
			<th data-options="field:'status',width:80,sortable:true">引物名称</th>
			<th data-options="field:'attr1',width:80,sortable:true">碱基总数</th>
			<th data-options="field:'unitcost',width:80,sortable:true">状态</th>
			<th data-options="field:'attr3',width:80,sortable:true">导入时间</th>
			<th data-options="field:'attr4',width:80,sortable:true">修改时间</th>
			<th data-options="field:'_operate',width:80,align:'center',formatter:formatOper">操作</th>
		</tr>
	</thead>
</table>
<script src="${ctx}/views/order/js/orderList.js" ></script>
<script type="text/javascript">

</script>
</body>
</html>