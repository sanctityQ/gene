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
			<td><input id="outOrderNo" class="inp_text" type="text" value="" style="width: 200px" /></td>
			<td align="right">生产编号:</td>
			<td><input id="productNo" class="inp_text" type="text" value="" style="width: 200px" /></td>
		</tr>
		<tr>
			<td align="right">客户公司名称:</td>
			<td><input class="inp_text" type="text" autocomplete="off" id="seachCustom" name="customerName" value="" style="width: 200px" />
			    <input type="hidden" id="customerid" name="customerid" value=""/>
			    <input type="hidden" id="customerCode" name="customerCode" value=""/>
			    <input type="hidden" id="customerFlag" name="customerFlag" value=""/>
			    <ul id="seachCustomList"></ul>
			</td>
			<td align="right">客户姓名:</td>
            <td>
			    <input class="inp_text" type="text" autocomplete="off" id="seachContacts" name="contactsname" value="" style="width: 200px" />
			    <input class="inp_text" type="hidden" id="contactsid" name="contactsid" value=""/>
			    <ul id="seachContactsList"></ul>
			</td>
			<td align="right">特殊单体:</td>
			<td>
                <select id="modiSpeType" class="my_select" style="width: 100px;">
                <option value="">全部</option>
				<c:forEach items="${modiSpes}" var="modiSpe"  varStatus="status">
				  <option value="${modiSpe.productCode}">${modiSpe.productCode}</option>
				</c:forEach>
                </select>
			</td>
		</tr>
		<tr>
			<td align="right">5修饰:</td>
			<td>
                <select id="modiFiveType" class="my_select" style="width: 100px;">
                <option value="">全部</option>
				<c:forEach items="${modiFives}" var="modiFive"  varStatus="status">
				  <option value="${modiFive.productCode}">${modiFive.productCode}</option>
				</c:forEach>
                </select>
			</td>
			<td align="right">3修饰:</td>
			<td>
                <select id="modiThreeType" class="my_select" style="width: 100px;">
                <option value="">全部</option>
				<c:forEach items="${modiThrees}" var="modiThree"  varStatus="status">
				  <option value="${modiThree.productCode}">${modiThree.productCode}</option>
				</c:forEach>
                </select>
			</td>
			<td align="right">中间修饰:</td>
			<td>
                <select id="modiMidType" class="my_select" style="width: 100px;">
                <option value="">全部</option>
				<c:forEach items="${modiMids}" var="modiMid"  varStatus="status">
				  <option value="${modiMid.productCode}">${modiMid.productCode}</option>
				</c:forEach>
                </select>
			</td>
		</tr>
        <tr>
            <td colspan="8" height="20"></td>
        </tr>
	</table>
    <div class="btn_group">
        <button type="button" class="btn btn-primary" onclick="exportFile()">导出</button>
    </div>
</div>
</form>
<script src="${ctx}/static/js/vagueSeachCustom.js" ></script>
<script src="${ctx}/static/js/vagueSeachContacts.js" ></script>
<script type="text/javascript">
var exportFile = function(){
	
    var createStartTime = $('#createStartTime').datebox('getValue');
    var createEndTime   = $('#createEndTime').datebox('getValue');
    
    if(createStartTime=="" || createEndTime=="" ){
    	alert("订单导入时间段必须选择。");
    	return false;
    }

    var outOrderNo = $('#outOrderNo').val();
    var customerid = $('#customerid').val();
    var customerCode = $('#customerCode').val();
    var customerFlag = $('#customerFlag').val();
    var contactsid = $('#contactsid').val();
    var contactsName = $('#seachContacts').val();
    if(contactsid == ""){
    	contactsName = "";
    }
	
    var productNo     = $('#productNo').val();
    var modiFiveType  = $('#modiFiveType').val();
    var modiThreeType = $('#modiThreeType').val();
    var modiMidType   = $('#modiMidType').val();
    var modiSpeType   = $('#modiSpeType').val();
    
    var statisticsInfos = new Array();
    
    var statisticsInfo = {
		     "createStartTime":createStartTime,
		     "createEndTime":createEndTime,
		     "outOrderNo":outOrderNo,
		     "customerId":customerid,
		     "customerCode":customerCode,
		     "customerFlag":customerFlag,
		     "contactsId":contactsid,
		     "contactsName":contactsName,
		     "productNo":productNo,
		     "modiFiveType":modiFiveType,
		     "modiThreeType":modiThreeType,
		     "modiMidType":modiMidType,
		     "modiSpeType":modiSpeType
		     };
    statisticsInfos.push(statisticsInfo);
    
   document.form.action = "/gene/statistics/exportXiuShiJinDuBiao?statisticsInfojson="+JSON.stringify(statisticsInfos); 
   document.form.submit();
}
</script>
</body>
</html>