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
<form action="" id='queryForm'>
<div class="tools">
	<table width="100%">
		<tr>
			<td align="right">订单号:</td>
			<td>
			<input class="inp_text" type="text" id="seachOrder" name="orderNo" value="" style="width:120px"/>
			<ul id="seachOrderList"></ul>
			</td>
			<td>客户代码:</td>
			<td><input class="inp_text" type="text" id="seachCustom" name="customerName" style="width:150px" />
			    <input class="inp_text" type="hidden" id="customercode" name="customercode" />
			    <ul id="seachCustomList"></ul>
			</td>
		    <td align="right">订单日期:</td>
            <td>
                <input type="text" class="easyui-datebox" id="createStartTime" required="required" style="width: 90px;">
                -
                <input type="text" class="easyui-datebox" id="createEndTime" required="required" style="width: 90px;">
            </td>
            <td>生产编号:</td>
			<td><input class="inp_text" type="text" id="productNo" name="productNo" style="width:120px" />
			</td>
			<td><button type="button" class="btn" onclick="getOrderInfo();">查询</button></td>
			<td align="right">
				<!-- <button type="button" class="btn btn-primary" onclick="goToPage('addOrder.html')">增加订单</button>
				<button type="button" class="btn btn-primary submit" onclick="DeletdRows('orderList')">批量删除</button> -->
			</td>
		</tr>
	</table>
</div>
<div class="content_box info margin_btoom" id="productDiv" style="display:none">
		<h2>引物生产数据信息</h2>
		<table width="100%" class="order_info">
			<tr>
				<td colspan="4" height="10"></td>
			</tr>
			<tr>
				<td align="right">生产编号:</td>
				<td><input id="product_No" name="product_No" class="readonly_inp" type="text"  style="width: 80%" value="" disabled/></td>
				<td align="right">订单号:</td>
				<td><input id="orderNo" name="orderNo" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
			</tr>
			<tr>
				<td align="right">外部生产编号:</td>
				<td><input id="outProductNo" name="outProductNo" class="readonly_inp" type="text"  style="width: 80%" value="" disabled/></td>
				<td align="right">引物名称:</td>
				<td><input id="primeName" name="primeName" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
			</tr>
			<tr>
				<td align="right">引物序列:</td>
				<td><input id="geneOrder" name="geneOrder" class="readonly_inp" type="text"  style="width: 80%" value="" disabled/></td>
				<td align="right">带修饰引物序列:</td>
				<td><input id="geneOrderMidi" name="geneOrderMidi" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
			</tr>
			<tr>
				<td align="right">纯化方式:</td>
				<td><input id="purifyType" name="purifyType" class="readonly_inp" type="text"  style="width: 80%" value="" disabled/></td>
			</tr>
			<tr>
				<td align="right">5修饰类型:</td>
				<td><input id="modiFiveType" name="modiFiveType" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
				<td align="right">3修饰类型:</td>
				<td><input id="modiThreeType" name="modiThreeType" class="readonly_inp" type="text"  style="width: 80%" value="" disabled/></td>
			</tr>
			<tr>
				<td align="right">中间修饰:</td>
				<td><input id="modiMidType" name="modiMidType" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
				<td align="right">特殊单体:</td>
				<td><input id="modiSpeType" name="modiSpeType" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
			</tr>
			<tr>
				<td align="right">OD总量:</td>
				<td><input id="odTotal" name="odTotal" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
				<td align="right">OD/Tube:</td>
				<td><input id="odTB" name="odTB" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
			</tr>
			<tr>
				<td align="right">nmole总量:</td>
				<td><input id="nmolTotal" name="nmolTotal" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
				<td align="right">nmole/Tube:</td>
				<td><input id="nmolTB" name="nmolTB" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
			</tr>
			<tr>
				<td align="right">碱基数:</td>
				<td><input id="tbn" name="tbn" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
				<td align="right">分装管数:</td>
				<td><input id="tb" name="tb" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
			</tr>
			<tr>
				<td align="right">修饰价格:</td>
				<td><input id="modiPrice" name="modiPrice" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
				<td align="right">碱基单价:</td>
				<td><input id="baseVal" name="baseVal" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
			</tr>
			<tr>
				<td align="right">纯化价格:</td>
				<td><input id="purifyVal" name="purifyVal" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
				<td align="right">总价格:</td>
				<td><input id="totalVal" name="totalVal" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
			</tr>
			<tr>
				<td align="right">描述:</td>
				<td><input id="remark" name="remark" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
				<td align="right">状态:</td>
				<td><input id="operationType" name="operationType" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
			</tr>
			<tr>
				<td align="right">板号:</td>
				<td><input id="boardNo" name="boardNo" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
				<td align="right">归属机构代码:</td>
				<td><input id="comCode" name="comCode" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
			</tr>
			<tr>
				<td align="right">循环重回次数:</td>
				<td><input id="backTimes" name="backTimes" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
				<td align="right">测值体积:</td>
				<td><input id="measureVolume" name="measureVolume" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
			</tr>
			<tr>
				<td align="right">最后操作时间:</td>
				<td><input id="modifyTime" name="modifyTime" class="readonly_inp" type="text" style="width: 80%" value="" disabled/></td>
			</tr>
		</table>
	</div>
</form>
<div id="orderDiv">
	<table id="orderList" class="easyui-datagrid" data-options="striped:true,method: 'get',pagination:true,fitColumns:true">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'orderNo',width:70,sortable:true">订单号</th>
				<th data-options="field:'outOrderNo',width:70,sortable:true">外部订单号</th>
				<th data-options="field:'customerName',width:80,sortable:true">客户姓名</th>
				<th data-options="field:'productNoMinToMax',width:90,sortable:true">生产编号</th>
				<th data-options="field:'tbnTotal',width:40,sortable:true">碱基总数</th>
				<th data-options="field:'status',width:50,sortable:true">状态</th>
				<th data-options="field:'createTime',width:80,sortable:true">导入时间</th>
				<th data-options="field:'modifyTime',width:80,sortable:true">修改时间</th>
				<th data-options="field:'_operate',width:80,align:'center',formatter:formatOper">操作</th>
			</tr>
		</thead>
	</table>
</div>
<div id="inputCause" class="easyui-dialog" data-options="closed:true"><textarea class="inp_text" style="width: 376px;height: 102px;"></textarea></div>
<script src="${ctx}/views/order/js/orderList.js" ></script>
<script src="${ctx}/static/js/vagueSeachOrder.js" ></script>
<script src="${ctx}/static/js/vagueSeachCustom.js" ></script>
<script type="text/javascript">

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
 var orderNo = '${param.orderNo}';
 if(orderNo!=''){
 	$("#seachOrder").val(orderNo);
 	orderInfoIni();
 }
</script>
</body>
</html>