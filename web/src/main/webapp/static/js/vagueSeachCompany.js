$(function(){

    $("#seachCompany")
        .focus(function(){$(this).addClass("fouse");})  
        .blur(function(){$(this).removeClass("fouse");});
    
    seachCompanyChange();
});
function seachCompanyChange(){
    var seach = $("#seachCompany");
    var left = seach.offset().left;
    var top = seach.offset().top + 30;
    var list = $("#seachCompanyList");
    list.css({'left':left,'top':top});
    list.on("click",'li',seachCompanySelect);
    seach.bind('keydown',function(event){
        var select = $('li.selected',list);
        if(event.keyCode == 40){
            if(select.length){
                if(select.index()!=$('li',list).length-1){
                    select.removeClass('selected').next().addClass('selected');
                }
            }else{
                $('li:first',list).addClass('selected');
            }
        }else if(event.keyCode == 38){
            if(select.length){
                if(select.index()!=0){
                    select.removeClass('selected').prev().addClass('selected');
                }
            }else{
                $('li:first',list).addClass('selected');
            }
        }else if(event.keyCode == 13){
            var val = select.text();
            select.trigger('click');
        }else{
        ajaxSeach();
        }
    });
    function ajaxSeach(){
    	
    	//重新模糊查询时清空
    	$("#companyId").val("");
    	$("#companyComCode").val("");
        
    	setTimeout(function(){
            if(seach.val() != ''){
                $.ajax({
                    type: "post",
                    url: "/gene/company/vagueSeachCompany",
                    dataType: "json",
            		data:{
            			comName: seach.val()
                    },
                    success:function(data){
                        list.empty();
                        var li = '';
                        if(list.is(':hidden')){
                            list.show(100);
                        }
                        for(var i = 0; i < data.length; i++){
                            var id   = data[i].id;
                            var code = data[i].comCode;
                            var name = data[i].comName;
                            li += '<li id="'+id+'" code="'+code+'">'+name+'</li>';
                        };
                        list.append(li);
                    }
                });
            }else{
                list.hide(100);
            }
        },500);
    };
}
function seachCompanySelect(){
    var seach = $("#seachCompany");
    var companyId = $("#companyId");
    var comCode = $("#companyComCode");
    var list = $("#seachCompanyList");
    var val = $(this).text();
    var id = $(this).attr("id");
    var code = $(this).attr("code");
    seach.val(val).attr("tagId",code);
    companyId.val(id)
    comCode.val(code)
    list.hide(100);
};
