var bigIndex = undefined,orderList = $('#orderList');
function formatOper(val,row,index){
    return '&nbsp;<a href="javascript:;" onclick="checkOrder('+row.id+','+index+')"><i class="icon-eye-open"></i>审核订单</a>';
}


/**
 * 订单列表初始化
 */
var examineIni=function(){
	$.ajax({
		type : "post",
		url : "/gene/order/query",
		dataType : "json",
		data:{status:0},
		success : function(data) {
			if(data != null){
				var total = data.totalElements;
        		var reSultdata = data.content;
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#orderList').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			alert("无法获取信息");
		}
	});
}


var getExamineInfo=function(){
    var gridOpts = $('#orderList').datagrid('getPager').data("pagination").options;
	var orderNo = $("#orderNo").val();
	var customerCode = $("#customerCode").val();
	$.ajax({
		type : "post",
		url : "/gene/order/query",
		dataType : "json",
		data:
		{
			orderNo:orderNo,
	        customerCode:customerCode,
			pageNo: gridOpts.pageNumber,
			pageSize: gridOpts.pageSize
        },
		success : function(data) {
			if(data != null){
				var total = data.totalElements;
        		var reSultdata = data.content;
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#orderList').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			alert("无法获取信息");
		}
	});
}

//审核订单查询
var checkOrder=function(id,index){
	var row = $('#orderList').datagrid('getData').rows[index];
	goToPage('/gene/views/order/orderInfoExamine.jsp?orderNo='+row.orderNo);
}

var examine=function(orderNo,failReason){
	$.ajax({
		type : "post",
		url : "/gene/order/examine",
		dataType : "html",
		data:
		{
			orderNo:orderNo,
			failReason:failReason
        },
		success : function(data) {
			if(data == "sucess"){
				if(failReason==''){
				  alert("审核通过！")
			    }
				goToPage('/gene/views/order/orderExamine.jsp');
			}
		},
		error:function(){
			alert("无法获取信息");
		}
	});
}