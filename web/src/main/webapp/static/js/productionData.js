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
function makeBoard(url){
    var rows = $('#productionData').datagrid('getSelections');
    var ary = [];
    for(var i = 0; i < rows.length; i++){
        var data = rows[i];
        ary.push(data.productNo);
    }
    goToPage("/gene/views/synthesis/makeBoard.jsp?ary="+ary);
}

var getProducts=function(){
	
	var gridOpts = $('#productionData').datagrid('getPager').data("pagination").options;
	var strModifyFlag;
	if($('#modiFlag').is(':checked')) {
		strModifyFlag = '1';
	}else{
		strModifyFlag = '0';
	}
	
	$.ajax({
		type : "post",
		url : "/gene/synthesis/makeBoardQuery",
		dataType : "json",
		data:{
			customercode: $("#customercode").val(),
			tbn1: $("#tbn1").val(),
			tbn2: $("#tbn2").val(),
			modiFlag: strModifyFlag,
			purifytype: $("#purifytype").val(),
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