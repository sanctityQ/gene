<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
<%
ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
String customerFlag = user.getUser().getCustomer().getCustomerFlag();
%>
</head>
<body>
<form action="" id='queryForm'>
<input type="hidden" id="customerFlagOld" name="customerFlagOld" value="<%=customerFlag %>"/>
<div class="tools">
	<table width="100%">
		<tr>
			<td align="right">订单号:</td>
			<td><input id="orderNo" class="inp_text" type="text" value="" style="width: 60%" /></td>
			<td>客户公司名称:</td>
			<td><input class="inp_text" type="text" id="seachCustom" name="customerName" style="width:150px" />
			    <input class="inp_text" type="hidden" id="customerCode" name="customerCode" />
			    <ul id="seachCustomList"></ul>
			</td>
			<td><button type="button" class="btn" onclick="getOrderInfo();">查询</button></td>
			<td align="right">
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
			<th data-options="field:'outOrderNO',width:80,sortable:true">外部订单号</th>
			<th data-options="field:'customerName',width:80,sortable:true">客户姓名</th>
			<th data-options="field:'productNoMinToMax',width:80,sortable:true">生产编号</th>
			<th data-options="field:'tbnTotal',width:80,sortable:true">碱基总数</th>
			<th data-options="field:'status',width:80,sortable:true">状态</th>
			<th data-options="field:'createTime',width:80,sortable:true">导入时间</th>
			<th data-options="field:'modifyTime',width:80,sortable:true">修改时间</th>
			<th data-options="field:'_operate',width:80,align:'center',formatter:formatOper">操作</th>
		</tr>
	</thead>
</table>
<div id="inputCause" class="easyui-dialog" data-options="closed:true"><textarea class="inp_text" style="width: 376px;height: 102px;"></textarea></div>
<script src="${ctx}/views/delivery/js/delivery.js" ></script>
<script src="${ctx}/static/js/vagueSeachCustom.js" ></script>
<script type="text/javascript">

//orderInfoIni();

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
</script>
</body>
</html>