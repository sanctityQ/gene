<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<script src="${ctx}/static/js/jquery.min.js" ></script>
<script src="${ctx}/static/js/jquery.easyui.min.js" ></script>
<script src="${ctx}/static/js/perfect-scrollbar.min.js" ></script>
<script src="${ctx}/static/js/index.js" ></script>
<script src="${ctx}/static/js/json2.js"></script>
<script type="text/javascript">


function ajaxBatchDelete() {

	var userInfoList=new Array();
	var selectFlag = false;

	jQuery("input[name='userCheck']:checked").each(function(index){
  	      var outbound = { "id":"" };
          outbound["id"] = "";
          outbound["id"] = jQuery("input[name='users["+index+"].id']")[0].value;
          userInfoList.push(outbound);
          selectFlag = true;
	});
	
	if(!selectFlag){
		alert("请选择要删除的业务员。");
		return false;
	}
	
    $.ajax({
    	async:false,
        url: "${ctx}/user/batchDelete/",
        type: "POST",
        data:"userInfoList="+JSON.stringify(userInfoList),
        dataType: "json",
        success: function(data) {
        	
        }
        
    });
	


	
}
</script>
</head>
<body>
	<form name="form" action="${ctx}/user/manageQuery/" method="post" class="">
    <table>
    	<tr>
    		<td>
    			姓名：<input type="text" id="userName" name="userName" value="${userName}"/>
    		</td>
    		<td>
    			部门：<input type="text" id="comCode" name="comCode" value="${comCode}"/>
    		</td>
    		<td>
        		   <button type="" class="btn btn-primary" onclick="userQuery();">查询</button>
    		</td>
    		<td>
    		       <button type="" class="btn btn-primary" onclick="prepareAddUser();">增加业务员</button>
    		</td>
    		<td>   
    		       <button type="" class="btn btn-primary" onclick="ajaxBatchDelete();">批量删除</button>
    		</td>
    	</tr>
    </table>
    <c:if test="${not empty page.content}">
	    <table>
	    <thead>
		    <tr>
		        <td></td>
		        <td>姓名</td>
		        <td>部门</td>
		        <td>电话</td>
		        <td>操作</td>
		    </tr>
	    </thead>
	    <tbody>
	    <c:forEach items="${page.content}" var="user" varStatus="status">
	       <tr>
	           <td><input type="checkbox"  name="userCheck"  value="${status.index}" onclick=""/></td>
	           <input type="hidden"  name="users[${status.index}].id" size="10" value="${user.id}" />
	           <td><input type="text"  name="users[${status.index}].name" size="10" value="${user.name}" /></td>
	           <td><input type="text"  name="users[${status.index}].comCode" size="10" value="${user.comCode}" /></td>
	           <td><input type="text"  name="users[${status.index}].mobile" size="10" value="${user.mobile}" /></td>
	           <td></td>
	       </tr>  
		</c:forEach>
		</tbody>
		</table>
	</c:if>
	</form>
<script src="${ctx}/views/user/js/user.js" ></script>
</body>
</html>