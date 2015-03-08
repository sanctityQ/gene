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
	var customercode = '';
	if($("#seachCustom").val() != ''){
		customercode = $("#customercode").val();
	}
	$.ajax({
		type : "post",
		url : "/gene/synthesis/makeBoardQuery",
		dataType : "json",
		data:{
			customercode: customercode,
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

var printOutBoundQuery=function(){
	var gridOpts = $('#productionData').datagrid('getPager').data("pagination").options;
	var customerName = $("#seachCustom").val();
	var orderNo =  $("#seachOrder").val();
	
	$.ajax({
		type : "post",
		url : ctx+"/print/printOutBoundQuery",
		dataType : "json",
		data:{
			customerName: customerName,
			orderNo: orderNo,
			pageNo: gridOpts.pageNumber,
			pageSize: gridOpts.pageSize,
			status:'1'
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
}


function printOutBound(){
    var rows = $('#productionData').datagrid('getSelections');
    var orderInfoList=new Array();
    for(var i = 0; i < rows.length; i++){
        var data = rows[i];
        var outbound = {
			     "orderNo":""
			     };
        outbound["orderNo"] = "";
        outbound["orderNo"] = data.orderNo;
        orderInfoList.push(outbound);
    }
    var path = ctx+'/print/printOutBound?orderInfoList='+JSON.stringify(orderInfoList); 
    progress();
	 $('#printOutBoundfm').attr("action", path).submit();
	 //window.location.href = path;
}