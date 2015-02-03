$(function(){

    $("#seachUnitName")
        .focus(function(){$(this).addClass("fouse");})  
        .blur(function(){$(this).removeClass("fouse");});
    
    seachUnitNameChange();
});
function seachUnitNameChange(){
    var seach = $("#seachUnitName");
    var left = seach.offset().left;
    var top = seach.offset().top + 30;
    var list = $("#seachUnitNameList");
    list.css({'left':left,'top':top});
    list.on("click",'li',seachUnitNameSelect);
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
                    url: "/gene/customer/vagueSeachUnit",
                    dataType: "json",
            		data:{
            			unitName: seach.val()
                    },
                    success:function(data){
                        list.empty();
                        var li = '';
                        if(list.is(':hidden')){
                            list.show(100);
                        }
                        for(var i = 0; i < data.length; i++){
                            var code = data[i].code;
                            var name = data[i].name;
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
function seachUnitNameSelect(){
    var seach = $("#seachUnitName");
    var list = $("#seachUnitNameList");
    var id = $(this).attr("id");
    var val = $(this).text();
    seach.val(val).attr("tagId",id);
    $("#customercode").val(id);
    list.hide(100);
}