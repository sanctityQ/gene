$(function(){
    
    $(":seachPrimerProduct")
        .focus(function(){$(this).addClass("fouse");})  
        .blur(function(){$(this).removeClass("fouse");});

    seachPrimerProductChange("seachCustom.json");
});
function seachPrimerProductChange(url){
    var seach = $("#seachPrimerProduct");
    var left = seach.offset().left;
    var top = seach.offset().top + 30;
    var list = $("#seachPrimerProductList");
    list.css({'left':left,'top':top});
    list.on("click",'li',seachLiSelect);
    seach.bind('input',function(){
        ajaxSeach();
    }).bind('keyup',function(){
        ajaxSeach();
    });
    function ajaxSeach(){
        setTimeout(function(){
            if(seach.val() != ''){
                $.ajax({
                    type: "GET",
                    url: url,
                    dataType: "json",
                    success:function(data){
                        list.empty();
                        var li = '';
                        if(list.is(':hidden')){
                            list.show(100);
                        }
                        for(var i = 0; i < data.length; i++){
                            var id = data[i].id;
                            var text = data[i].text;
                            li += '<li id="'+id+'">'+text+'</li>';
                        };
                        list.append(li);
                    }
                });
            }else{
                list.hide(100);
            }
        },500)
    }
}
function seachLiSelect(){
    var seach = $("#seachPrimerProduct");
    var list = $("#seachPrimerProductList");
    var id = $(this).attr("id");
    var val = $(this).text();
    seach.val(val).attr("tagId",id);
    list.hide(100);
}
//查询方法
function searchLabel(){
	
	var boardNo = $.trim($('#boardNoOrProductNo').val());
	var noType = $.trim($('#noType').val());
	
	if(boardNo == ""){
		alert("请输入板号或生产编号。");
		return;
	}
	
    $.ajax({
        type:'post',
        url : "/gene/print/printLabelQuery",
        dataType:'json',
		data:{
			boardNo: boardNo,
			noType:noType
        },
        success:function(data){
            var downList = $('#downList');
            var html = '<ul class="xls_list"><li class="tip"><i class="icon-info-sign"></i>选择下列公司下载生产标签。</li>';
            for(var i = 0; i < data.length; i++){
                var name = data[i].customerName;
                var src = data[i].fileName;
                html += '<li><i class="icon-paper-clip"></i><a href="'+src+'" >'+name+'</a></li>';
            }
            html += '</ul>';
            downList.append(html);
        },
		error:function(){
			alert("无法获取信息");
		}
    })
}