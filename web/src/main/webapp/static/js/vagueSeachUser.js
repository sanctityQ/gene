$(function(){
    $("#seachUserName")
        .focus(function(){$(this).addClass("fouse");})  
        .blur(function(){$(this).removeClass("fouse");});
    
    seachUserChange();
});
function seachUserChange(){
    var seach = $("#seachUserName");
    var left = seach.offset().left;
    var top = seach.offset().top + 30;
    var list = $("#seachUserList");
    list.css({'left':left,'top':top});
    list.on("click",'li',seachUserSelect);
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
    	$("#vagueUserCode").val("");//清空用户代码
        setTimeout(function(){
            if(seach.val() != ''){
                $.ajax({
                    type: "post",
                    url: "/gene/user/vagueSeachUser",
                    dataType: "json",
            		data:{
            			userName: seach.val()
                    },
                    success:function(data){
                        list.empty();
                        var li = '';
                        if(list.is(':hidden')){
                            list.show(100);
                        }
                        for(var i = 0; i < data.length; i++){
                            var id   = data[i].id;
                            var code = data[i].code;
                            var name = data[i].name;
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
function seachUserSelect(){
    var seach = $("#seachUserName");
    var vagueUserCode = $("#vagueUserCode");
    var list = $("#seachUserList");
    var id = $(this).attr("id");
    var code = $(this).attr("code");
    var val = $(this).text();
    seach.val(val).attr("tagId",code);
    vagueUserCode.val(code)
    list.hide(100);
};
