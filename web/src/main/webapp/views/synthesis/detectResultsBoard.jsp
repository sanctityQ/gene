<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<link href="${ctx}/static/css/easyui.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/icon.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/perfect-scrollbar.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/master.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/font-awesome.min.css" />
<script src="${ctx}/static/js/jquery.min.js" ></script>
<script src="${ctx}/static/js/jquery.easyui.min.js" ></script>
<script src="${ctx}/static/js/perfect-scrollbar.min.js" ></script>
<script src="${ctx}/static/js/index.js" ></script>
<script src="${ctx}/static/js/json2.js"></script>
</head>
<body>
<div class="tools">
	<table width="100%">
		    <input type="hidden" id="operationType" name="operationType" value="detect"/>
		<tr>
			<td align="right">板号:</td>
			<td><input class="inp_text" type="text" id="boardNo" name="boardNo" value="" style="width: 80%" />
			    <ul id="seachBoardList"></ul>
			</td>
			<td align="right">生产编号:</td>
			<td class="scope"><input class="inp_text" type="text" id="productNo" name="productNo" value="" style="width: 60%" /></td>
            <td>修饰方式:</td>
            <td>
                <label><input type="checkbox" id="modiFiveType" name="modiFiveType"/> 5'修饰</label>
                <label><input type="checkbox" id="modiThreeType" name="modiThreeType"/> 3'修饰</label>
                <label><input type="checkbox" id="modiMidType" name="modiMidType"/> 中间修饰</label>
                <label><input type="checkbox" id="modiSpeType" name="modiSpeType"/> 特殊修饰</label>
            </td>
            <td align="right">纯化方式:</td>
            <td>
                <label><input type="radio" id="purifyType" name="purifyType" value="OPC"/>OPC</label>
                <label><input type="radio" id="purifyType" name="purifyType" value="PAGE"/>PAGE</label>
                <label><input type="radio" id="purifyType" name="purifyType" value="HPLC"/>HPLC</label>
            </td>
            <td><button type="button" class="btn" onclick="getResultProducts();">查询</button></td>
		</tr>
        <tr>
            <td colspan="9" height="10"></td>
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
<table id="productionData" class="easyui-datagrid" data-options="striped:true,method: 'get',pagination:true,fitColumns:true,">
	<thead>
		<tr>
			<th data-options="field:'ck',checkbox:true"></th>
			<th data-options="field:'productNo',width:80,sortable:true">生产编号</th>
            <th data-options="field:'reviewFileName',width:120,sortable:true">文件</th>
			<th data-options="field:'boardNo',width:80,sortable:true">板号</th>
			<th data-options="field:'geneOrder',width:80,sortable:true">序列</th>
			<th data-options="field:'tbn',width:80,sortable:true">碱基数</th>
            <th data-options="field:'purifyType',width:80,sortable:true">纯化方式</th>
            <th data-options="field:'midi',width:80,sortable:true">修饰</th>
            <th data-options="field:'operationTypeDesc',width:80,sortable:true">状态</th>
            <th data-options="field:'backTimes',width:80,sortable:true">重回次数</th>
            <th data-options="field:'mw',width:80,sortable:true">分子量(MW)</th>
            <th data-options="field:'attr9',width:80,sortable:true">操作时间</th>
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
        pageSize:10,
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