<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page import="org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<script src="${ctx}/static/js/json2.js"></script>
<%
ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
String customerFlag = user.getUser().getCustomer().getCustomerFlag();
String customerName_con = request.getParameter("customerName_con");
if(customerName_con==null || "null".equals(customerName_con)){
	customerName_con = "";
} else {
	customerName_con = new String(request.getParameter("customerName_con").getBytes("iso-8859-1"),"UTF-8");
}
%>
</head>
<body>
<form name="form" action="${ctx}/delivery/deliveryList/" method="post">
<input type="hidden" id="customerFlagOld" name="customerFlagOld" value="<%=customerFlag %>"/>
<div class="tools">
	<table width="100%">
		<tr>
			<td align="right">订单号:</td>
			<td><input id="orderNo" class="inp_text" type="text" value=""  style="width: 100px"  /></td>
			<td align="right">生产编号开头:</td>
			<td><input class="inp_text" type="text" autocomplete="off" id=productNoPrefix name="productNoPrefix" style="width: 20px" />
			</td>
			<td align="right">客户公司名称:</td>
			<td><input class="inp_text" type="text" autocomplete="off" id="seachCustom" name="customerName" value="" style="width: 120px" />
			    <input class="inp_text" type="hidden" id="customercode" name="customercode" value=""/>
			    <ul id="seachCustomList"></ul>
			</td>
			<td align="right">公司性质:</td>
			<td>
                <select id="customerFlag" class="my_select" style="width: 90px;" >
                    <option value="">请选择</option>
                    <option value="1" <c:if test="${customerFlag_con=='1'}">selected</c:if> >代理公司</option>
                    <option value="2" <c:if test="${customerFlag_con=='2'}">selected</c:if> >直接客户</option>
                </select>
			</td>
            <td align="right">订单时间:</td>
            <td>
                <input type="text" class="easyui-datebox" id="createStartTime" required="required" style="width: 90px;">
                -
                <input type="text" class="easyui-datebox" id="createEndTime" required="required" style="width: 90px;">
            </td>
            <td><button type="button" class="btn" onclick="getProducts();">查询</button></td>
		</tr>
        <tr>
            <td colspan="8" height="10"></td>
        </tr>
	</table>
    <div class="btn_group">
        <button id="makeBoard" type="button" class="btn btn-primary" disabled onclick="deliveryList()">导出发货清单</button>
    </div>
</div>
<table id="productionData" class="easyui-datagrid" data-options="striped:true,method: 'get',pagination:true,fitColumns:true,sortOrder:'desc',remoteSort:false">
	<thead>
		<tr>
			<th data-options="field:'ck',checkbox:true"></th>
            <th data-options="field:'orderNo',width:80,sortable:true">订单号</th>
            <th data-options="field:'customerName',width:100,sortable:true">客户公司名称</th>
            <th data-options="field:'contactsName',width:60,sortable:true">客户联系人</th>
			<th data-options="field:'productNoMinToMax',width:100,sortable:true">生产编号</th>
			<th data-options="field:'tbnTotal',width:50,sortable:true">碱基总数</th>
            <th data-options="field:'createTime',width:80,sortable:true">导入时间</th>
            <th data-options="field:'modifyTime',width:80,sortable:true">修改时间</th>
		</tr>
	</thead>
</table>
<div id="inputCause" class="easyui-dialog" data-options="closed:true"><textarea class="inp_text" style="width: 376px;height: 102px;"></textarea></div>
</form>
<script src="${ctx}/views/delivery/js/deliveryList.js" ></script>
<script src="${ctx}/static/js/vagueSeachCustom.js" ></script>
<script type="text/javascript">
$(function(){
    var dg = $('#productionData');
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        pageSize:1000,
        pageList:[50,100,500,1000],
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