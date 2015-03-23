<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <script type="text/javascript">



    </script>
</head>
<body>
<div class="tools">
    <table>
        <tr>
            <td align="right">姓名：</td>
            <td>
                <input type="text" class="inp_text" style="width:80%" id="userName" name="userName"
                       value="${userName}"/>
            </td>
            <td align="right">部门：</td>
            <td>
                <input type="text" id="comCode" name="comCode" value="${comCode}"/>
            </td>
            <td>
                <button type="button" class="btn" onclick="rpcData();">查询</button>
            </td>
            <td align="right">
                <button type="button" class="btn btn-primary"
                        onclick="javascript:void(prepareAddUser());">增加业务员
                </button>
                <button type="button" class="btn btn-primary submit" onclick="batchDelete();">
                    批量删除
                </button>
            </td>
        </tr>
    </table>
</div>
<table id="userList" class="easyui-datagrid"
       data-options="striped:true,method:'get',pagination:true,fitColumns:true">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true"></th>
        <th data-options="field:'code',width:80,sortable:true">登陆名</th>
        <th data-options="field:'name',width:80,sortable:true">姓名</th>
        <th data-options="field:'comCode',width:80,sortable:true">机构名称</th>
        <th data-options="field:'mobile',width:80,sortable:true">手机号</th>
        <th data-options="field:'email',width:80,sortable:true">Email</th>
        <th data-options="field:'staffFlag',width:80,sortable:true">是否本公司</th>
        <th data-options="field:'_operate',align:'center',formatter:formatOper">操作</th>
    </tr>
    </thead>
</table>
<script>
    function formatOper(val, row, index) {
        return '&nbsp;<a href="javascript:;" onclick="view(\'#userList\',\'' + index + '\')"><i class="icon-book"></i>查看</a>&nbsp; ' +
               '<span class="gray">|</span> &nbsp;<a href="javascript:;"  onclick="update(\'#userList\',\'' + index + '\')"' +
               '<i class="icon-pencil"></i>修改</a>&nbsp; <span class="gray">|</span> &nbsp' +
               ';<a href="javascript:;" onclick="deleteUser(\'#userList\',\'' + index + '\')"><i class="icon-trash"></i>删除</a>&nbsp;';
    }

</script>
<script src="${ctx}/views/user/js/user.js?version=1" type="application/javascript"></script>
<script language="JavaScript" type="application/javascript">
    $(function () {
        var data = ${data};
        userList(data);
    });
</script>
</body>
</html>