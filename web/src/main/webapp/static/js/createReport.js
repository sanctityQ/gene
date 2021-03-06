$(function(){
    $('#productionData').datagrid({
        onCheck : gridCheck,
        onUncheck : gridUnCheck,
        onCheckAll : gridCheck,
        onUncheckAll : gridUnCheck
    });
})
function gridCheck(rowIndex,rowData){
    var makeBoard = $('#makeBoard');
    makeBoard.removeAttr('disabled');
}
function gridUnCheck(rowIndex,rowData){
    var makeBoard = $('#makeBoard');
    var rows = $('#productionData').datagrid('getSelections');
    if(!rows.length){
        makeBoard.attr('disabled','disabled');
    };
}
function exportReports(){
    var rows = $('#productionData').datagrid('getSelections');
    
    if(rows.length>5){
    	alert("选择的订单个数不能大于5个。");
    	return;
    }
    var orderNo = '';
    var ary = [];
    for(var i = 0; i < rows.length; i++){
        var data = rows[i];
        orderNo += data.orderNo;
        if(i!=rows.length-1){
        	orderNo += ",";
        }
    }
    //生成报告单 按钮置灰
    $('#makeBoard').attr('disabled','disabled');
    
    document.form.action = "/gene/print/exportReports/"+orderNo+"/";
	document.form.submit();
}

var getOrderInfos=function(){
    var win = $.messager.progress({
        title:'系统消息',
        msg:'请稍候…',
        text:'页面处理中…'
    });
    
	var gridOpts = $('#productionData').datagrid('getPager').data("pagination").options;
	var customerCode = '';
	if($("#seachCustom").val() != ''){
		customerCode = $("#customerCode").val();
	}
	var orderNo = '';
	if($("#seachOrder").val() != ''){
		orderNo = $("#seachOrder").val();
	}
	
	$.ajax({
		type : "post",
		url : "/gene/print/printReportQuery",
		dataType : "json",
		data:{
			customerCode: customerCode,
			orderNo: orderNo,
			modifyTime:'',
			productNoPrefix: $("#productNoPrefix").val(),
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
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#productionData').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			alert("无法获取信息");
		}
	});
	
	$.messager.progress('close');
	
}