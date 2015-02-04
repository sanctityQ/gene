var bigIndex = undefined,orderList = $('#orderList');
function cellStyler(value,row,index){
    return 'color:red;';
}
function formatOper(val,row,index){
    return '&nbsp;<a href="javascript:;" onclick="lookOrder('+row.id+','+index+')"><i class="icon-truck"></i>发货处理</a>';
}

/**
 * 订单列表初始化
 */
var orderInfoIni=function(){
	$.ajax({
		type : "post",
		url : "/gene/order/query",
		dataType : "json",
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
/**
 * 获取订单信息
 */
var getOrderInfo=function(){
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

//查看
var lookOrder=function(id,index){
	var row = $('#orderList').datagrid('getData').rows[index];
	goToPage('/gene/views/delivery/deliveryView.jsp?orderNo='+row.orderNo);
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
                
                $("#createTime").html("<b>订购日期：</b>"+data.order.createTime);
                $("#totalValue").html("<b class='bule'>订单总计：</b>￥ "+data.order.totalValue);
                /*赋值 end*/
        		var total = data.order.primerProducts.length;
        		var reSultdata = data.order.primerProducts;
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#bigToSmall').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			alert("无法获取信息");
		}
	});
}

function saveDelivery(){
	
    var rows = $('#bigToSmall').datagrid('getSelections');
    var primerProducts = [];
    if(rows.length == 0){
    	$.messager.alert('系统提示','请选择需要进行发货处理的数据。')
    	return false;
    }
    for(var i = 0; i < rows.length; i++){
        var data = rows[i];
        if( data.operationType != 'delivery'){
        	$.messager.alert('系统提示','您选择的生产编号:'+data.productNo+'的状态为:'+data.operationTypeDesc+'，不能进行发货处理。')
        	break;
        	return false;
        }
        primerProducts.push(data);
    }
    //alert(primerProducts);
    
    $.ajax({
    	url : "/gene/delivery/saveDelivery",
    	type : "post",
        dataType : "json",
        data: {
        	"primerProducts":primerProducts
        },
		success : function(data) {
			if(data != null){
			    $.messager.alert('系统提示','数据已保存！','',function(){
			        goToPage('/gene/views/delivery/deliveryResults.jsp');
			    });
			}
		},
		error:function(){
			alert("数据保存失败，请重试！");
		}
	});
}