<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.LockedAccountException "%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>登陆</title>
	<link href="${ctx}/static/css/login.css" rel="stylesheet" type="text/css" />
    <script src="${ctx}/static/js/jquery.min.js"></script>
    <script>
        $(function () {
            $('#login_button').click(function () {
                $('#loginForm').submit();
            })

            $('#password').bind('keyup', function(event){
                if (event.keyCode=="13"){
                    $('#login_button').trigger('click');
                }
            });


        });
    </script>
</head>

<body>
		<div class="page">
			<div class="repair"></div>
			<div class="main" id="main">
				<div class="logo"></div>
				<h1 class="title1">欢迎使用 梓熙生物生产管理系统</h1>
				<ul class="login">
					<form id="loginForm" action="${ctx}/login" method="post">
					<li><span>用户名：</span><input type="text" id="username" name="username" value="${username}" class="text" /></li>
					<li><span>密码：</span><input type="password"  id="password" name="password"  class="text" /></li>
					<li><span></span><input type="button" id="login_button" class="btn_login" value="登 录" /></li>

						<%
							String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
							if(error != null){
						%>
							<li class="note">
						<%
								if(error.contains("DisabledAccountException")){
									out.print("用户已被屏蔽,请登录其他用户.");
								}
								else{
									out.print("账号或密码错误，请重试.");
								}
						%>
							</li>
						<%
							}
						%>
					</form>
				</ul>
			</div>
			<div class="copyright">
				Copyright &copy; 2015 BeiJingZixiBioTechCO.,Ltd
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<div class="controls">--%>
				<%--<label class="checkbox inline" for="rememberMe"> <input type="checkbox" id="rememberMe" name="rememberMe"/> 记住我</label>--%>
				<%--<input id="submit_btn" class="btn" type="submit" value="登录"/>--%>
				<%--<p class="help-block">(管理员：<b>admin/admin</b>, 普通用户：<b>user/user</b>)</p>--%>
			<%--</div>--%>
		<%--</div>--%>

</body>
</html>
