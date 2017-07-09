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
<script src="${ctx}/static/js/json2.js"></script>
<%
ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
String customerFlag = user.getUser().getCustomer().getCustomerFlag();
%>
</head>
<body>
<div class="tools">
            <input type="hidden" id="customerFlagOld" name="customerFlagOld" value="<%=customerFlag %>"/>
	<table width="100%">
		<tr>
			<td align="right">客户公司名称:</td>
            <td> <input class="inp_text" type="text" autocomplete="off" id="seachCustom" name="customerName" value="" style="width: 120px" />
			    <input class="inp_text" type="hidden" id="customerCode" name="customerCode" value=""/>
			    <ul id="seachCustomList"></ul>
			</td>
			<td align="right">分装方式:</td>
			<td>
                <select id="liquidFlag" class="my_select" style="width: 150px;" >
                    <option value="1">干粉</option>
                    <option value="2">液体</option>
                </select>
               </td>
			<td align="right">碱基数范围:</td>
            <td> <input class="easyui-numberbox"  id="tbn1" name="tbn1" value="" style="width: 30px;" data-options="width:30" />
			                - <input class="easyui-numberbox inp_text"  id="tbn2" name="tbn2" value="" style="width:30px;" /></td>
            <td><input type="checkbox" id="modiFlag" name="modiFlag" checked /> <label>有/无修饰</label></td>
            <td><button type="button" class="btn" onclick="getProducts();">查询</button></td>            
		</tr>
		<tr>
			<td align="right">生产编号开头:</td>
            <td><input class="inp_text" type="text" autocomplete="off" id="productNoPrefix" name="productNoPrefix" style="width: 120px" />
            <td align="right">纯化方式:</td>
            <td> <select id="purifytype" class="combobox-checkbox"  style="width: 100px;" >
                    <option value=""></option>
                    <option value="OPC">OPC</option>
                    <option value="PAGE">PAGE</option>
                    <option value="page">page</option>
                    <option value="HPLC">HPLC</option>
                </select>
            </td>
			<td align="right">OD总量范围:</td>
            <td> <input class="easyui-numberbox"  id="odTotal1" name="odTotal1" value="" style="width: 30px;" data-options="width:30" />
			                - <input class="easyui-numberbox inp_text"  id="odTotal2" name="odTotal2" value="" style="width:30px;" /></td>

		</tr>
	</table>
    <div class="btn_group">
        <button id="makeBoard" class="btn btn-success" disabled onclick="makeBoard('views/synthesis/makeBoard.jsp');">制作合成板</button>
    </div>
</div>
<table id="productionData" class="easyui-datagrid" data-options="striped:true,method:'post',pagination:true,fitColumns:true,sortOrder:'desc',remoteSort:false">
	<thead>
		<tr>
			<th data-options="field:'ck',checkbox:true"></th>
			<th data-options="field:'productNo',width:65,sortable:true">生产编号</th>
			<th data-options="field:'geneOrderMidi',width:50,sortable:true,styler:cellStyler1">序列</th>
			<th data-options="field:'odTotal',width:60,sortable:true">OD总量</th>
			<th data-options="field:'odTB',width:60,sortable:true">OD/TB</th>
			<th data-options="field:'nmolTotal',width:60,sortable:true">nmol总量</th>
			<th data-options="field:'nmolTB',width:60,sortable:true">nmol/TB</th>
			<th data-options="field:'tbn',width:60,sortable:true">碱基数</th>
            <th data-options="field:'purifyType',width:60,sortable:true">纯化方式</th>
            <th data-options="field:'midi',width:120,sortable:true">修饰</th>
            <th data-options="field:'operationTypeDesc',width:60,sortable:true">状态</th>
            <th data-options="field:'remark',width:60,sortable:true">备注</th>
            <th data-options="field:'backTimes',width:40,sortable:true">重回次数</th>
            <th data-options="field:'modifyTime',width:80,sortable:true">操作时间</th>
		</tr>
	</thead>
</table>
<div id="inputCause" class="easyui-dialog" data-options="closed:true"><textarea class="inp_text" style="width: 376px;height: 102px;"></textarea></div>
<script src="${ctx}/static/js/productionData.js" ></script>
<script src="${ctx}/static/js/vagueSeachCustom.js" ></script>
<script type="text/javascript">

$('#purifytype').combobox({
	width:160,
    multiple:true,
    panelHeight:100
});  

$(function(){
    var dg = $('#productionData');
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        pageSize:1000,
        pageList:[48,96,500,1000],
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
});

</script>
</body>
</html>