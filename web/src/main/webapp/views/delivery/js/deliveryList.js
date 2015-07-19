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

var getProducts=function(){

	if($("#customerFlag").val()!='0'){
		alert("只有梓熙生物公司的用户才可以使用此功能。");
		return false;
	}
	
    var win = $.messager.progress({
        title:'系统消息',
        msg:'请稍候…',
        text:'页面载入中…'
    });
    
	var gridOpts = $('#productionData').datagrid('getPager').data("pagination").options;

	$.ajax({
		type : "post",
		url : "/gene/order/queryDeliveryList",
		dataType : "json",
		data:{
			orderNo:$("#orderNo").val(),
			customerName: $("#seachCustom").val(),
			createStartTime: $('#createStartTime').datebox('getValue'),
			createEndTime: $('#createEndTime').datebox('getValue'),
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
			$.messager.progress('close');
		},
		error:function(){
			$.messager.progress('close');
			alert("无法获取信息");
		}
	});

	
	
};

//导出发货清单
var deliveryList = function(){
	
    var table = $('#productionData');
    var orderInfos = table.datagrid('getSelections');
    var makeBoard = $('#makeBoard');
  
	makeBoard.attr('disabled','disabled');//按钮置灰
	table.datagrid('loadData', { total: 0, rows: [] });//清空数据
	
	document.form.action = "/gene/delivery/deliveryList/"+JSON.stringify(orderInfos)+"/";
	document.form.submit();
	
}




