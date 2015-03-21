<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title></title>
</head>
<body>
<form name="form" action="${ctx}/user/addUser" method="post">
    <div class="page_padding">
        <div class="content_box info margin_btoom">
            <h2>用户信息</h2>
            <table width="100%" class="order_info">
                <input class="inp_text" type="hidden" name="id" value="${id}" style="width:150px" />
                <tr>
                    <td colspan="4" height="10"></td>
                </tr>
                <tr>
                    <td align="right">登陆名:</td>
                    <td colspan="3"> <input class="inp_text easyui-validatebox" type="text" id="code" name="user.code" value="${user.code}" style="width:340px" data-options="required:true,missingMessage:'必录项'"/></td>

                </tr>
                <tr>
                    <td align="right">密码:</td>
                    <td> <input class="inp_text" type="password" id="plainPassword" name="user.plainPassword" value="${user.plainPassword}" style="width: 80%"/></td>
                    <td align="right">确认密码:</td>
                    <td>
                        <input class="inp_text" type="password" id="plainPasswordConfirm" value="${user.plainPassword}" style="width: 80%"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">姓名:</td>
                    <td><input class="inp_text" type="text" id="name" name="user.name" value="${user.name}" style="width:150px"/></td>
                    <td align="right">部门:</td>
                    <td>
                        <input class="inp_text" type="text" id="comCode" name="user.company.comCode" value="${user.comCode}" style="width: 80%"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">手机号:</td>
                    <td><input class="inp_text" type="text" id="mobile" name="user.mobile" required="required" style="width: 150px;"></td>
                    <td align="right">邮箱:</td>
                    <td><input class="inp_text easyui-validatebox" id="email" type="text" name="user.email" value="${user.email}" style="width: 80%"
                               data-options="validType: 'email',invalidMessage:'email格式不正确'"/></td>
                </tr>
                <tr>
                    <td align="right">是否本公司人员:</td>
                    <td colspan="3">
                        <label><input type="radio" name="user.staffFlag" value="1"/> 是</label>&nbsp;&nbsp;
                        <label><input type="radio" name="user.staffFlag" value="0"/> 否</label>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" height="10"></td>
                </tr>
            </table>
        </div>
        <div class="tools_bar">
            <button type="button" class="btn" onclick="goToPage('${ctx}/user/prepareManageQuery');">取 消</button>
            <button type="button" id="addUserBtn" class="btn btn-primary">保 存</button>
        </div>
    </div>
</form>
<script src="${ctx}/views/user/js/user.js"></script>
</body>
</html>