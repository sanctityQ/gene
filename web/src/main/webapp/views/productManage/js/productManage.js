var bigIndex = undefined,orderList = $('#orderList');
function formatOper(val,row,index){
    return '&nbsp;<a href="javascript:;"  onclick="modifyProduct('+row.id+','+index+')"><i class="icon-pencil"></i>修改</a>&nbsp; <span class="gray">|</span> &nbsp;<a href="javascript:;" onclick="deleteProduct('+row.id+','+index+')"><i class="icon-trash"></i>删除</a>&nbsp;';
}

function formatOperModi(val,row,index){
    return '&nbsp;<a href="javascript:;"  onclick="modifyModiPrice('+row.id+','+index+')"><i class="icon-pencil"></i>修改</a>&nbsp; <span class="gray">|</span> &nbsp;<a href="javascript:;" onclick="deleteModiPrice('+row.id+','+index+')"><i class="icon-trash"></i>删除</a>&nbsp;';
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
	var validate = $("#validate").val();
	progress();
	$.ajax({
		type : "post",
		url : ctx+"/productManage/query",
		dataType : "json",
		data:
		{
			productCode:productCode,
			productCategories:productCategories,
			validate:validate,
			pageNo: gridOpts.pageNumber,
			pageSize: gridOpts.pageSize
        },
		success : function(data) {
			$.messager.progress('close');
			if(data != null){
				var total = data.totalElements;
        		var reSultdata = data.content;
        		for(var i=0;i<reSultdata.length;i++){
        			if(reSultdata[i].productCategories=='modiThreeType'){
          			  reSultdata[i].productCategories = '3端修饰';
          			}else if(reSultdata[i].productCategories=='modiFiveType'){
          			  reSultdata[i].productCategories = '5端修饰';
          			}else if(reSultdata[i].productCategories=='modiSpeType'){
          			  reSultdata[i].productCategories = '特殊单体';	
          			}else if(reSultdata[i].productCategories=='modiMidType'){
          			  reSultdata[i].productCategories = '中间修饰';	
          			}
        			
        		}
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


//修改
var modifyModiPrice=function(id,index){
	var row = $('#modiPriceList').datagrid('getData').rows[index];
	 var path = ctx+'/productManage/modifyModiPrice?id='+row.id;  
	 window.location.href = path;
}

//删除
var deleteModiPrice=function(id,index){
	 $.messager.confirm('系统消息','确认删除选中的数据?',function(data){
		 if(data){
			var row = $('#modiPriceList').datagrid('getData').rows[index];
			var id = row.id;
			progress();
			$.ajax({
				type : "post",
				url : ctx+"/productManage/deleteModiPrice",
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
var getModiPriceInfo=function(){
    var gridOpts = $('#modiPriceList').datagrid('getPager').data("pagination").options;
	var modiType = $("#modiType").val();
	var validate = $("#validate").val();
	progress();
	$.ajax({
		type : "post",
		url : ctx+"/productManage/queryModiPrice",
		dataType : "json",
		data:
		{
			modiType:modiType,
			validate:validate,
			pageNo: gridOpts.pageNumber,
			pageSize: gridOpts.pageSize
        },
		success : function(data) {
			$.messager.progress('close');
			if(data != null){
				var total = data.totalElements;
        		var reSultdata = data.content;
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#modiPriceList').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			$.messager.progress('close');
			alert("无法获取信息");
		}
	});
}


var modiPriceSave=function(){
	if($("#modiPriceCategories").val()==''){
		alert("请选择修饰类别！");
		return false;
	}
	if($("#modiTypeTemp").val()==''){
		alert("请选择修饰类型！");
		return false;
	}
	if($("#modiPrice").val()==''){
		alert("请录入修饰价格！");
		return false;
	}
	if($("#modiPriceCategories").val()=='groupType'){
		$("#modiType").val($("#modiTypegroup5").val()+" and " +$("#modiTypeTemp").val());
	}else{
		$("#modiType").val($("#modiTypeTemp").val())
	}
	$("#modiPricefm").submit();
}

$("#modiPriceCategories").change(function(event){
	if(this.value=='groupType'){
		$("#modiPriceCategories").val('modiThreeType');
		getselectInfo();
		$("#modiPriceCategories").val('modiFiveType');
		getselectGroup();
		$("#modiPriceCategories").val('groupType');
	}else{
		getselectInfo();
	}
	
});


//下拉查询
var getselectInfo=function(){
	var productCategories = $("#modiPriceCategories").val();
	progress();
	$.ajax({
		type : "post",
		async: false,
		url : ctx+"/productManage/selectQuery",
		dataType : "json",
		data:
		{
			productCategories:productCategories
        },
		success : function(data) {
			$.messager.progress('close');
			if(data != null){
				$("#andType").hide();
        		var options_str = "";
        		var flag = "";
        		if(productCategories=='modiThreeType'){
        			flag="3'-";
        		}
        		if(productCategories=='modiFiveType'){
        			flag="5'-";
        		}
        		$("#modiTypeTemp").html("");
        		for(var i=0;i<data.length;i++){
        			options_str += "<option value=\"" +flag+ data[i].productCode + "\" >"+ flag + data[i].productCode + "</option>";
        		}
        		$("#modiTypeTemp").append(options_str);
			}
		},
		error:function(){
			$.messager.progress('close');
			alert("修饰类型加载失败!	请联系技术人员");
		}
	});
}

var getselectGroup=function(){
	var productCategories = 'modiFiveType';
	progress();
	$.ajax({
		type : "post",
		async: false,
		url : ctx+"/productManage/selectQuery",
		dataType : "json",
		data:
		{
			productCategories:productCategories
        },
		success : function(data) {
			$.messager.progress('close');
			if(data != null){
        		var options_str = "";
        		var flag="5'-";
        		$("#modiTypegroup5").html("");
        		for(var i=0;i<data.length;i++){
        			options_str += "<option value=\"" +flag+ data[i].productCode + "\" >"+ flag + data[i].productCode + "</option>";
        		}
        		$("#modiTypegroup5").append(options_str);
        		$("#andType").show();
			}
		},
		error:function(){
			$.messager.progress('close');
			alert("修饰类型加载失败!	请联系技术人员");
		}
	});
}
