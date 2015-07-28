<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title></title>
</head>
<%
ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
String operateUserComID = user.getUser().getCompany().getId()+"";
String operateUserComLevel = user.getUser().getCompany().getComLevel();
String operateUserComCode = user.getUser().getCompany().getComCode();
String operateUserComName = user.getUser().getCompany().getComName();
%>
<body>
<form id="userForm" name="form" action="${ctx}/user/register" method="post">
    <div class="page_padding">
        <div class="content_box info margin_btoom">
            <h2>用户信息</h2>
            <table width="100%" class="order_info">
                <input type="hidden" id="operateUserComID" name="operateUserComID" value="<%=operateUserComID %>"/>
                <input type="hidden" id="operateUserComLevel" name="operateUserComLevel" value="<%=operateUserComLevel %>"/>
                <input type="hidden" id="operateUserComCode" name="operateUserComCode" value="<%=operateUserComCode %>"/>
                <input type="hidden" id="operateUserComName" name="operateUserComName" value="<%=operateUserComName %>"/>
              
                <input type="hidden" id="userid" name="user.id" value="${id}"/>
                <input type="hidden" id="needTopCom" name="needTopCom" value=""/>
                <tr>
                    <td colspan="4" height="10"></td>
                </tr>
                <tr>
                    <td align="right">工号:</td>
                    <td> <input class="inp_text easyui-validatebox" type="text" id="code" name="user.code" value="${user.code}" style="width:240px" data-options="required:true,missingMessage:'必录项'"/></td>
                    <td align="right">姓名:</td>
                    <td><input class="inp_text" type="text" id="name" name="user.name" value="${user.name}" style="width:240px"/></td>
                </tr>
                <tr>
                    <td align="right">密码:</td>
                    <td> <input class="inp_text" type="password" id="plainPassword" name="user.plainPassword" value="${user.plainPassword}" style="width:240px"/></td>
                    <td align="right">确认密码:</td>
                    <td>
                        <input class="inp_text" type="password" id="plainPasswordConfirm" value="${user.plainPassword}" style="width:240px"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">用户标志:</td>
                    <td>
                        <label><input type="radio" name="user.userFlag" value="2"/> 使用者</label>&nbsp;&nbsp;
                        <label><input type="radio" name="user.userFlag" value="1"/> 管理层</label>&nbsp;&nbsp;
                        <label><input type="radio" name="user.userFlag" value="0"/> 系统管理员</label>
                    </td>
                    <td align="right">归属公司名称:</td>
					<td>
					    <input type="hidden" id="customerid" name="user.customer.id" value="${user.customer.id}"/>
					    <input type="hidden" id="customerFlag" name="user.customer.customerFlag" value="${user.customer.customerFlag}"/>
					    <input class="inp_text" type="text" autocomplete="off" id="seachCustom" name="user.customer.name" value="${user.customer.name}" onblur="clearCustomerId()" style="width: 240px" />
					    <ul id="seachCustomList"></ul>
					</td>
               </tr>
                <tr>
                    <td id="tdComCodeHidden" colspan="2" style="display:none"></td>
                    <td align="right" id="tdComCode">梓熙总/分公司机构名称:</td>
                    <td id="tdComName">
					    <input type="hidden" id="companyId" name="user.company.id" value="${user.company.id}"/>
					    <input type="hidden" id="companyComCode" name="user.company.comCode" value="${user.company.comCode}"/>
                        <input class="inp_text" type="text" id="seachCompany" name="user.company.comName" value="${user.company.comName}" style="width:240px"/>
                        <ul id="seachCompanyList"></ul>
                    </td>
                    <td align="right">手机号:</td>
                    <td><input class="inp_text" type="text" id="mobile" name="user.mobile" required="required" value="${user.mobile}" style="width:240px"></td>
                </tr>
                <tr>
                    <td align="right">邮箱:</td>
                    <td><input class="inp_text easyui-validatebox" id="email" type="text" name="user.email" value="${user.email}" style="width:240px"
                               data-options="validType: 'email',invalidMessage:'email格式不正确'"/></td>
                    <td align="right">是否有效:</td>
                    <td colspan="3">
                        <label><input type="radio" name="user.validate" value="1"/> 是</label>&nbsp;&nbsp;
                        <label><input type="radio" name="user.validate" value="0"/> 否</label>
                    </td>
                </tr>
                <tr>

                    <td align="right">创建时间:</td>
                    <td>
                        <input class="inp_text" type="text" id="createTime" name="user.createTime" value="<fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />" style="width:240px" readonly='true'/>
                    </td>
                    <td align="right">修改时间:</td>
                        <td><input class="inp_text" type="text" id="modifyTime" name="user.modifyTime" value="<fmt:formatDate value="${user.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss" />" style="width:240px"  readonly='true'></td>
                </tr>                
                <tr>
                    <td colspan="4" height="10"></td>
                </tr>
            </table>
        </div>
        <div class="tools_bar">
            <button type="button" class="btn" onclick="goToPage('${ctx}/user/manageQuery');">取 消</button>
            <button type="button" id="addUserBtn" class="btn btn-primary">保 存</button>
        </div>
    </div>
</form>
<script src="${ctx}/views/user/js/user.js"></script>
<script src="${ctx}/static/js/vagueSeachCustom.js" ></script>
<script src="${ctx}/static/js/vagueSeachCompany.js" ></script>
<script>
    <c:if test="${not empty user}">
    $('input[name = "user.userFlag"]').each(function (index, element) {
	        <c:if test="${user.userFlag == '0'}">
		        if (element.value == '0') {
		            element.checked = "checked";
		        }
	        </c:if>
	        <c:if test="${user.userFlag == '1'}">
		        if (element.value == '1') {
		            element.checked = "checked";
		        }
	        </c:if>
	        <c:if test="${user.userFlag == '2'}">
		        if (element.value == '2') {
		            element.checked = "checked";
		        }
	        </c:if>
	        
    });
    $('input[name = "user.validate"]').each(function (index, element) {
        <c:if test="${user.validate == true}">
        if (element.value == 1) {
            element.checked = "checked";
        }
        </c:if>
        <c:if test="${user.validate == false}">
        if(element.value == 0){
            element.checked = "checked";
        }
        </c:if>
    });
    </c:if>

    $('input[name = "user.customer.customerFlag"]').each(function (index, element) {
    	if(element.value == "" || element.value == '0'){
        	//梓熙 或者新增
        	$("#tdComCode").show();
        	$("#tdComName").show();
        	$("#tdComCodeHidden").hide();
        }else{
        	//外部客户，需要赋值总公司值
        	$("#tdComCode").hide();
        	$("#tdComName").hide();
        	$("#tdComCodeHidden").show();
        }
    });
    
    <c:if test="${op == 'view'}">
    $('input').each(function (index, element) {
        $(element).attr('readonly',true);
        $('input[name = "user.userFlag"]').each(function (index, element) {
           $(element).prop('disabled',true);
        });
    });
    $('#addUserBtn').remove();
    </c:if>

    <c:if test="op == 'update'">
        $('#userForm').attribute('action','${ctx}/user/register')
    </c:if>

</script>
</body>
</html>