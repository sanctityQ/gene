var bigIndex = undefined,orderList = $('#orderList');
function formatOper(val,row,index){
    return '&nbsp;<a href="javascript:;" onclick="checkOrder('+row.id+','+index+')"><i class="icon-eye-open"></i>审核订单</a>';
}


/**
 * 订单列表初始化
 */
var examineIni=function(){
	progress();
	$.ajax({
		type : "post",
		url : ctx+"/order/query",
		dataType : "json",
		data:{orderStatus:'examine'},
		success : function(data) {
			$.messager.progress('close');
			if(data != null){
				var total = data.totalElements;
        		var reSultdata = data.content;
        		for(var i=0;i<reSultdata.length;i++){
        			if(reSultdata[i].status=='0'){
        			  reSultdata[i].status = '订单初始化';
        			}else if(reSultdata[i].status=='1'){
        			  reSultdata[i].status = '订单审核通过';
        			}else if(reSultdata[i].status=='2'){
        			  reSultdata[i].status = '订单审核不通过';	
        			}
        		}
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#orderList').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			$.messager.progress('close');
			alert("无法获取信息");
		}
	});
}


var getExamineInfo=function(){
    var gridOpts = $('#orderList').datagrid('getPager').data("pagination").options;
	var orderNo = $("#orderNo").val();
	var customerCode = $("#customerCode").val();
	progress();
	$.ajax({
		type : "post",
		url : ctx+"/order/query",
		dataType : "json",
		data:
		{
			orderNo:orderNo,
	        customerCode:customerCode,
			pageNo: gridOpts.pageNumber,
			pageSize: gridOpts.pageSize,
			orderStatus:'examine'
        },
		success : function(data) {
			$.messager.progress('close');
			if(data != null){
				var total = data.totalElements;
        		var reSultdata = data.content;
        		for(var i=0;i<reSultdata.length;i++){
        			if(reSultdata[i].status=='0'){
        			  reSultdata[i].status = '订单初始化';
        			}else if(reSultdata[i].status=='1'){
        			  reSultdata[i].status = '订单审核通过';
        			}else if(reSultdata[i].status=='2'){
        			  reSultdata[i].status = '订单审核不通过';	
        			}else if(reSultdata[i].status=='3'){
        			  reSultdata[i].status = '通过后已修改';	
        			}
        		}
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#orderList').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			$.messager.progress('close');
			alert("无法获取信息");
		}
	});
}

//审核订单查询
var checkOrder=function(id,index){
	var row = $('#orderList').datagrid('getData').rows[index];
//	goToPage(ctx+'/views/order/orderInfoExamine.jsp?orderNo='+row.orderNo);
	var path = ctx+'/order/modifyQuery?orderNo='+row.orderNo+'&forwordName=orderInfoExamine';  
	 window.location.href = path;
}

var examine=function(orderNo,failReason){
	progress();
	$.ajax({
		type : "post",
		url : ctx+"/order/examine",
		dataType : "html",
		data:
		{
			orderNo:orderNo,
			failReason:failReason
        },
		success : function(data) {
			$.messager.progress('close');
			if(data == "sucess"){
				if(failReason==''){
				  alert("审核通过！")
			    }
				goToPage(ctx+'/views/order/orderExamine.jsp');
			}
		},
		error:function(){
			$.messager.progress('close');
			alert("无法获取信息");
		}
	});
}