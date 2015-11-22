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
            <td align="right">订单导入时间段:</td>
            <td>
                <input type="text" class="easyui-datebox" id="createStartTime" required="required" style="width: 120px;">
                -
                <input type="text" class="easyui-datebox" id="createEndTime" required="required" style="width: 120px;">
            </td>
			<td align="right">客户公司名称:</td>
			<td><input class="inp_text" type="text" autocomplete="off" id="seachCustom" name="customerName" value="" style="width: 200px"  onchange="showButtonDiv()"/>
			    <input type="hidden" id="customerid" name="customerid" value=""/>
			    <input type="hidden" id="customerCode" name="customerCode" value=""/>
			    <input type="hidden" id="customerFlag" name="customerFlag" value=""/>
			    <ul id="seachCustomList"></ul>
			</td>
		</tr>
	</table>
    <div class="btn_group" id="buttonDiv">
        <button type="button" class="btn btn-primary" onclick="exportFile()">导出</button>
    </div>
</div>
</form>
<script src="${ctx}/static/js/vagueSeachCustom.js" ></script>
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
var exportFile = function(){
	
    var createStartTime = $('#createStartTime').datebox('getValue');
    var createEndTime   = $('#createEndTime').datebox('getValue');
    
    if(createStartTime=="" || createEndTime=="" ){
    	alert("订单导入时间段必须选择。");
    	return false;
    }

    $('#buttonDiv').hide();
    
    var customerid = $('#customerid').val();
    var customerFlag = $('#customerFlag').val();
    
    var statisticsInfos = new Array();
    
    var statisticsInfo = {
		     "createStartTime":createStartTime,
		     "createEndTime":createEndTime,
		     "customerId":customerid,
		     "customerFlag":customerFlag
		     };
    statisticsInfos.push(statisticsInfo);
    
   document.form.action = "/gene/statistics/exportYinWuJinDuBiao?statisticsInfojson="+JSON.stringify(statisticsInfos); 
   document.form.submit();
}
</script>
</body>
</html>