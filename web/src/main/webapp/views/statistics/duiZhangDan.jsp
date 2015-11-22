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
			<td align="right">外部订单号:</td>
			<td><input id="outOrderNo" class="inp_text" type="text" value="" style="width: 150px"  onchange="showButtonDiv()"/></td>
			<td align="right">生产编号:</td>
			<td><input id="productNo" class="inp_text" type="text" value="" style="width: 100px"  onchange="showButtonDiv()"/></td>
			<td align="right">生产编号开头:</td>
			<td><input id=productNoPrefix class="inp_text" type="text" autocomplete="off" style="width: 40px"  onchange="showButtonDiv()"/>
		</tr>
		<tr>
            <td colspan="8" height="20"></td>
        </tr>
		<tr>
			<td align="right">客户公司名称:</td>
			<td><input class="inp_text" type="text" autocomplete="off" id="seachCustom" name="customerName" value="" style="width: 200px"  onchange="showButtonDiv()"/>
			    <input class="inp_text" type="hidden" id="customerid" name="customerid" value=""/>
			    <input class="inp_text" type="hidden" id="customerCode" name="customerCode" value=""/>
			    <ul id="seachCustomList"></ul>
			</td>
			<td align="right">客户姓名:</td>
            <td>
			    <input class="inp_text" type="text" autocomplete="off" id="seachContacts" name="contactsname" value="" style="width: 150px"  onchange="showButtonDiv()"/>
			    <input class="inp_text" type="hidden" id="contactsid" name="contactsid" value=""/>
			    <ul id="seachContactsList"></ul>
			</td>
			<td align="right">业务员:</td>
            <td>
                <input class="inp_text" type="text" autocomplete="off" id="seachUserName" name="customer.handlerName" value="" style="width: 100px"  onchange="showButtonDiv()"/>
			    <input class="inp_text" type="hidden" id="vagueUserCode" name="customer.handlerCode" value=""/>
			    <ul id="seachUserList"></ul>
			</td>
		</tr>
        <tr>
            <td colspan="8" height="20"></td>
        </tr>
	</table>
    <div class="btn_group" id="buttonDiv">
        <button type="button" class="btn btn-primary" onclick="exportDZD()">导出</button>
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

var exportDZD = function(){
	
    var createStartTime = $('#createStartTime').datebox('getValue');
    var createEndTime   = $('#createEndTime').datebox('getValue');
    
    if(createStartTime=="" || createEndTime=="" ){
    	alert("订单导入时间段必须选择。");
    	return false;
    }

    $('#buttonDiv').hide();
    
    var outOrderNo = $('#outOrderNo').val();
    var customerid = $('#customerid').val();
    var customerCode = $('#customerCode').val();
    var contactsid = $('#contactsid').val();
    var vagueUserCode = $('#vagueUserCode').val();
    var contactsName = $('#seachContacts').val();
    var productNoPrefix=$("#productNoPrefix").val();
    if(contactsid == ""){
    	contactsName = "";
    }
	
    var productNo = $('#productNo').val();
    
    var statisticsInfos = new Array();
    
    var statisticsInfo = {
		     "createStartTime":createStartTime,
		     "createEndTime":createEndTime,
		     "outOrderNo":outOrderNo,
		     "customerId":customerid,
		     "customerCode":customerCode,
		     "contactsId":contactsid,
		     "userCode":vagueUserCode,
		     "contactsName":contactsName,
		     "productNo":productNo,
		     "productNoPrefix":productNoPrefix
		     };
    statisticsInfos.push(statisticsInfo);
    
   document.form.action = "/gene/statistics/exportDuiZhangDan?statisticsInfojson="+JSON.stringify(statisticsInfos); 
   document.form.submit();
}
</script>
</body>
</html>