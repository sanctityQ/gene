$(function(){
    
    $(":text")
        .focus(function(){$(this).addClass("fouse");})  
        .blur(function(){$(this).removeClass("fouse");});
    
    seachChange("seachCustom.json");
});
function seachChange(url){
    var seach = $("#seachCustom");
    var left = seach.offset().left;
    var top = seach.offset().top + 30;
    var list = $("#seachList");
    list.css({'left':left,'top':top});
    list.on("click",'li',seachLiSelect);
    seach.bind('input propertychange', function() {
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
    });
}
function seachLiSelect(){
    var seach = $("#seachCustom");
    var list = $("#seachList");
    var id = $(this).attr("id");
    var val = $(this).text();
    seach.val(val).attr("tagId",id);
    list.hide(100);
}