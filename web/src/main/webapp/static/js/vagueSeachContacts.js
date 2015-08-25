$(function(){

    $("#seachContacts")
        .focus(function(){$(this).addClass("fouse");})  
        .blur(function(){$(this).removeClass("fouse");});
    
    seachContactsChange();
});
function seachContactsChange(){
	
    var seach = $("#seachContacts");
    var left = seach.offset().left;
    var top = seach.offset().top + 30;
    var list = $("#seachContactsList");
    list.css({'left':left,'top':top});
    list.on("click",'li',seachContactsSelect);
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
    	
    	//重新模糊查询时清空
    	$("#contactsid").val("");
        
    	setTimeout(function(){
            if(seach.val() != ''){
                $.ajax({
                    type: "post",
                    url: "/gene/customerContacts/vagueSeachContacts",
                    dataType: "json",
            		data:{
            			contactsName: seach.val(),
            			customerid: $("#customerid").val()
                    },
                    success:function(data){
                        list.empty();
                        var li = '';
                        if(list.is(':hidden')){
                            list.show(100);
                        }
                        for(var i = 0; i < data.length; i++){
                            var id   = data[i].id;
                            var name = data[i].name;
                            li += '<li id="'+id+'">'+name+'</li>';
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
function seachContactsSelect(){
    var seach = $("#seachContacts");
    var contactsid = $("#contactsid");
    var list = $("#seachContactsList");
    var val = $(this).text();
    var id = $(this).attr("id");
    seach.val(val);
    contactsid.val(id)
    list.hide(100);
};