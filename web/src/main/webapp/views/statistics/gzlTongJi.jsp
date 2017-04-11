<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page import="org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<script src="${ctx}/static/js/json2.js"></script>
<%
ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
String customerFlag = user.getUser().getCustomer().getCustomerFlag();
%>
</head>
<body>
<form name="form" action="${ctx}/delivery/deliveryList/" method="post">
<input type="hidden" id="customerFlagOld" name="customerFlagOld" value="<%=customerFlag %>"/>
<div class="tools">
	<table width="100%">
		<tr>
            <td align="right">选择日期区间:</td>
            <td>
                <input type="text" class="easyui-datebox" id="createStartTime" required="required" style="width: 120px;">
                -
                <input type="text" class="easyui-datebox" id="createEndTime" required="required" style="width: 120px;">
            </td>
			<td align="right">工号:</td>
			<td><input id=userCode class="inp_text" type="text" value="" style="width: 200px" onchange="showButtonDiv()"/></td>
			<td align="right"></td>
			<td align="right"></td>
			<td align="right"></td>
		</tr>
        <tr>
            <td colspan="6" height="20"></td>
        </tr>
	</table>
    <div class="btn_group" id="buttonDiv">
        <button type="button" class="btn btn-primary" onclick="exportGZLTJ()">导出</button>
    </div>
</div>
</form>
<script src="${ctx}/static/js/vagueSeachCustom.js" ></script>
<script src="${ctx}/static/js/vagueSeachContacts.js" ></script>
<script src="${ctx}/static/js/vagueSeachUser.js" ></script>
<script type="text/javascript">

$('#createStartTime').datebox({
    onSelect: function (date) {
    	$('#buttonDiv').show();
    }
});
$('#createEndTime').datebox({
    onSelect: function (date) {
    	$('#buttonDiv').show();
    }
});

var showButtonDiv = function(){
	$('#buttonDiv').show();
}
var exportGZLTJ = function(){
	
    var createStartTime = $('#createStartTime').datebox('getValue');
    var createEndTime = $('#createEndTime').datebox('getValue');
    
    //var dateSpan = $('#dateSpan input[name="dateFlag"]:checked ').val();//取得日期跨度的值

    if(createStartTime=="" || createEndTime=="" ){
    	alert("请选择需要统计的日期。");
    	return false;
    }

    var userCode = $('#userCode').val();
    
    if(userCode==""){
    	alert("请填写工号。");
    	return false;
    }
    
    $('#buttonDiv').hide();
    
    var statisticsInfos = new Array();
    
    var statisticsInfo = {
		     "createStartTime":createStartTime,
		     "createEndTime":createEndTime,
		     "userCode":userCode
		     };
    statisticsInfos.push(statisticsInfo);
    
   document.form.action = "/gene/statistics/exportGZLTongJi?statisticsInfojson="+JSON.stringify(statisticsInfos); 
   document.form.submit();
}
</script>
</body>
</html>