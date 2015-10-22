<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="orderNo_con" value="${param.orderNo_con}" />
<c:set var="customerCode_con" value="${param.customerCode_con}" />
<c:set var="customerFlag_con" value="${param.customerFlag_con}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<script type="text/javascript">
var customerFlag_con = ${customerFlag_con}+"";
</script>
<meta charset="utf-8">
<%
ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
String customerFlag = user.getUser().getCustomer().getCustomerFlag();
String customerName_con = request.getParameter("customerName_con");
if(customerName_con==null || "null".equals(customerName_con)){
	customerName_con = "";
} else {
	customerName_con = new String(request.getParameter("customerName_con").getBytes("iso-8859-1"),"UTF-8");
}
String autoSeachFlag = request.getParameter("autoSeachFlag");
if(autoSeachFlag==null || "null".equals(autoSeachFlag)){
	autoSeachFlag = "0";
}
%>
</head>
<body>
<form action="" id='queryForm'>
<input type="hidden" id="customerFlagOld" name="customerFlagOld" value="<%=customerFlag %>"/>

<input type="hidden" id="autoSeachFlag" name="autoSeachFlag" value="<%=autoSeachFlag %>"/>

<div class="tools">
	<table width="100%">
		<tr>
			<td align="right">订单号:</td>
			<td><input id="orderNo" class="inp_text" type="text"  value="${orderNo_con}" style="width: 60%" /></td>
			<td>客户公司名称:</td>
			<td><input class="inp_text" type="text" id="seachCustom" name="customerName" style="width:150px" value="<%=customerName_con%>"/>
			    <input class="inp_text" type="hidden" id="customerCode" name="customerCode" value="${customerCode_con}"/>
			    <ul id="seachCustomList"></ul>
			</td>
			<td align="right">公司性质:</td>
			<td>
                <select id="customerFlag" class="my_select" style="width: 100px;" >
                    <option value="">请选择</option>
                    <option value="1" <c:if test="${customerFlag_con=='1'}">selected</c:if> >代理公司</option>
                    <option value="2" <c:if test="${customerFlag_con=='2'}">selected</c:if> >直接客户</option>
                </select>
			</td>
			<td><button type="button" class="btn" onclick="getOrderInfo();">查询</button></td>
			<td align="right">
			</td>
		</tr>
	</table>
</div>
</form>
<table id="orderList" class="easyui-datagrid" data-options="striped:true,method: 'get',pagination:true,fitColumns:true,sortOrder:'desc',remoteSort:false">
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

if($("#autoSeachFlag").val()=='1'){
	orderInfoIni();
}

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