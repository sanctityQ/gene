var bigIndex = undefined,bigToSmall = $('#bigToSmall');
function userQuery(){
	var userName = $.trim($('#userName').val());
	var comCode = $.trim($('#comCode').val());
	
	if(userName == "" && comCode == ""){
		alert("请输入业务员姓名或选择机构。");
		return;
	}
    else {
    	 document.form.submit();
    }
    
}

function prepareAddUser(){
	goToPage('views/user/addUser.jsp');
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
        else if ($.trim($("#password").val()) == "") {
            alert("用户密码必须填写！");
            return false;
        }
        else {
            //document.form.action = '';
            document.form.submit();
        }
        
    });
});


