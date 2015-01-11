<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
</head>
<body>
	<form name="form" action="${ctx}/user/addUser/" method="post" class="">
    <table>
    	<tr>
    		<td>
        		   <button id="addUserBtn" type="" class="btn btn-primary" onclick="addUser();">保存</button>
    		</td>
    	</tr>
    	<tr>
    		<td>
    			工号：<input type="text" id="code" name="user.code" value=""/>
    		</td>
    		<td>
    			姓名：<input type="text" id="name" name="user.name" value=""/>
    		</td>
    	</tr>
    	<tr>
    		<td>
    			密码：<input type="text" id="password" name="user.password" value=""/>
    		</td>
    		<td>
    			机构代码：<input type="text" id="comCode" name="user.comCode" value=""/>
    		</td>
    	</tr>
    	<tr>
    		<td>
    			手机号：<input type="text" id="mobile" name="user.mobile" value=""/>
    		</td>
    		<td>
    			邮箱：<input type="text" id="email" name="user.email" value=""/>
    		</td>
    		<td>
    			是否有效：
                <input type="radio" name="user.validate" value="1" checked/>有效
                <input type="radio" name="user.validate" value="0" />无效
    		</td>
    	</tr>
    	<tr>
    		<td>
    			本公司用户标识：
                <input type="radio" name="user.staffFlag" value="1" checked/>是
                <input type="radio" name="user.staffFlag" value="0" />否
    		</td>
    		<td>
    			客户信息数据ID：<input type="text" id="customerid" name="user.customer.id" value=""/>
    		</td>
    	</tr>
    </table>
	</form>
<script src="${ctx}/views/user/js/user.js" ></script>
</body>
</html>