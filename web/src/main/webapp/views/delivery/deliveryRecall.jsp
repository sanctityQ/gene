<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<%
ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
String customerFlag = user.getUser().getCustomer().getCustomerFlag();
%>
</head>
<body>
<input type="hidden" id="customerFlagOld" name="customerFlagOld" value="<%=customerFlag %>"/>
<div class="tools">
	<table width="100%">
		<tr>
			<td align="right">客户公司名称:</td>
			<td><input class="inp_text" type="text" autocomplete="off" id="seachCustom" name="customerName" value="" style="width: 150px" />
			    <input class="inp_text" type="hidden" id="customercode" name="customercode" value=""/>
			    <ul id="seachCustomList"></ul>
			</td>
            <td align="right">订单时间:</td>
            <td>
                <input type="text" class="easyui-datebox" id="createStartTime" required="required" style="width: 90px;">
                -
                <input type="text" class="easyui-datebox" id="createEndTime" required="required" style="width: 90px;">
            </td>
            <td align="right">板号:</td>
            <td><input class="inp_text" type="text" id="boardNo" value="" style="width: 80%" /></td>
            <td align="right">生产编号:</td>
            <td><input class="inp_text" type="text" value="" id="productNo" style="width: 80%" /></td>
            <td><button type="button" class="btn" onclick="getProducts();">查询</button></td>
		</tr>
        <tr>
            <td colspan="4" height="10"></td>
        </tr>
	</table>
    <div class="btn_group">
        <button id="decorate" class="btn btn-success easyui-menubutton" data-options="menu:'#dropdownMenu'" disabled="disabled">处理结果</button>
        <div id="dropdownMenu" style="width:150px;">
            <div onclick="setResult(true);"><i class="icon-ok"></i>安排合成</div>
            <div class="menu-sep"></div>
            <div onclick="setResult(false);"><i class="icon-remove"></i>重新分装</div>
        </div>
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
<script src="${ctx}/views/delivery/js/deliveryRecall.js" ></script>
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
            getProducts();
        }
    });
})
</script>
</body>
</html>