function toggleWest(e){
    var west = $(e);
    if(west.hasClass("hide")){
        var lws = $('body').layout('panel', 'west');
        lws.panel('expand');
        var wd = lws.width();
        $('body').layout('panel', 'center').panel('resize',{'left':wd,'width':$('body').width()-wd});
        $(e).css("left",188).removeClass("hide");
    }else{
        $('body').layout('panel', 'west').panel('collapse');
        $('body').layout('panel', 'center').panel('resize',{'left':0,'width':$('body').width()});
        $(e).css("left", 0).addClass("hide");
    }
}
function goToPage(url){
    /*var tab = $('#tabList');
    var theTab = tab.tabs('getSelected');
    setTimeout(function(){
        theTab.panel('refresh', url);
    },100)*/
    window.location.href=url;
}
function delet(grid,index){
    var table = $(grid);
    var tr = table.datagrid('selectRow',index);
    var row = table.datagrid('getSelections');
    $.messager.confirm('系统消息','确认删除选中的数据?',function(ok){
        alert(row[0].productid)
        table.datagrid('deleteRow',index);
        table.datagrid('reload');
    });
}
function deleteRows(table,index){
    var grid = $("#"+table);
    var rows = grid.datagrid('getSelections');
    var arr=[];
    if(rows.length==0)
    {
        $.messager.alert('系统提示','您还没有选择任何数据项，请选择要删除的数据。');
    }else
    {
        $.messager.confirm('系统提示','确认选中的删除?',function(ok){
            if(ok){
                for(var i=0;i<rows.length;i++)
                {
                    arr.push(rows[i].productid);
                }
                alert(arr);
                grid.datagrid('reload');
            };
        });
    }
}

var errors = new Array();
var ajaxFileUpload=function() {
    $.ajaxFileUpload
    (
        {
            url: "upload", //用于文件上传的服务器端请求地址
            secureuri: false, //是否需要安全协议，一般设置为false
            data: {customerCode: $("#customerCode").val()},
            fileElementId:"upload",
            dataType: 'JSON', //返回值类型 一般设置为JSON
            success: function (data,status)  //服务器成功响应处理函数
            {
            	
            	if(data != null){
            		//此处没返回两个不同类型的对象处理。待优化
            		if(data.orderNo!=undefined){
            			goToPage(ctx+'/views/order/orderInfo.jsp?orderNo='+data.orderNo);
            			orderNo = data.orderNo;
	            	}else{
	            		errors=data;
	            		goToPage(ctx+'/views/order/importError.jsp');
	            	}
            	}
            },
            error: function (data, status, e)//服务器响应失败处理函数
            {
            	alert("无法获取数据信息!");
            }
        }
    )
    //return false;
}
