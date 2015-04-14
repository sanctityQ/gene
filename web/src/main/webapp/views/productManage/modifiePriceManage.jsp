<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
</head>
<body>
<div class="tools">
	<table width="100%">
		<tr>
			<td align="right">产品代码:</td>
			<td><input class="inp_text" type="text" value="" style="width: 60%" /></td>
			<td align="right">类别:</td>
			<td>
                <select id="state" class="my_select" style="width: 150px;" name="state">
                    <option value="3'端修饰">3'端修饰</option>
                    <option value="5'端修饰">5'端修饰</option>
                </select>
			</td>
			<td><button type="button" class="btn">查询</button></td>
			<td align="right">
				<button type="button" class="btn btn-primary" onclick="goToPage('addProduct.html')">添加产品</button>
				<button type="button" class="btn btn-primary submit" onclick="deleteRows('orderList')">批量删除</button>
			</td>
		</tr>
	</table>
</div>
<table id="orderList" class="easyui-datagrid" data-options="striped:true,method: 'get',pagination:true,fitColumns:true,url: 'datagrid_data1.json'">
	<thead>
		<tr>
			<th data-options="field:'ck',checkbox:true"></th>
			<th data-options="field:'itemid',width:80,sortable:true">产品代码</th>
			<th data-options="field:'productid',width:80,sortable:true">分子量</th>
			<th data-options="field:'listprice',width:80,sortable:true">类别</th>
			<th data-options="field:'status',width:80,sortable:true">基准价格</th>
			<th data-options="field:'_operate',align:'center',formatter:formatOper">操作</th>
		</tr>
	</thead>
</table>
<script src="js/productManage.js" ></script>
</body>
</html>