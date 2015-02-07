<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="orderNo" value="${param.orderNo}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
<link href="${ctx}/static/css/easyui.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/icon.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/perfect-scrollbar.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/master.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/font-awesome.min.css" />
<!--[if IE 7]>
  <link rel="stylesheet" href="css/font-awesome-ie7.min.css">
<![endif]-->
<script src="${ctx}/static/js/jquery.min.js" ></script>
<script src="${ctx}/static/js/jquery.easyui.min.js" ></script>
<script src="${ctx}/static/js/perfect-scrollbar.min.js" ></script>
<script src="${ctx}/static/js/index.js" ></script>
<script type="text/javascript">
var orderNo = ${orderNo};
var ctx = '${ctx}';
</script>
</head>
<body>
<div class="page_padding">
	<div class="content_box totle margin_btoom">
		<b>订单号：</b>${orderNo}<br />
		<div id="createTime"></div>
		<div id="totalValue"></div>
	</div>
	<div class="content_box info margin_btoom">
		<h2>客户信息</h2>
		<table width="100%" class="order_info">
			<tr>
				<td colspan="4" height="10"></td>
			</tr>
			<tr>
				<td align="right">客户编号:</td>
				<td><input id="code" name="customer.code" class="readonly_inp" type="text"  style="width: 80%" disabled/></td>
				<td align="right">客户姓名:</td>
				<td><input id="name" name="customer.name" class="readonly_inp" type="text" style="width: 80%" disabled/></td>
			</tr>
			<tr>
				<td align="right">负责人姓名:</td>
				<td><input id="leaderName" name="customer.leaderName" class="readonly_inp" type="text"  style="width: 80%" disabled/></td>
				<td align="right">客户单位:</td>
				<td><input id="customerUnit" class="readonly_inp" type="text" value="" style="width: 80%" disabled/></td>
			</tr>
			<tr>
				<td align="right">发票抬头:</td>
				<td><input id="invoiceTitle" name="customer.invoiceTitle" class="readonly_inp" type="text"  style="width: 80%" disabled/></td>
				<td align="right">结账方式:</td>
				<td><input id="payWays" name="customer.payWays" class="readonly_inp" type="text"  style="width: 80%" disabled/></td>
			</tr>
			<tr>
				<td align="right">客户地址:</td>
				<td><input id="address" name="customer.address" class="readonly_inp" type="text"  style="width: 50%" disabled/></td>
				<td align="right">传真:</td>
				<td><input id="fax" name="customer.fax" class="readonly_inp" type="text"  style="width: 50%" value="${customer.fax}" disabled/></td>
			</tr>
			<tr>
				<td align="right">联系电话:</td>
				<td><input id="phoneNo" name="customer.phoneNo" class="readonly_inp" type="text" style="width: 80%" disabled/></td>
				<td align="right">Email:</td>
				<td><input id="email" name="customer.email" class="readonly_inp" type="text" style="width: 80%" disabled/></td>
			</tr>
			<tr>
				<td align="right">网址:</td>
				<td><input id="webSite" class="readonly_inp" type="text" value="" style="width: 50%" disabled/></td>
				<td align="right">业务员:</td>
				<td><input id="handlerCode" class="readonly_inp" type="text" style="width: 80%" value="${customer.handlerCode}" disabled/></td>
			</tr>
			<tr>
				<td colspan="4" height="10"></td>
			</tr>
		</table>
	</div>
	<div class="content_box info margin_btoom">
		<h2>生产数据</h2>
		<table id="bigToSmall" class="easyui-datagrid" data-options="fitColumns:true,singleSelect: true,striped:true,method: 'get'">
		<thead>
			<tr>
				<th data-options="field:'productNo',width:80,sortable:true,editor:'text'">生产编号</th>
				<th data-options="field:'primeName',width:80,sortable:true,editor:'text'">引物名称</th>
				<th data-options="field:'geneOrder',width:80,sortable:true,editor:'text'">序列</th>
				<th data-options="field:'tbn',width:80,sortable:true,editor:'text'">碱基数</th>
				<th data-options="field:'purifyType',width:80,sortable:true,editor:'text'">纯化方式</th>
				<th data-options="field:'modiPrice',width:80,sortable:true,editor:'text'">修饰价格</th>
				<th data-options="field:'baseVal',width:80,sortable:true,editor:'text'">碱基单价</th>
				<th data-options="field:'purifyVal',width:80,sortable:true,editor:'text'">纯化价格</th>
				<th data-options="field:'totalVal',width:80,sortable:true,editor:'text'">总价格</th>
				<th data-options="field:'fromProductNo',width:80,hidden:true,sortable:true,editor:'text'">来源生产编号</th>
			</tr>
		</thead>
	</table>
	</div>
	<div class="tools_bar">
        <button type="" class="btn btn-primary submit" onclick="$('#inputCause').dialog('open');">审核不通过</button>
		<button type="" class="btn btn-primary" onclick="examine(${orderNo},'');">审核通过</button>
	</div>
</div>
<div id="inputCause" closed="true" class="easyui-dialog" title="请输入不同意原因：" style="width:400px;height:200px;"
     data-options="
				buttons: [{
					text:'取消',
                    handler:function(){
                        $('#inputCause').dialog('close');
                    }
                 },{
					text:'确定',
					handler:function(){
					    var text = $('#inputCause .inp_text').val();
					    if(text==''){
					    	alert('请您录入失败原因!');
					    	return;
					    }
						$('#inputCause').dialog('close');
						examine(${orderNo},text);
					}
				}]
			">
    <textarea class="inp_text" style="width: 376px;height: 102px;"></textarea>
</div>
<script src="${ctx}/views/order/js/orderInfo.js" ></script>
<script src="${ctx}/views/order/js/orderList.js" ></script>
<script src="${ctx}/views/order/js/orderExamine.js" ></script>
<script type="text/javascript">
orderDetail(${orderNo});
</script>
</body>
</html>