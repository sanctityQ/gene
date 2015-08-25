<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<script type="text/javascript">
 var ctx = '${ctx}';
</script>
</head>
<body>
<div class="tools">
	<table width="100%">
		<tr>
			<td align="right">订单号:</td>
			<td><input id="orderNo" class="inp_text" type="text" value="" style="width: 60%" /></td>
			<c:if test="${customerFlag=='0'}">
				<td align="right">客户公司代码:</td>
				<td><input id="customerCode" class="inp_text" type="text" value="" style="width: 60%" /></td>
			</c:if>
			<td><button type="button" class="btn" onclick="getExamineInfo()">查询</button></td>

		</tr>
	</table>
</div>
<table id="orderList" class="easyui-datagrid" data-options="striped:true,method: 'get',pagination:true,fitColumns:true">
	<thead>
		<tr>
			<th data-options="field:'ck',checkbox:true"></th>
			<th data-options="field:'orderNo',width:80,sortable:true">订单号</th>
			<th data-options="field:'outOrderNo',width:80,sortable:true">外部订单号</th>
			<th data-options="field:'customerName',width:80,sortable:true">客户公司名称</th>
			<th data-options="field:'contactsName',width:50,sortable:true">客户联系人</th>
			<th data-options="field:'productNoMinToMax',width:80,sortable:true">生产编号</th>
			<th data-options="field:'tbnTotal',width:80,sortable:true">碱基总数</th>
			<th data-options="field:'status',width:80,sortable:true">状态</th>
			<th data-options="field:'createTime',width:80,sortable:true">导入时间</th>
			<th data-options="field:'modifyTime',width:80,sortable:true">修改时间</th>
			<th data-options="field:'_operate',width:80,align:'center',formatter:formatOper">操作</th>
		</tr>
	</thead>
</table>
<script src="${ctx}/views/order/js/orderExamine.js" ></script>
<script type="text/javascript">
examineIni();
$(function(){
    var dg = $('#orderList');
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
            getExamineInfo();
        }
    });
})
</script>
</body>
</html>