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
	
	var gridOpts = $('#productionData').datagrid('getPager').data("pagination").options;

	$.ajax({
		type : "post",
		url : "/gene/delivery/queryDeliveryList",
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
		},
		error:function(){
			alert("无法获取信息");
		}
	});

};

//导出发货清单
var deliveryList = function(){
	
    var table = $('#productionData');
    var primerProducts = table.datagrid('getSelections');
    var makeBoard = $('#makeBoard');
  
	var url = $('#deliveryListfm').attr("action");
	var path = url+"?primerProducts="+JSON.stringify(primerProducts);
	
	makeBoard.attr('disabled','disabled');//按钮置灰
	table.datagrid('loadData', { total: 0, rows: [] });//清空数据
	
	$('#deliveryListfm').attr("action", path).submit();//提交
   
}




