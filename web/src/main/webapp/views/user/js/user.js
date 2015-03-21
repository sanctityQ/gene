function userQuery(){
    var userName = $.trim($('#userName').val());
    var comCode = $.trim($('#comCode').val());

    if(userName == "" && comCode == ""){
        alert("请输入业务员姓名或选择机构。");
        return;
    }

    $('#userList').datagrid('load', {
        userName:$('#userName').val(),
        comCode:$('#comCode').val()
    });
}

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
    //var opts = dg.datagrid('options');


    var pager = dg.datagrid('getPager');

    //var pagination = pager.pagination({
    //                                      pageSize: 20,
    //                                      onSelectPage: function (pageNum, pageSize) {
    //                                          opts.pageNumber = pageNum;
    //                                          opts.pageSize = pageSize;
    //
    //                                          pager.pagination('refresh', {
    //                                              pageNumber: pageNum,
    //                                              pageSize: pageSize
    //                                          });
    //                                          //getOrderInfo();
    //                                      }
    //                                  });
    pager.pagination('loading', true);
    dg.datagrid(data);

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

    //var dg = $('#userList');
    //var opts = dg.datagrid('options');
    //var pager = dg.datagrid('getPager');
    //
    //var pagination = pager.pagination({
    //    pageSize:20,
    //    onSelectPage:function(pageNum, pageSize){
    //        opts.pageNumber = pageNum;
    //        opts.pageSize = pageSize;
    //
    //        pager.pagination('refresh',{
    //            pageNumber:pageNum,
    //            pageSize:pageSize
    //        });
    //        //getOrderInfo();
    //    }
    //});
    //
    //pager.pagination('loading',true);

    //alert(pager.pagination('loading',true));
});