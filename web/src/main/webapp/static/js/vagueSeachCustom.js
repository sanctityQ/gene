$(function(){

    $("#seachCustom")
        .focus(function(){$(this).addClass("fouse");})  
        .blur(function(){$(this).removeClass("fouse");});
    
    seachCustomChange();
});
function seachCustomChange(){
    var seach = $("#seachCustom");
    var left = seach.offset().left;
    var top = seach.offset().top + 30;
    var list = $("#seachCustomList");
    list.css({'left':left,'top':top});
    list.on("click",'li',seachCustomSelect);
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
                    type: "post",
                    url: "/gene/customer/vagueSeachCustomer",
                    dataType: "json",
            		data:{
            			customercode: seach.val()
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
function seachCustomSelect(){
    var seach = $("#seachCustom");
    var customerid = $("#customerid");
    var list = $("#seachCustomList");
    var id = $(this).attr("id");
    var code = $(this).attr("code");
    var val = $(this).text();
    seach.val(val).attr("tagId",code);
    customerid.val(id)
    list.hide(100);
};
