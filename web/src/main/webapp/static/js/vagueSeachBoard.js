$(function(){

    $("#boardNo")
        .focus(function(){$(this).addClass("fouse");})  
        .blur(function(){$(this).removeClass("fouse");});
    
    seachBoardChange();
});
function seachBoardChange(){
    var seach = $("#boardNo");
    var left = seach.offset().left;
    var top = seach.offset().top + 30;
    var list = $("#seachBoardList");
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
                    type: "post",
                    url: "/synthesis/vagueSeachBoard",
                    dataType: "json",
            		data:{
            			boardNo: seach.val(),
            			operationType:$("#operationType").val()
                    },
                    success:function(data){
                        list.empty();
                        var li = '';
                        if(list.is(':hidden')){
                            list.show(100);
                        }
                        for(var i = 0; i < data.length; i++){
                            var id = data[i].id;
                            var boardNo = data[i].boardNo;
                            li += '<li id="'+id+'">'+boardNo+'</li>';
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
    var seach = $("#boardNo");
    var list = $("#seachBoardList");
    var id = $(this).attr("id");
    var val = $(this).text();
    seach.val(val).attr("tagId",id);
    list.hide(100);
};
