var bigIndex = undefined,orderList = $('#orderList'),batchData = 0;
function cellStyler(value,row,index){
    return 'color:red;';
}
function formatOper(val,row,index){
    return '&nbsp;<a href="javascript:;" onclick="lookOrder('+row.id+','+index+')"><i class="icon-truck"></i>发货处理</a>';
}
function batchDelivery(){
	var data = orderList.datagrid('getSelections');
	//console.log(data);
	//alert("发货数据："+data.length);

	var win = $.messager.progress({
        title:'系统消息',
        msg:'请稍候…',
        text:'页面保存中…'
    });
	
    //查询条件的值
	var orderNo_con      = $("#orderNo").val();
	var customerCode_con = $("#customerCode").val();
	var customerName_con = $("#seachCustom").val();
	var customerFlag_con = $("#customerFlag").val();
    var createStartTime_con = $('#createStartTime').datebox('getValue');
    var createEndTime_con   = $('#createEndTime').datebox('getValue');
    
    $.ajax({
    	url : "/gene/delivery/batchDelivery",
    	type : "post",
        dataType : "json",
        data: {
        	"orderInfos":data
        },
		success : function(data) {
			if(data != null){
			    $.messager.alert('系统提示','数据已保存！','',function(){
			        goToPage('/gene/views/delivery/deliveryResults.jsp?orderNo_con='+orderNo_con+
                            '&customerCode_con='+customerCode_con+
                            '&customerName_con='+customerName_con+
                            '&customerFlag_con='+customerFlag_con+
                            '&createStartTime_con='+createStartTime_con+
                            '&createEndTime_con='+createEndTime_con+
                            '&autoSeachFlag=1');
			    });
			}
		},
		error:function(){
			$.messager.progress('close');
			alert("数据保存失败，请重试！");
		}
	});
    
    $.messager.progress('close');
    
}
/**
 * 订单列表初始化
 */
var orderInfoIni=function(){
	
	progress();

	var orderNo = $("#orderNo").val();
	var customerCode = $("#customerCode").val();
    var createStartTime = $('#createStartTime_con').val();
    var createEndTime   = $('#createEndTime_con').val();
    
    alert(createStartTime);
    alert(createEndTime);
	$.ajax({
		type : "post",
		url : "/gene/order/queryDeliveryDeal",
		dataType : "json",
		data:
		{
			orderNo:orderNo,
	        customerCode:customerCode,
	        customerFlagStr: $("#customerFlag").val(),
	        createStartTime:createStartTime,
	        createEndTime:createEndTime
        },
		success : function(data) {
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
				orderList.datagrid({
					onClickCell:function(rowIndex, field, value){
						if(field == "deliveryRemark"){
							var dig = $('#inputCause');
							console.log(reSultdata)
							$('.inp_text',dig).val(value);
							dig.dialog({
								title: '添加备注',
								width: 400,
								height: 200,
								closed: false,
								cache: false,
								modal: true,
								buttons:[{
									text:'取 消',
									handler:function(){
										dig.dialog('close');
										return false;
									}
								},{
									text:'保 存',
									handler:function(){
										var text = $('.inp_text',dig).val();
										if(text != value){
											//alert('调保存数据接口：'+ "rowIndex:"+rowIndex+',field:'+field);
											orderList.datagrid('updateRow', {index:rowIndex,row:{deliveryRemark:text}});
										}
										dig.dialog('close');
									}
								}]
							});
						}
					},
					onCheck: function(rowIndex,rowData){
						batchData += 1;
						setDisabel();
					},
					onUncheck: function(rowIndex,rowData){
						batchData -= 1;
						setDisabel();
					},
					onCheckAll:function(rows){
						batchData = rows.length;
						setDisabel();
					},
					onUncheckAll:function(){
						batchData = 0;
						setDisabel();
					}
				});
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#orderList').datagrid("loadData",jsonsource);
			}
			$.messager.progress('close');
		},
		error:function(){
			$.messager.progress('close');
			alert("无法获取信息");
		}
	});
}
function setDisabel(){
	var btachBtn = $('#btachBtn');
	if(batchData > 0){
		btachBtn.removeAttr("disabled");
	}else{
		btachBtn.attr("disabled","disabled");
	}
}
/**
 * 获取订单信息
 */
var getOrderInfo=function(){
	if($("#customerFlagOld").val()!='0'){
		alert("只有梓熙生物公司的用户才可以使用此功能。");
		return false;
	}
	
    var win = $.messager.progress({
        title:'系统消息',
        msg:'请稍候…',
        text:'页面载入中…'
    });
    
    var gridOpts = $('#orderList').datagrid('getPager').data("pagination").options;
	var orderNoVal = $("#orderNo").val();
	var customerCode = $("#customerCode").val();
    var createStartTime = $('#createStartTime').datebox('getValue');
    var createEndTime   = $('#createEndTime').datebox('getValue');
    
	$.ajax({
		type : "post",
		url : "/gene/order/queryDeliveryDeal",
		dataType : "json",
		data:
		{
			orderNo:orderNoVal,
	        customerCode:customerCode,
	        customerFlagStr: $("#customerFlag").val(),
	        createStartTime:createStartTime,
	        createEndTime:createEndTime,
			pageNo: gridOpts.pageNumber,
			pageSize: gridOpts.pageSize
        },
		success : function(data) {
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
				orderList.datagrid({
					onClickCell:function(rowIndex, field, value){
						if(field == "deliveryRemark"){
							var dig = $('#inputCause');
//							console.log(reSultdata)
							$('.inp_text',dig).val(value);
							dig.dialog({
								title: '添加备注',
								width: 400,
								height: 200,
								closed: false,
								cache: false,
								modal: true,
								buttons:[{
									text:'取 消',
									handler:function(){
										dig.dialog('close');
										return false;
									}
								},{
									text:'保 存',
									handler:function(){
										var text = $('.inp_text',dig).val();
										if(text != value){
											//alert('调保存数据接口：'+ "rowIndex:"+rowIndex+',field:'+field);
											orderList.datagrid('updateRow', {index:rowIndex,row:{deliveryRemark:text}});
										}
										dig.dialog('close');
									}
								}]
							});
						}
					},
					onCheck: function(rowIndex,rowData){
						batchData += 1;
						setDisabel();
					},
					onUncheck: function(rowIndex,rowData){
						batchData -= 1;
						setDisabel();
					},
					onCheckAll:function(rows){
						batchData = rows.length;
						setDisabel();
					},
					onUncheckAll:function(){
						batchData = 0;
						setDisabel();
					}
				});
        		var jsonsource = {total: total, rows: reSultdata};
				orderList.datagrid("loadData",jsonsource);

			}
			$.messager.progress('close');
		},
		error:function(){
			$.messager.progress('close');
			alert("无法获取信息");
		}
	});
}

Array.prototype.remove=function(dx)
{
	if(isNaN(dx)||dx>this.length){return false;}
	for(var i=0,n=0;i<this.length;i++)
	{
		if(this[i]!=this[dx])
		{
			this[n++]=this[i]
		}
	}
	this.length-=1
}

//查看
var lookOrder=function(id,index){
	var row = $('#orderList').datagrid('getData').rows[index];
	var orderNo_con      = $("#orderNo").val();
	var customerCode_con = $("#customerCode").val();
	var customerName_con = $("#seachCustom").val();
	var customerFlag_con = $("#customerFlag").val();
	goToPage('/gene/views/delivery/deliveryView.jsp?orderNo='+row.orderNo+
			                                       '&orderNo_con='+orderNo_con+
			                                       '&customerCode_con='+customerCode_con+
			                                       '&customerName_con='+customerName_con+
			                                       '&customerFlag_con='+customerFlag_con);
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

//发货保存
function saveDelivery(){
	
    var win = $.messager.progress({
        title:'系统消息',
        msg:'请稍候…',
        text:'页面保存中…'
    });
    
    //查询条件的值
	var orderNo_con      = $("#orderNo_con").val();
	var customerCode_con = $("#customerCode_con").val();
	var customerName_con = $("#customerName_con").val();
	var customerFlag_con = $("#customerFlag_con").val();
	
    var rows = $('#bigToSmall').datagrid('getSelections');
    var primerProducts = [];
    if(rows.length == 0){
    	$.messager.progress('close');
    	$.messager.alert('系统提示','请选择需要进行发货处理的数据。')
    	return false;
    }
    
    var saveFlag = true;
    
    for(var i = 0; i < rows.length; i++){
        var data = rows[i];
        if( data.operationType != 'delivery'){
        	$.messager.progress('close');
        	$.messager.alert('系统提示','您选择的生产编号:'+data.productNo+'的状态为:'+data.operationTypeDesc+'，不能进行发货处理。')
        	saveFlag = false;
        	break;
        	
        }
        primerProducts.push(
          {
        	"id":data.id
          }
        );
    }
    //不是待发货状态的数据不允许提交
    if(!saveFlag){
    	return false;
    }
    
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
			        goToPage('/gene/views/delivery/deliveryResults.jsp?orderNo_con='+orderNo_con+
                            '&customerCode_con='+customerCode_con+
                            '&customerName_con='+customerName_con+
                            '&customerFlag_con='+customerFlag_con+
                            '&autoSeachFlag=1');
			    });
			}
		},
		error:function(){
			alert("数据保存失败，请重试！");
		}
	});
    
    $.messager.progress('close');
}