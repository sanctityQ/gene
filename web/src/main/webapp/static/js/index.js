
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
    var tab = $('#tabList');
    var theTab = tab.tabs('getSelected'); // get selected panel
    
     
    // call 'refresh' method for tab panel to update its content
    //var tab = $('#tt').tabs('getSelected'); // get selected panel
    theTab.panel('refresh', url);
}

var errors = new Array();
var ajaxFileUpload=function() {
    $.ajaxFileUpload
    (
        {
            url: "order/upload", //用于文件上传的服务器端请求地址
            secureuri: false, //是否需要安全协议，一般设置为false
            data: {customerCode: $("#customerCode").val()},
            fileElementId:"upload",
            dataType: 'JSON', //返回值类型 一般设置为JSON
            success: function (data,status)  //服务器成功响应处理函数
            {
            	
            	if(data != null){
            		//此处没返回两个不同类型的对象处理。待优化
            		if(data.orderNo!=undefined){
            			goToPage('views/order/orderInfo.jsp?orderNo='+data.orderNo);
            			orderNo = data.orderNo;
	            	}else{
	            		errors=data;
	            		goToPage('views/order/importError.jsp');
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
