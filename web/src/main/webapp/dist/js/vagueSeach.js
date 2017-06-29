$(function(){
    
    $(":text")
        .focus(function(){$(this).addClass("fouse");})  
        .blur(function(){$(this).removeClass("fouse");});

    seachChange(ctx+"/order/vagueSeachCustomer");
});
function seachChange(url){
    var seach = $("#seachCustom");
    var left = seach.offset().left;
    var top = seach.offset().top + 30;
    var list = $("#seachList");
    list.css({'left':left,'top':top});
    list.on("click",'li',seachLiSelect);
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
        setTimeout(function(){
            if(seach.val() != ''){
                $.ajax({
                    type: "POST",
                    url: url,
                    dataType: "json",
                    contentType: "application/x-www-form-urlencoded; charset=utf-8", 
                    data:{
            			seachCustom: seach.val()
                    },
                    success:function(data){
                        list.empty();
                        var li = '';
                        if(list.is(':hidden')){
                            list.show(100);
                        }
                        for(var i = 0; i < data.length; i++){
                            var id = data[i].code;
                            var text = data[i].name;
                            li += '<li id="'+id+'">'+text+'</li>';
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
function seachLiSelect(){
    var seach = $("#seachCustom");
    var list = $("#seachList");
    var id = $(this).attr("id");
    var val = $(this).text();
    seach.val(val).attr("tagId",id);
    $("#customerCode").val(id);
    list.hide(100);
};
