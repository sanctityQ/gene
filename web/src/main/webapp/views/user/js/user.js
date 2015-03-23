function prepareAddUser(){
    goToPage(ctx+'/user/preAdd');
}


var view = function (id,index){
    var row = $(id).datagrid('getData').rows[index];
    var id = row.id;
    goToPage(ctx+'/user/view/'+id+'?op=view');
}

var update = function (id,index){
    var row = $(id).datagrid('getData').rows[index];
    var id = row.id;
    goToPage(ctx+'/user/view/'+id+'?op=update');
}

var deleteUser = function (id, index) {
    var row = $(id).datagrid('getData').rows[index];
    var idArray = [row.id];
    deleteRpc(idArray);
}


function deleteRpc(rpcDatas){
    $.messager.confirm('系统消息', '确认删除选中的数据?', function (result) {
        if (result) {
            progress();
            $.ajax({
                       type: "post",
                       url: ctx + "/user/delete",
                       dataType: "html",
                       data: {"id": rpcDatas},
                       success: function (data) {
                           $.messager.progress('close');
                           if (data == "true") {
                               $.messager.alert('结果', '删除用户成功', 'info');
                           } else {
                               $.messager.alert('结果', '删除用户失败', 'error');
                           }
                           rpcData();
                       },
                       error: function () {
                           $.messager.progress('close');
                           $.messager.alert('删除结果', '失败', 'error');
                       }
                   });
        }
    });
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
    showData(data);

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
                   showData(data);
               },
               error:function(){
                   $.messager.progress('close');
                   alert("无法获取信息");
               }
           });
}

function showData(data){
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
}


function batchDelete() {

    var checkedIds = new Array();
    var selectFlag = false;
    $("input[name='id']:checked").each(function (index, element) {
        checkedIds.push(element.value);
        selectFlag = true;
    });
    if (!selectFlag) {
        $.messager.alert('系统信息','请选择要删除的业务员','info');
        return false;
    }
    deleteRpc(checkedIds);
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