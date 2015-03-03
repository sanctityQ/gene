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
	
	$.ajax({
		type : "post",
		url : "/gene/print/printReportQuery",
		dataType : "json",
		data:{
			customercode: customercode,
			orderNo: orderNo,
			modifyTime:'',
			pageNo: gridOpts.pageNumber,
			pageSize: gridOpts.pageSize
        },
		success : function(data) {
			if(data != null){
        		var total = data.totalElements;
        		var reSultdata = data.content;
        		for(var i=0;i<reSultdata.length;i++){
        			if(reSultdata[i].outOrderNO!=''){
        				reSultdata[i].orderNo = reSultdata[i].outOrderNO;
        			}
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
}