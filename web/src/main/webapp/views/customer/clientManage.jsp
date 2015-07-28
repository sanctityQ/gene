<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<%@page import="org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
<script type="text/javascript">
var ctx = '${ctx}';
</script>
<%
ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
String customerFlag = user.getUser().getCustomer().getCustomerFlag();
String comCode = user.getUser().getCompany().getComCode();


%>
</head>
<body>
<div class="tools">
	<table width="100%">
		<tr>
			<td align="right">公司名称:</td>
			<td>
			<input class="inp_text" type="text" autocomplete="off" id="seachCustom" name="customerName" value="" style="width:60%" />
			<input class="inp_text" id="customerCode" type="hidden" name="customerCode" />
			<ul id="seachCustomList"></ul>
			</td>
			<td align="right">公司性质:</td>
			<td>
                <select id="customerFlag" class="my_select" style="width: 100px;" >
                    <option value="">请选择</option>
                    <option value="1">代理公司</option>
                    <option value="2">直接客户</option>
                   <% //总公司才能选择和维护梓熙 
                     if(comCode.startsWith("0")){%>
                    <option value="0">梓熙</option>
                    <% }%>
                </select>
			</td>
              <% //总公司才能选择分公司
               if(comCode.startsWith("0")){%>
			<td align="right">归属机构:</td>
			<td>
                <select id="companyList" class="my_select" style="width: 100px;">
				<c:forEach items="${companys}" var="company"  varStatus="status">
				  <option value="${company.comCode}">${company.comName}</option>
				</c:forEach>
                </select>
			</td>
              <% }%>
			<td align="left"><button type="button" class="btn" onclick="getCustomerList()">查询</button></td>
			<td align="left">
				<button type="button" class="btn btn-primary" onclick="goToPage('${ctx}/customer/addClient')">添加客户</button>
				<!-- <button type="button" class="btn btn-primary submit" onclick="deleteRows('orderList')">批量删除</button> -->
			</td>
		</tr>
	</table>
</div>
<table id="customerList" class="easyui-datagrid" data-options="striped:true,method: 'get',pagination:true,fitColumns:true">
	<thead>
		<tr>
			<th data-options="field:'ck',checkbox:true"></th>
			<th data-options="field:'id',width:30,sortable:true">编号</th>
			<th data-options="field:'code',width:50,sortable:true">公司代码 </th>
			<th data-options="field:'name',width:100,sortable:true">公司名称</th>
			<th data-options="field:'customerFlag',width:40,sortable:true">公司性质</th>
			<th data-options="field:'phoneNo',width:60,sortable:true">联系电话</th>
			<th data-options="field:'email',width:80,sortable:true">Email</th>
            <th data-options="field:'payWays',width:80,sortable:true">结算方式</th>
            <th data-options="field:'haveUserFlag',width:80,hidden:true,sortable:true"></th>
			<th data-options="field:'_operate',align:'center',formatter:formatOper">操作</th>
		</tr>
	</thead>
</table>
<script src="${ctx}/views/customer/js/clientManage.js" ></script>
<script src="${ctx}/static/js/vagueSeachCustom.js" ></script>
<script type="text/javascript">
getCustomerini();
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