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
<form id="printOutBoundfm"  action="${ctx}/print/printOutBound" method="post" class="form-horizontal">
<div class="tools">
	<table width="100%">
		<tr>
			<td align="right">订单号:</td>
			<td>
			<input class="inp_text" type="text" id="seachOrder" name="orderNo" value="" style="width:80%"/>
			<ul id="seachOrderList"></ul>
			</td>
            <td align="right">客户名称:</td>
            <td><input class="inp_text" type="text" id="seachCustom" name="customerName" style="width:150px" />
			    <input class="inp_text" type="hidden" id="customercode" name="customercode" />
			    <ul id="seachCustomList"></ul>
            </td>
            <!-- <td align="right">日期:</td>
            <td>
                <input type="text" class="easyui-datebox" style="width: 150px;">
            </td> -->
            <td><button type="button" class="btn" onclick="printOutBoundQuery();">查询</button></td>
		</tr>
	</table>
    <div class="btn_group">
        <button id="makeBoard" type="button" class="btn btn-primary" disabled onclick="printOutBound()">打印出库单</button>
    </div>
</div>
<table id="productionData" class="easyui-datagrid" data-options="striped:true,method: 'get',pagination:true,fitColumns:true">
	<thead>
		<tr>
			<th data-options="field:'ck',checkbox:true"></th>
            <th data-options="field:'orderNo',width:80,sortable:true">订单号</th>
            <th data-options="field:'customerName',width:80,sortable:true">客户姓名</th>
			<th data-options="field:'productNoMinToMax',width:80,sortable:true">生产编号</th>
			<th data-options="field:'tbnTotal',width:80,sortable:true">碱基总数</th>
            <th data-options="field:'status',width:80,sortable:true">状态</th>
            <th data-options="field:'createTime',width:80,sortable:true">导入时间</th>
            <th data-options="field:'modifyTime',width:80,sortable:true">修改时间</th>
		</tr>
	</thead>
</table>
<div id="inputCause" class="easyui-dialog" data-options="closed:true"><textarea class="inp_text" style="width: 376px;height: 102px;"></textarea></div>
</form>
<script src="${ctx}/static/js/productionData.js" ></script>
<script src="${ctx}/static/js/createReport.js" ></script>
<script src="${ctx}/static/js/vagueSeachOrder.js" ></script>
<script src="${ctx}/static/js/vagueSeachCustom.js" ></script>
<script type="text/javascript">
$(function(){
    var dg = $('#productionData');
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
            //alert(pageNum+','+pageSize);
            //dg.datagrid('loadData',data);
            printOutBoundQuery();
        }
    });
})
</script>
</body>
</html>