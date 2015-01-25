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
function exportFile(flag){
    var rows = $('#productionData').datagrid('getSelections');
    var orderNo = '';
    var ary = [];
    for(var i = 0; i < rows.length; i++){
        var data = rows[i];
        orderNo = data.orderNo;
    }
    document.form.action = "/gene/print/exportFile/"+orderNo+"/"+flag+"/";
	document.form.submit();
}
var getOrderInfos=function(){
	var gridOpts = $('#productionData').datagrid('getPager').data("pagination").options;
	var customercode = '';
	if($("#seachCustom").val() != ''){
		customercode = $("#customercode").val();
	}
	var orderNo = '';
	if($("#seachOrder").val() != ''){
		orderNo = $("#seachOrder").val();
	}
	var modifyTime = $('#modifyTime').datebox('getValue');  
	
	if(modifyTime==null){
		modifyTime = "";
	}
	
	$.ajax({
		type : "post",
		url : "/gene/print/printReportQuery",
		dataType : "json",
		data:{
			customercode: customercode,
			orderNo: orderNo,
			modifyTime:modifyTime,
			pageNo: gridOpts.pageNumber,
			pageSize: gridOpts.pageSize
        },
		success : function(data) {
			if(data != null){
        		var total = data.totalElements;
        		var reSultdata = data.content;
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#productionData').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			alert("无法获取信息");
		}
	});
}