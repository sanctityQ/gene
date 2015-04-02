<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<script src="${ctx}/static/js/json2.js"></script>
</head>
<body>
<div class="tools">
	<table width="100%">
	        <input type="hidden" id="operationType" name="operationType" value="modification"/>
		<tr>
			<td align="right">板号:</td>
			<td><input class="inp_text" type="text" autocomplete="off" id="boardNo" name="boardNo" value="" style="width: 80%" />
			    <ul id="seachBoardList"></ul>
			</td>
            <td>
                <label><input type="checkbox" id="modiFiveType" name="modiFiveType" checked /> 5'修饰</label>
                <label><input type="checkbox" id="modiThreeType" name="modiThreeType" checked /> 3'修饰</label>
                <label><input type="checkbox" id="modiMidType" name="modiMidType" checked /> 中间修饰</label>
                <label><input type="checkbox" id="modiSpeType" name="modiSpeType" checked /> 特殊修饰</label>
            </td>
            <td><button type="button" class="btn" onclick="getResultProducts();">查询</button></td>
		</tr>
        <tr>
            <td colspan="4" height="10"></td>
        </tr>
	</table>
    <div class="btn_group">
        <button id="decorate" class="btn btn-success easyui-menubutton" data-options="menu:'#dropdownMenu'" disabled="disabled">处理结果</button>
        <div id="dropdownMenu" style="width:150px;">
            <div onclick="setResult(true);"><i class="icon-ok"></i>成功</div>
            <div class="menu-sep"></div>
            <div onclick="setResult(false);"><i class="icon-remove"></i>失败</div>
        </div>
    </div>
</div>
<table id="productionData" class="easyui-datagrid" data-options="striped:true,method: 'post',pagination:true,fitColumns:true">
	<thead>
		<tr>
			<th data-options="field:'ck',checkbox:true"></th>
			<th data-options="field:'productNo',width:80,sortable:true">生产编号</th>
			<th data-options="field:'boardNo',width:80,sortable:true">板号</th>
			<th data-options="field:'geneOrder',width:80,sortable:true,styler:cellStyler1">序列</th>
			<th data-options="field:'tbn',width:40,sortable:true">碱基数</th>
            <th data-options="field:'purifyType',width:50,sortable:true">纯化方式</th>
            <th data-options="field:'midi',width:80,sortable:true">修饰</th>
            <th data-options="field:'operationTypeDesc',width:50,sortable:true">状态</th>
            <th data-options="field:'backTimes',width:40,sortable:true">重回次数</th>
            <th data-options="field:'modifyTime',width:80,sortable:true">操作时间</th>
		</tr>
	</thead>
</table>
<div id="inputCause" class="easyui-dialog" data-options="closed:true"><textarea class="inp_text" style="width: 376px;height: 102px;"></textarea></div>
<script src="${ctx}/static/js/commonResultsSelect.js" ></script>
<script src="${ctx}/static/js/vagueSeachBoard.js"></script>
<script type="text/javascript">
$(function(){
    var dg = $('#productionData');
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
            getResultProducts();
        }
    });
})
</script>
</body>
</html>