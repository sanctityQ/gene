<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
<script type="text/javascript">
var ctx = '${ctx}';
</script>
</head>
<body>
<form id="customerfm" action="${ctx}/customer/save" method="post">
<div class="page_padding">
	<div class="content_box info margin_btoom">
		<h2>客户信息</h2>
		
		<table width="100%" class="order_info">
			<tr>
				<td colspan="4" height="10"></td>
			</tr>
            <tr>
                <td align="right">客户编号:</td>
                <td><input class="inp_text" type="text" id="customercode" name="customer.code" value="${customer.code}"  style="width:150px" /></td>
                <td align="right">客户公司:</td>
				<td colspan="3"><input class="inp_text" type="text" id="customername" name="customer.name" value="${customer.name}"  style="width:200px" /></td>
            </tr>
			<tr>
				<td align="right">客户性质:</td>
				<td>
	                <select id="customerFlag" name="customer.customerFlag" value="${customer.customerFlag}" class="my_select" style="width: 100px;" >
	                    <option value="">请选择</option>
	                    <option value="1">代理公司</option>
	                    <option value="2">直接客户</option>
	                    <option value="0">梓熙</option>
	                </select>
                </td>
                <td align="right">负责人姓名:</td>
				<td><input class="inp_text" type="text" name="customer.leaderName" value="${customer.leaderName}"  style="width:150px" /></td>
			</tr>
             <tr>
				<td align="right">业务员姓名:</td>
                <td><input class="inp_text" type="text" autocomplete="off" id="seachUserName" name="customer.handlerName" value="${customer.handlerName}" style="width: 150px" />
			    <input type="hidden" id="vagueUserCode" name="customer.handlerCode" value="${customer.handlerCode}"/>
			    <ul id="seachUserList"></ul>
			</tr>
			<tr>
				 <td align="right">结算方式:</td>
                <td ><input class="inp_text" type="text" name="customer.payWays" value="${customer.payWays}"  style="width:150px" /></td>
            	<td align="right">发票抬头:</td>
				<td><input class="inp_text" type="text" name="customer.invoiceTitle" value="${customer.invoiceTitle}"  style="width: 80%" /></td>
				
			</tr>
			<tr>
				<td align="right">生产编号开头:</td>
				<td>
                    <input class="inp_text" type="text" name="customer.prefix" value="${customer.prefix}"  style="width:50px" />
				</td>
				<td align="right">联系电话:</td>
				<td>
                    <input class="inp_text" type="text" name="customer.phoneNo" value="${customer.phoneNo}"  style="width:150px" />
				</td>
			</tr>
            <tr>
                <td align="right">&nbsp;
            	</td>
            	<td>
            	<input class="readonly_inp" type="text" disabled value="例：X501120004为生产编号，录入生产编号开头标识X开头。"  style="width: 200px" />
            	</td>
				<td align="right">网址:</td>
				<td>
                    <input class="inp_text" type="text" name="customer.webSite" value="${customer.webSite}"  style="width:150px" />
				</td>
            </tr>
            <tr>
                <td align="right">办事处:</td>
                <td ><input class="inp_text" name="customer.office" type="text" value="${customer.office}"  style="width: 50%" /></td>
            	<td align="right">传真:</td>
				<td>
                    <input class="inp_text" type="text" name="customer.fax" value="${customer.fax}"  style="width:150px" />
				</td>
            </tr>
			<tr>
				<td align="right">客户地址:</td>
				<td>
                    <input class="inp_text" type="text" name="customer.address" value="${customer.address}"  style="width: 80%" />
                </td>
                <td align="right">Email:</td>
                <td><input class="inp_text" name="customer.email" type="text" value="${customer.email}"  style="width:150px" /></td>
			</tr>
            <%-- <tr>
				<td align="right">修饰价格:</td>
				<td><input class="inp_text" type="text" name="customer.customerPrice.modifyPrice" value="${customer.customerPrice.modifyPrice}"  style="width:150px" /></td>
				<td align="right">碱基单价:</td>
                <td><input class="inp_text" name="customer.customerPrice.baseVal" type="text" value="${customer.customerPrice.baseVal}"  style="width:150px" /></td>
				
			</tr>
			<tr>
				<td align="right">纯化价格:</td>
				<td><input class="inp_text" type="text" name="customer.customerPrice.purifyVal" value="${customer.customerPrice.purifyVal}"  style="width:150px" /></td>
				<td align="right">&nbsp;</td>
                <td>&nbsp;</td>
				
			</tr> --%>
			<tr>
				<td colspan="3" height="10"></td>
			</tr>
			<input type="hidden" name="customer.createTime" value="<fmt:formatDate value="${customer.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />"/>
			<input type="hidden" name="customer.id" value="${customer.id}"/>
			<input type="hidden" id="haveZiXi" value="${haveZiXi}"/>
		</table>
	</div>
	<div class="tools_bar">
		<button type="button" class="btn" onclick="goToPage('${ctx}/customer/clientManage');">取 消</button>
		<button type="button" class="btn btn-primary" onclick="customerSave();">保 存</button>
	</div>
</div>
</form>
<script src="${ctx}/static/js/vagueSeachUser.js" ></script>
<script type="text/javascript">
$(document).ready(function(){ 
    var customerFlag = "${customer.customerFlag}";
    if(customerFlag!=""){
   	 $("#customerFlag").val(customerFlag);
    }
})

var customerSave=function(){
	var messAge = '';
	if($("#customercode").val()==''){
		messAge += "请录入客户代码。\n";
	}
	if($("#customername").val()==''){
		messAge += "请录入客户公司。\n";
	}
	if($("#customerFlag").val()==''){
		messAge += "请选择客户性质。\n";
	}
	if($("#customerFlag").val()=='0'){
		if($("#haveZiXi").val()=='1'){
    		messAge += "系统中客户性质已存在‘梓熙’。\n";
		}
		if($("#seachUserName").val()!=''){
    		messAge += "客户性质选择‘梓熙’时不需要录入业务员。\n";
		}
	}else{
		if($("#vagueUserCode").val()==''){
			messAge += "请选择业务员(点选下拉列表中的结果)。\n";
		}
	}
	if(messAge!=''){
		alert(messAge);
		return false;
	}
	$("#customerfm").submit();
}
</script>                                      
</body>
</html>