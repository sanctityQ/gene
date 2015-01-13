var bigIndex = undefined,orderList = $('#orderList');
function formatOper(val,row,index){
    return '&nbsp;<a href="javascript:;" onclick="lookOrder('+row.id+','+index+')"><i class="icon-book"></i>查看</a>&nbsp; <span class="gray">|</span> &nbsp;<a href="javascript:;"  onclick="modifyOrder('+row.id+','+index+')"><i class="icon-pencil"></i>修改</a>&nbsp; <span class="gray">|</span> &nbsp;<a href="javascript:;" onclick="deleteOrder('+row.id+','+index+')"><i class="icon-trash"></i>删除</a>&nbsp;';
}


/**
 * 获取订单信息
 */
var getOrderInfo=function(){
	var orderNo = $("#orderNo").val();
	var customerCode = $("#customerCode").val();
	$.ajax({
		type : "post",
		url : "/gene/order/query",
		dataType : "json",
		data:"orderNo="+orderNo+"&customerCode="+customerCode,
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

//查看
var lookOrder=function(id,index){
	var row = $('#orderList').datagrid('getData').rows[index];
	goToPage('/gene/views/order/orderView.jsp?orderNo='+row.orderNo);
}

/**
 * 明细展示
 */
var orderDetail=function(orderNo){
	$.ajax({
		type : "post",
		url : "/gene/order/productQuery",
		dataType : "json",
		data:"orderNo="+orderNo,
		success : function(data) {
			if(data != null){
				/*赋值 begin*/
                $("#code").val(data.customer.code);
                $("#name").val(data.customer.name);
                $("#leaderName").val(data.customer.leaderName);
                $("#invoiceTitle").val(data.customer.invoiceTitle);
                $("#payWays").val(data.customer.payWays);
                $("#address").val(data.customer.address);
                $("#phoneNo").val(data.customer.phoneNo);
                $("#email").val(data.customer.email);
                $("#webSite").val(data.customer.webSite);
                $("#customerUnit").val(data.customer.invoiceTitle);
                
                $("#createTime").html("<b>订购日期：</b>"+data.orderPage.content[0].createTime);
                $("#totalValue").html("<b class='bule'>订单总计：</b>￥ "+data.orderPage.content[0].totalValue);
                /*赋值 end*/
        		var total = data.orderPage.content[0].primerProducts.length;
        		var reSultdata = data.orderPage.content[0].primerProducts;
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#bigToSmall').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			alert("无法获取信息");
		}
	});
}

//修改
var modifyOrder=function(id,index){
	var row = $('#orderList').datagrid('getData').rows[index];
	goToPage('/gene/views/order/orderInfo.jsp?orderNo='+row.orderNo);
}

//删除
var deleteOrder=function(id,index){
	var row = $('#orderList').datagrid('getData').rows[index];
	var orderNo = row.orderNo;
	$.ajax({
		type : "post",
		url : "/gene/order/delete",
		dataType : "html",
		data:"orderNo="+orderNo,
		success : function(data) {
			if(data == "sucess"){
				alert("删除成功！")
				getOrderInfo();
			}
		},
		error:function(){
			alert("删除失败!");
		}
	});
}

$(function(){
   $('#orderList').datagrid({
	   
   });
   $('#orderList').datagrid('getPager').pagination({
     onBeforeRefresh:function(pageNumber, pageSize){
       $(this).pagination('loading');
       alert('pageNumber:'+pageNumber+',pageSize:'+pageSize);
       $(this).pagination('loaded');
     },
   });
});
