$(function(){
    $("#seachOrder")
        .focus(function(){$(this).addClass("fouse");})  
        .blur(function(){$(this).removeClass("fouse");});
    
    seachOrderChange();
});
function seachOrderChange(){
    var seach = $("#seachOrder");
    var left = seach.offset().left;
    var top = seach.offset().top + 30;
    var list = $("#seachOrderList");
    list.css({'left':left,'top':top});
    list.on("click",'li',seachOrderSelect);
    seach.bind('input',function(){
        ajaxSeach();
    }).bind('keyup',function(){
        ajaxSeach();
    });
    function ajaxSeach(){
        setTimeout(function(){
            if(seach.val() != ''){
                $.ajax({
                    type: "post",
                    url: "/gene/order/vagueSeachOrder",
                    dataType: "json",
            		data:{
            			orderNo: seach.val()
                    },
                    success:function(data){
                        list.empty();
                        var li = '';
                        if(list.is(':hidden')){
                            list.show(100);
                        }
                        for(var i = 0; i < data.length; i++){
                            var code = data[i].orderNo;
                            var name = data[i].orderNo;
                            li += '<li id="'+code+'">'+name+'</li>';
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
function seachOrderSelect(){
    var seach = $("#seachOrder");
    var list = $("#seachOrderList");
    var id = $(this).attr("id");
    var val = $(this).text();
    seach.val(val);
    $("#seachOrder").val(id);
    list.hide(100);
}