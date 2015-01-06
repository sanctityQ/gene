
function toggleWest(e){
    var west = $(e);

    if(west.hasClass("hide")){
        var lws = $('body').layout('panel', 'west');
        lws.panel('expand');
        var wd = lws.width();
        $('body').layout('panel', 'center').panel('resize',{'left':wd,'width':$('body').width()-wd});
        $(e).css("left",188).removeClass("hide");
    }else{
        $('body').layout('panel', 'west').panel('collapse');
        $('body').layout('panel', 'center').panel('resize',{'left':0,'width':$('body').width()});
        $(e).css("left", 0).addClass("hide");
    }
}

function goToPage(url){
    var tab = $('#tabList');
    var theTab = tab.tabs('getSelected'); // get selected panel
    
     
    // call 'refresh' method for tab panel to update its content
    //var tab = $('#tt').tabs('getSelected'); // get selected panel
    theTab.panel('refresh', url);
}