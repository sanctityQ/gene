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
    	
    	//重新模糊查询时清空
    	$("#customerid").val("");
    	$("#customerFlag").val("");
    	$("#customerCode").val("");
    	$("#contactsid").val("");
    	$("#seachContacts").val("");
    	
    	setTimeout(function(){
            if(seach.val() != ''){
                $.ajax({
                    type: "post",
                    url: "/customer/vagueSeachCustomer",
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
                            var custFlag = data[i].customerFlag;
                            var leaderName   = data[i].leaderName;
                            var handlerName  = data[i].handlerName;
                            var invoiceTitle = data[i].invoiceTitle;
                            var payWays      = data[i].payWays;
                            var address      = data[i].address;
                            var fax          = data[i].fax;
                            var phoneNo      = data[i].phoneNo;
                            var email        = data[i].email;
                            var webSite      = data[i].webSite;
                            
                            li += '<li id="'+id+'" custFlag="'+custFlag+'" code="'+code+'" leaderName="'+leaderName+'" handlerName="'+handlerName+'" invoiceTitle="'+invoiceTitle+'" payWays="'+payWays+'" address="'+address+'" fax="'+fax+'" phoneNo="'+phoneNo+'" email="'+email+'" webSite="'+webSite+'" >'+name+'</li>';
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
    var customerFlag = $("#customerFlag");
    var customerCode = $("#customerCode");
    var list = $("#seachCustomList");
    
    var customer_leaderName = $("#customer_leaderName");
    var customer_handlerName = $("#customer_handlerName");
    var customer_invoiceTitle = $("#customer_invoiceTitle");
    var customer_payWays = $("#customer_payWays");
    var customer_address = $("#customer_address");
    var customer_fax = $("#customer_fax");
    var customer_phoneNo = $("#customer_phoneNo");
    var customer_email = $("#customer_email");
    var customer_webSite = $("#customer_webSite");
    
    var val = $(this).text();
    var id = $(this).attr("id");
    var code = $(this).attr("code");
    var custFlag = $(this).attr("custFlag");
    var leaderName = $(this).attr("leaderName");
    var handlerName = $(this).attr("handlerName");
    var invoiceTitle = $(this).attr("invoiceTitle");
    var payWays = $(this).attr("payWays");
    var address = $(this).attr("address");
    var fax = $(this).attr("fax");
    var phoneNo = $(this).attr("phoneNo");
    var email = $(this).attr("email");
    var webSite = $(this).attr("webSite");
    
    seach.val(val).attr("tagId",code);
    customerid.val(id)
    customerFlag.val(custFlag);
    customerCode.val(code);
    
    //修改客户信息页面赋值
    if(typeof($("#customer_leaderName").val()) != 'undefind'){
    	customer_leaderName.val(leaderName);
    	customer_handlerName.val(handlerName);
    	customer_invoiceTitle.val(invoiceTitle);
    	customer_payWays.val(payWays);
    	customer_address.val(address);
    	customer_fax.val(fax);
    	customer_phoneNo.val(phoneNo);
    	customer_email.val(email);
    	customer_webSite.val(webSite);
    }
    
    list.hide(100);

    //如果需要赋总公司值
    if(typeof($("#needTopCom").val()) != 'undefind'){
    	addTopComInfo( custFlag );
    }
    
};

function addTopComInfo( custFlag ){

    if(custFlag =='0' && $("#operateUserComLevel").val() == '1'){//梓熙  & 操作员是总公司
    	
    	$("#companyId").val("");
    	$("#companyComCode").val("");
    	$("#seachCompany").val("");
    	$("#tdComCode").show();
    	$("#tdComName").show();
    	$("#tdComCodeHidden").hide();
    }else{
    	$("#companyId").val("");
    	$("#companyComCode").val("");
    	$("#seachCompany").val("");
    	$("#tdComCode").hide();
    	$("#tdComName").hide();
    	$("#tdComCodeHidden").show();
    }
	
};	

