function prepareAddUser(){
    goToPage(ctx+'/views/user/addUser.jsp');
}


function addUserSubmit() {
    var staffFlag = $('input:radio[name="user.staffFlag"]:checked').val();
    if (staffFlag == "0" && $.trim($("#customerid").val()) == "") {
        alert("不是本公司用户，必须选择外部客户信息数据!");
        return false;
    }
    if ($.trim($("#code").val()) == "") {
        alert("用户工号必须填写！");
        return false;
    }
    else if ($.trim($("#name").val()) == "") {
        alert("用户姓名必须填写！");
        return false;
    }
    else if ($.trim($("#plainPassword").val()) == "") {
        alert("用户密码必须填写！");
        return false;
    }
    if ($("#plainPassword").val() != $('#plainPasswordConfirm').val()) {
        alert("用户密码必须填写！");
    }

    document.form.submit();
}


function userList(data){
    var dg = $('#userList');

    var pager = dg.datagrid('getPager');
    pager.pagination('loading', true);
    var opts = dg.datagrid('options');
    //dg.datagrid(data);
    $('#userList').datagrid("loadData", data);


    pager.pagination({
                         total: data.total,
                         pageSize: data.pageSize,
                         pageNumber: data.pageNumber,
                         onSelectPage: function (pageNum, pageSize) {
                             opts.pageNumber = pageNum;
                             opts.pageSize = pageSize;

                             pager.pagination('refresh', {
                                 pageNumber: pageNum,
                                 pageSize: pageSize
                             });
                             rpcData();
                         }
                     });

}

function rpcData() {
    progress();
    var gridOpts = $('#userList').datagrid('getPager').data("pagination").options;
    var userName = $("#userName").val();
    var comCode = $("#comCode").val();
    $.ajax({
               type : "get",
               url : ctx+"/user/list",
               dataType : "json",
               data:
               {
                   userName:userName,
                   comCode:comCode,
                   pageNo: gridOpts.pageNumber - 1,
                   pageSize: gridOpts.pageSize
               },
               success: function (data) {
                   $.messager.progress('close');
                   if (data != null) {
                       var resultData = data.rows;
                       for (var i = 0; i < resultData.length; i++) {
                           if (resultData[i].staffFlag) {
                               resultData[i].staffFlag = '是';
                           } else {
                               resultData[i].staffFlag = '否';
                           }
                       }
                       $('#userList').datagrid("loadData", data);
                   }
               },
               error:function(){
                   $.messager.progress('close');
                   alert("无法获取信息");
               }
           });
}


$(function () {


    $("#addUserBtn").click(function () {

        var staffFlag=$('input:radio[name="user.staffFlag"]:checked').val();
        if(staffFlag == "0" && $.trim($("#customerid").val()) == ""){
            alert("不是本公司用户，必须选择外部客户信息数据!");
            return false;
        }
        if ($.trim($("#code").val()) == "") {
            alert("用户工号必须填写！");
            return false;
        }
        else if ($.trim($("#name").val()) == "") {
            alert("用户姓名必须填写！");
            return false;
        }
        else if ($.trim($("#plainPassword").val()) == "") {
            alert("用户密码必须填写！");
            return false;
        }

        if ($("#plainPassword").val() != $('#plainPasswordConfirm').val()) {
            alert("用户密码必须填写！");
        }

        document.form.submit();


    });

});