var bigIndex = undefined,orderList = $('#orderList');
function formatOper(val,row,index){
    return '&nbsp;<a href="javascript:;" onclick="lookCustomer('+index+')"><i class="icon-book"></i>查看</a>&nbsp; <span class="gray">|</span> &nbsp;<a href="javascript:;"  onclick="modifyCustomer('+index+')"><i class="icon-pencil"></i>修改</a>&nbsp; <span class="gray">|</span> &nbsp;<a href="javascript:;" onclick="deleteCustomer('+index+')"><i class="icon-trash"></i>删除</a>&nbsp;';
}

//查看
var lookCustomer=function(index){
	var row = $('#customerList').datagrid('getData').rows[index];
	var path = ctx+'/customer/customerView?customerCode='+row.code;  
	window.location.href = path;
}

//修改
var modifyCustomer=function(index){
	var row = $('#customerList').datagrid('getData').rows[index];
	var path = ctx+'/customer/modifyCustomer?customerCode='+row.code;  
	window.location.href = path;
}
//删除
var deleteCustomer=function(index){
	var row = $('#customerList').datagrid('getData').rows[index];
	var customerCode = row.code;
	$.ajax({
		type : "post",
		url : ctx+"/customer/delete",
		dataType : "html",
		data:"customerCode="+customerCode,
		success : function(data) {
			if(data == "sucess"){
				alert("删除成功！")
				getCustomerList();
			}
		},
		error:function(){
			alert("删除失败!");
		}
	});
}

var getCustomerList=function(){
	var gridOpts = $('#customerList').datagrid('getPager').data("pagination").options;
	var customerName = $("#seachCustom").val();
	var unitName = $("#seachUnitName").val();
	$.ajax({
		type : "post",
		url : ctx+"/customer/query",
		dataType : "json",
		data:
		{
			customerName:customerName,
			unitName:unitName,
			pageNo: gridOpts.pageNumber,
			pageSize: gridOpts.pageSize
        },
		success : function(data) {
			if(data != null){
				var total = data.totalElements;
        		var reSultdata = data.content;
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#customerList').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			alert("无法获取信息");
		}
	});
}

var getCustomerini=function(){
	$.ajax({
		type : "post",
		url : ctx+"/customer/query",
		dataType : "json",
		success : function(data) {
			if(data != null){
				var total = data.totalElements;
        		var reSultdata = data.content;
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#customerList').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			alert("无法获取信息");
		}
	});
}