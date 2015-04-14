var bigIndex = undefined,orderList = $('#orderList');
function formatOper(val,row,index){
    return '&nbsp;<a href="javascript:;"  onclick="modifyProduct('+row.id+','+index+')"><i class="icon-pencil"></i>修改</a>&nbsp; <span class="gray">|</span> &nbsp;<a href="javascript:;" onclick="deleteProduct('+row.id+','+index+')"><i class="icon-trash"></i>删除</a>&nbsp;';
}


//修改
var modifyProduct=function(id,index){
	var row = $('#moleculaList').datagrid('getData').rows[index];
	 var path = ctx+'/productManage/modifyQuery?id='+row.id;  
	 window.location.href = path;
}

//删除
var deleteProduct=function(id,index){
	 $.messager.confirm('系统消息','确认删除选中的数据?',function(data){
		 if(data){
			var row = $('#moleculaList').datagrid('getData').rows[index];
			var id = row.id;
			progress();
			$.ajax({
				type : "post",
				url : ctx+"/productManage/delete",
				dataType : "html",
				data:"id="+id,
				success : function(data) {
					$.messager.progress('close');
					if(data == "sucess"){
						alert("删除成功！")
						getProductInfo();
					}
				},
				error:function(){
					$.messager.progress('close');
					alert("删除失败!");
				}
			});
		 }else{}
	 });
}

//查询
var getProductInfo=function(){
    var gridOpts = $('#moleculaList').datagrid('getPager').data("pagination").options;
	var productCode = $("#productCode").val();
	var productCategories = $("#productCategories").val();
	progress();
	$.ajax({
		type : "post",
		url : ctx+"/productManage/query",
		dataType : "json",
		data:
		{
			productCode:productCode,
			productCategories:productCategories,
			pageNo: gridOpts.pageNumber,
			pageSize: gridOpts.pageSize
        },
		success : function(data) {
			$.messager.progress('close');
			if(data != null){
				var total = data.totalElements;
        		var reSultdata = data.content;
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#moleculaList').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			$.messager.progress('close');
			alert("无法获取信息");
		}
	});
}

var productMolecularSave=function(){
	if($("#productCode").val()==''){
		alert("请录入产品代码！");
		return false;
	}
	if($("#modifiedMolecular").val()==''){
		alert("请录入修饰分子量！");
		return false;
	}
	
	$("#molecularfm").submit();
}