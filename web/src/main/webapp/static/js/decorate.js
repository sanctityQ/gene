$(function(){
    $('#productionData').datagrid({
        onCheck : gridCheck,
        onUncheck : gridUnCheck,
        onCheckAll : gridCheck,
        onUncheckAll : gridUnCheck
    });
})
function gridCheck(rowIndex,rowData){
    var makeBoard = $('#decorate');
    makeBoard.menubutton('enable')
}
function gridUnCheck(rowIndex,rowData){
    var makeBoard = $('#decorate');
    var rows = $('#productionData').datagrid('getSelections');
    if(!rows.length){
        makeBoard.menubutton('disable');
    };
}
function setResult(toggal){
    var table = $('#productionData');
    var primerProducts = table.datagrid('getSelections');
    var decorate = $('#decorate')
    if(toggal){
        $.messager.confirm('系统消息','确定将选中数据的修饰结果设置为“成功”吗?',function(ok){
            if(ok){

            	$.ajax({
            		type : "post",
            		url : "/gene/synthesis/submitDecorate",
            		dataType : "json",
            		data:{
            			primerProducts: primerProducts,
            			successFlag: '1',
            			failReason: ''
                    },
            		success : function(data) {
            		},
            		error:function(){
            			alert("无法处理信息");
            		}
            	});
            	
                table.datagrid('reload');
                decorate.menubutton('disable');
            }
        });
    }else{
        $('#inputCause').dialog({
            title: '请输入失败原因',
            width: 400,
            height: 200,
            closed: false,
            cache: false,
            modal: true,
            buttons:[{
                text:'取 消',
                handler:function(){
                    $('#inputCause').dialog('close');
                    return false;
                }
            },{
                text:'确 定',
                handler:function(){
                    var text = $('#inputCause .inp_text').val();
	               $.ajax({
	            		type : "post",
	            		url : "/gene/synthesis/submitDecorate",
	            		dataType : "json",
	            		data:{
	            			primerProducts: primerProducts,
	            			successFlag: '0',
	            			failReason: text
	                    },
	            		success : function(data) {
	            		},
	            		error:function(){
	            			alert("无法处理信息");
	            		}
	            	});
                    
                    $('#inputCause').dialog('close');
                    decorate.menubutton('disable');
                    table.datagrid('reload');
                }
            }]
        });
    }

}

//查询修饰信息
var getDecorateProducts=function(){
	
	var gridOpts = $('#productionData').datagrid('getPager').data("pagination").options;
	var modiFiveType;
	var modiThreeType;
	var modiMidType;
	var modiSpeType;
	if($('#modiFiveType').is(':checked')) {
		modiFiveType = '1';
	}else{
		modiFiveType = '0';
	}
	if($('#modiThreeType').is(':checked')) {
		modiThreeType = '1';
	}else{
		modiThreeType = '0';
	}
	if($('#modiMidType').is(':checked')) {
		modiMidType = '1';
	}else{
		modiMidType = '0';
	}
	if($('#modiSpeType').is(':checked')) {
		modiSpeType = '1';
	}else{
		modiSpeType = '0';
	}
	
	var boardNo = '';
	if($("#boardNo").val() != ''){
		boardNo = $("#boardNo").val();
	}
	$.ajax({
		type : "post",
		url : "/gene/synthesis/decorateQuery",
		dataType : "json",
		data:{
			boardNo: boardNo,
			modiFiveType: modiFiveType,
			modiThreeType: modiThreeType,
			modiMidType: modiMidType,
			modiSpeType: modiSpeType,
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