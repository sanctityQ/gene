$(function(){
    setMenuHeight();
    $('#scllor').perfectScrollbar();

    $(window).resize(function(){
        setTimeout(function(){
            var height = setMenuHeight();
            $('#scllor').height(height).perfectScrollbar('update');
        },200)
        
    });

    liOnClick();    
});
function setMenuHeight(){
    var height = parseInt($('#westLayout').height());
    if(!height){
        height = $("div.layout-panel-west > div.panel-body").height();
    }
    $("#scllor").height(height);
    return height;
};
function liOnClick(){
    var accordion = $("#scllor div.easyui-accordion");
    accordion.on("click","li",liClick);
}
function liClick(){
    var li = $(this);
    var url = li.attr("url");
    var tab = $("#tabList");
    var text = li.text();
    var id = li.attr("id") + "Cont";
    $("#scllor li.selected").removeClass("selected");
    li.addClass("selected");
    if($("#"+id).length){
        var index = $("#"+id).parent().index();
        tab.tabs('select',index);
    }else{
        if(url){
            var content = '<iframe id="#frameBody" frameborder="0"  src="'+url+'" style="width:100%;height:99%;"></iframe>';
            tab.tabs('add',{
                id:id,
                title: text,
                content:content,
                closable: true
            });
        }
    };    
}