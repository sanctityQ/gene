var bigIndex = undefined,orderList = $('#orderList');
function formatOper(val,row,index){
    return '&nbsp;<a href="javascript:;" onclick="lookCustomer('+index+')"><i class="icon-book"></i>查看</a>&nbsp; <span class="gray">|</span> &nbsp;<a href="javascript:;"  onclick="modifyCustomer('+index+')"><i class="icon-pencil"></i>修改</a>&nbsp; <span class="gray">|</span> &nbsp;<a href="javascript:;" onclick="deleteCustomer('+index+','+row.haveUserFlag+')"><i class="icon-trash"></i>删除</a>&nbsp;';
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
var deleteCustomer=function(index,haveUserFlag){
	
	if (haveUserFlag == '0'){
		alert("该客户存在用户，不允许删除。\n想要删除，需要先删除该客户下的用户。");
		return false;
	}
	
    $.messager.confirm('系统消息','确认删除选中的数据?',function(data){
    	if(data){
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
    	}else{}
    });
	
}

var getCustomerList=function(){
	var gridOpts = $('#customerList').datagrid('getPager').data("pagination").options;
	var customerName = $("#seachCustom").val();
	var customerFlag = $("#customerFlag").val();
	var companyList  = $("#companyList").val();
	var handlerName  = $("#handlerName").val();
	var contactName  = $("#contactName").val();
	
	$.ajax({
		type : "post",
		url : ctx+"/customer/query",
		dataType : "json",
		data:
		{
			customerName:customerName,
			customerFlag:customerFlag,
			companyList:companyList,
			handlerName:handlerName,
			contactName:contactName,
			pageNo: gridOpts.pageNumber,
			pageSize: gridOpts.pageSize
        },
		success : function(data) {
			if(data != null){
				var total = data.totalElements;
        		var reSultdata = data.content;
        		for(var i=0;i<reSultdata.length;i++){
        			if(reSultdata[i].customerFlag=='0'){
        			  reSultdata[i].customerFlag = '梓熙';
        			}else if(reSultdata[i].customerFlag=='1'){
        			  reSultdata[i].customerFlag = '代理公司';
        			}else if(reSultdata[i].customerFlag=='2'){
        			  reSultdata[i].customerFlag = '直接客户';	
        			}
        		}
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
	var companyList  = $("#companyList").val();
	$.ajax({
		type : "post",
		url : ctx+"/customer/query",
		dataType : "json",
		data:
		{
			companyList:companyList
        },
		success : function(data) {
			if(data != null){
				var total = data.totalElements;
        		var reSultdata = data.content;
        		for(var i=0;i<reSultdata.length;i++){
        			if(reSultdata[i].customerFlag=='0'){
        			  reSultdata[i].customerFlag = '梓熙';
        			}else if(reSultdata[i].customerFlag=='1'){
        			  reSultdata[i].customerFlag = '代理公司';
        			}else if(reSultdata[i].customerFlag=='2'){
        			  reSultdata[i].customerFlag = '直接客户';	
        			}
        		}
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#customerList').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			alert("无法获取信息");
		}
	});
}

function exportCustomer(){
	
	var customerName = $("#seachCustom").val();
	var customerFlag = $("#customerFlag").val();
	var companyList  = $("#companyList").val();
	var handlerName  = $("#handlerName").val();
	
    document.form.action = "/gene/customer/exportCustomer?customerName="+customerName+"&customerFlag="+customerFlag+"&companyList="+companyList+"&handlerName="+handlerName;
	document.form.submit();
	
}
