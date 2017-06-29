$(function(){

    $("#boardNoOrProductNo")
        .focus(function(){$(this).addClass("fouse");})  
        .blur(function(){$(this).removeClass("fouse");});
    
    seachBoardNoOrProductNo();
});
function seachBoardNoOrProductNo(){
    var seach = $("#boardNoOrProductNo");
    var left = seach.offset().left;
    var top = seach.offset().top + 30;
    var list = $("#seachBoardList");
    list.css({'left':left,'top':top});
    list.on("click",'li',seachNoSelect);
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
                    url: "/gene/synthesis/vagueSeachBoardNoOrProductNo",
                    dataType: "json",
            		data:{
            			boardNo: seach.val()
                    },
                    success:function(data){
                        list.empty();
                        var li = '';
                        if(list.is(':hidden')){
                            list.show(100);
                        }
                        for(var i = 0; i < data.length; i++){
                            var type = data[i].type;
                            var no = data[i].boardNo;
                            li += '<li id="'+type+'">'+no+'</li>';
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
function seachNoSelect(){
    var seach = $("#boardNoOrProductNo");
    var noType = $("#noType");
    var list = $("#seachBoardList");
    var type = $(this).attr("id");
    var val = $(this).text();
    seach.val(val);
    noType.val(type);
    list.hide(100);
}