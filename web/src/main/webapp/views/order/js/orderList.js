var bigIndex = undefined,orderList = $('#orderList');
function cellStyler(value,row,index){
	if(value!=''){
      return 'color:red;';
    }else{
      return '';	
    }
}
function formatOper(val,row,index){
	if(row.status=='订单审核通过'){
		return '&nbsp;<a href="javascript:;" onclick="lookOrder('+row.id+','+index+')"><i class="icon-book"></i>查看</a>&nbsp;';
	}else{
		return '&nbsp;<a href="javascript:;" onclick="lookOrder('+row.id+','+index+')"><i class="icon-book"></i>查看</a>&nbsp; <span class="gray">|</span> &nbsp;<a href="javascript:;"  onclick="modifyOrder('+row.id+','+index+')"><i class="icon-pencil"></i>修改</a>&nbsp; <span class="gray">|</span> &nbsp;<a href="javascript:;" onclick="deleteOrder('+row.id+','+index+')"><i class="icon-trash"></i>删除</a>&nbsp;';
	}
    
}

/**
 * 订单列表初始化
 */
var orderInfoIni=function(){
	progress();
	var orderNo = $("#seachOrder").val();
	$.ajax({
		type : "post",
		url : ctx+"/order/query",
		dataType : "json",
		data:
		{
			orderNo:orderNo
        },
		success : function(data) {
			$.messager.progress('close');
			if(data != null){
				var total = data.totalElements;
        		var reSultdata = data.content;
        		for(var i=0;i<reSultdata.length;i++){
//        			if(reSultdata[i].outOrderNO!=''){
//        				reSultdata[i].orderNo = reSultdata[i].outOrderNO;
//        			}
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
/**
 * 获取订单信息
 */
var getOrderInfo=function(){
    var gridOpts = $('#orderList').datagrid('getPager').data("pagination").options;
	var orderNo = $("#seachOrder").val();
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
			createStartTime: $('#createStartTime').datebox('getValue'),
			createEndTime: $('#createEndTime').datebox('getValue'),
			pageNo: gridOpts.pageNumber,
			pageSize: gridOpts.pageSize
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

//查看
var lookOrder=function(id,index){
	var row = $('#orderList').datagrid('getData').rows[index];
	var path = ctx+'/order/lookQuery?orderNo='+row.orderNo;  
	window.location.href = path;
}

/**
 * 明细展示
 */
var orderDetail=function(orderNo){
	progress();
	$.ajax({
		type : "post",
		url : ctx+"/order/productQuery",
		dataType : "json",
		data:"orderNo="+orderNo,
		success : function(data) {
			$.messager.progress('close');
			if(data != null){
				/*赋值 begin*/
                $("#totalValue").html("<b class='bule'>订单总计：</b>￥ "+data.order.totalValue);
                /*赋值 end*/
        		var total = data.order.primerProducts.length;
        		var reSultdata = data.order.primerProducts;
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#bigToSmall').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			$.messager.progress('close');
			alert("无法获取信息");
		}
	});
}

//修改
var modifyOrder=function(id,index){
	var row = $('#orderList').datagrid('getData').rows[index];
	 var path = ctx+'/order/modifyQuery?orderNo='+row.orderNo+'&forwordName=orderInfo';  
	 //$('#queryForm').attr("action", path).submit();
	 window.location.href = path;
}

//删除
var deleteOrder=function(id,index){
	 $.messager.confirm('系统消息','确认删除选中的数据?',function(data){
		 if(data){
			var row = $('#orderList').datagrid('getData').rows[index];
			var orderNo = row.orderNo;
			progress();
			$.ajax({
				type : "post",
				url : ctx+"/order/delete",
				dataType : "html",
				data:"orderNo="+orderNo,
				success : function(data) {
					$.messager.progress('close');
					if(data == "sucess"){
						alert("删除成功！")
						getOrderInfo();
					}
				},
				error:function(){
					$.messager.progress('close');
					alert("删除失败!");
				}
			});
		 }else{}
	 });
}

