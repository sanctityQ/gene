<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
<script type="text/javascript">
var ctx = '${ctx}';
</script>
</head>
<body>
<div class="tools">
	<table width="100%">
		<tr>
			<td align="right">产品代码:</td>
			<td><input id="productCode" class="inp_text" type="text" style="width: 60%" /></td>
			<td align="right">类别:</td>
			<td>
                <select id="productCategories" class="my_select" style="width: 150px;" name="state">
                    <option value="modiThreeType">3'端修饰</option>
                    <option value="modiFiveType">5'端修饰</option>
                    <option value="modiMidType">中间修饰</option>
                    <option value="modiSpeType">特殊单体</option>
                </select>
			</td>
			<td><button type="button" class="btn" onclick="getProductInfo();">查询</button></td>
			<td align="right">
				<button type="button" class="btn btn-primary" onclick="goToPage('${ctx}/productManage/addproductMolecular')">添加产品</button>
				<!-- <button type="button" class="btn btn-primary submit" onclick="deleteRows('orderList')">批量删除</button> -->
			</td>
		</tr>
	</table>
</div>
<table id="moleculaList" class="easyui-datagrid" data-options="striped:true,method: 'get',pagination:true,fitColumns:true">
	<thead>
		<tr>
		    <th data-options="field:'id',hidden:true,width:80,sortable:true">id</th>
		    <th data-options="field:'productCategories',width:80,sortable:true">类别</th>
			<th data-options="field:'productCode',width:80,sortable:true">修饰分子代码</th>
			<th data-options="field:'modifiedMolecular',width:80,sortable:true">修饰分子量</th>
			<th data-options="field:'_operate',align:'center',formatter:formatOper">操作</th>
		</tr>
	</thead>
</table>
<script src="${ctx}/views/productManage/js/productManage.js" ></script>
<script type="text/javascript">

$(function(){
    var dg = $('#moleculaList');
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
            getProductInfo();
        }
    });
})
 </script>
</body>
</html>