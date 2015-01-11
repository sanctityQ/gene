var bigIndex = undefined,orderList = $('#orderList');
function formatOper(val,row,index){
    return '<a href="javascript:;" onclick="goToPage(\'orderView.html\')">查看</a> | <a href="javascript:;" onclick="goToPage(\'orderEdit.html\')">修改</a> | <a href="javascript:;" onclick="deleteRow(\'orderList\','+index+')">删除</a>';
}


/**
 * 获取订单信息
 */
var getOrderInfo=function(){
	var orderNo = $("#orderNo").val();
	var customerCode = $("#customerCode").val();
	$.ajax({
		type : "post",
		url : "order/query",
		dataType : "json",
		data:"orderNo="+orderNo+"&customerCode="+customerCode,
		success : function(data) {
			if(data != null){
				var total = data.content.length;
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