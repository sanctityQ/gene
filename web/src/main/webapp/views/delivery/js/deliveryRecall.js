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
    var row = table.datagrid('getSelections');
    var decorate = $('#decorate');
    var title = '';
    var flag = '';
    if(toggal){
        title = '安排合成原因';
        flag = '1';
    }else{
        title = '重新分装原因';
        flag = '2';
    }
    $('#inputCause').dialog({
        title: title,
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
                if($.trim(text)==""){
                	$.messager.alert('系统提示','请填写失败原因。')
                	return false;
                }
                $('#inputCause').dialog('close');
                decorate.menubutton('disable');
                saveBack(row,flag,text);
            }
        }]
    });

}

var saveBack=function(row,flag,text){
	
	//提交
    $.ajax({
		type : "post",
		url : "/gene/delivery/saveBack",
		dataType : "json",
		data:{
			"orderInfos": row,
			"flag": flag,
			"text": text
        },
		success : function(data) {
			getProducts();
		},
		error:function(){
			alert("无法获取信息");
		}
	});
	
	
}
var getProducts=function(){
	
	if($("#customerFlagOld").val()!='0'){
		alert("只有梓熙生物公司的用户才可以使用此功能。");
		return false;
	}
	
    var win = $.messager.progress({
        title:'系统消息',
        msg:'请稍候…',
        text:'页面载入中…'
    });
    
	var gridOpts = $('#productionData').datagrid('getPager').data("pagination").options;

	var customercode = '';
	if($("#seachCustom").val() != ''){
		customercode = $("#customercode").val();
	}
	
	$.ajax({
		type : "post",
		url : "/gene/delivery/queryBack",
		dataType : "json",
		data:{
			customerCode: customercode,
			createStartTime: $('#createStartTime').datebox('getValue'),
			createEndTime: $('#createEndTime').datebox('getValue'),
			boardNo: $("#boardNo").val(),
			productNo: $("#productNo").val(),
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

	$.messager.progress('close');
	
}