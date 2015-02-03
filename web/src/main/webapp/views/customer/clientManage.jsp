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
<script type="text/javascript">
var ctx = '${ctx}';
</script>
</head>
<body>
<div class="tools">
	<table width="100%">
		<tr>
			<td align="right">姓名:</td>
			<td>
			<input class="inp_text" type="text" id="seachCustom" name="customerName" value="" style="width:60%" />
			<input class="inp_text" id="customerCode" type="hidden" name="customerCode" />
			<ul id="seachCustomList"></ul>
			</td>
			<td align="right">单位:</td>
			<td><input class="inp_text" type="text" id="seachUnitName" name="unitName" style="width: 60%" />
			<ul id="seachUnitNameList"></ul>
			</td>
			<td><button type="button" class="btn" onclick="getCustomerList()">查询</button></td>
			<td align="right">
				<button type="button" class="btn btn-primary" onclick="goToPage('addClient.html')">添加客户</button>
				<!-- <button type="button" class="btn btn-primary submit" onclick="deleteRows('orderList')">批量删除</button> -->
			</td>
		</tr>
	</table>
</div>
<table id="customerList" class="easyui-datagrid" data-options="striped:true,method: 'get',pagination:true,fitColumns:true">
	<thead>
		<tr>
			<th data-options="field:'ck',checkbox:true"></th>
			<th data-options="field:'id',width:80,sortable:true">编号</th>
			<th data-options="field:'code',width:80,sortable:true">客户代码 </th>
			<th data-options="field:'name',width:80,sortable:true">姓名</th>
			<th data-options="field:'unit',width:80,sortable:true">单位</th>
			<th data-options="field:'phoneNo',width:80,sortable:true">联系电话</th>
			<th data-options="field:'email',width:80,sortable:true">Email</th>
			<!-- <th data-options="field:'unitcost',width:80,sortable:true">所属业务员</th> -->
            <th data-options="field:'payWays',width:80,sortable:true">结算方式</th>
			<th data-options="field:'_operate',align:'center',formatter:formatOper">操作</th>
		</tr>
	</thead>
</table>
<script src="${ctx}/views/customer/js/clientManage.js" ></script>
<script src="${ctx}/static/js/vagueSeachCustom.js" ></script>
<script src="${ctx}/static/js/vagueSeachUnit.js" ></script>
<script type="text/javascript">
$(function(){
    var dg = $('#customerList');
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        pageSize:10,
        onSelectPage:function(pageNum, pageSize){
            opts.pageNumber = pageNum;
            opts.pageSize = pageSize;

            pager.pagination('refresh',{
                pageNumber:pageNum,
                pageSize:pageSize
            });
            getCustomerList();
        }
    });
})
</script>
</body>
</html>