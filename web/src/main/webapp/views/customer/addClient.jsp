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
                <td><input class="inp_text" type="text" name="customer.code" value="${customer.code}"  style="width:150px" /></td>
                <td align="right">姓名:</td>
				<td colspan="3"><input class="inp_text" type="text" name="customer.name" value="${customer.name}"  style="width:150px" /></td>
            </tr>
             
             <tr>
				<td align="right">业务员代码:</td>
				<td><input class="inp_text" type="text" name="customer.handlerCode" value="${customer.handlerCode}"  style="width:150px" /></td>
				<td align="right">业务员姓名:</td>
                <td><input class="inp_text" name="customer.handlerName" type="text" value="${customer.handlerName}"  style="width:150px" /></td>
				
			</tr>
			   
			<tr>
				 <td align="right">结算方式:</td>
                <td ><input class="inp_text" type="text" name="customer.payWays" value="${customer.payWays}"  style="width:150px" /></td>
				<td align="right">&nbsp;</td>
                <td colspan="3">&nbsp;</td>
				
			</tr>
			<tr>
				<td align="right">负责人姓名:</td>
				<td><input class="inp_text" type="text" name="customer.leaderName" value="${customer.leaderName}"  style="width:150px" /></td>
				<td align="right">客户单位:</td>
				<td>
                    <input class="inp_text" type="text" name="customer.unit" value="${customer.unit}"  style="width:150px" />
                </td>
			</tr>
			<tr>
				<td align="right">生产编号开头:</td>
				<td>
                    <input class="inp_text" type="text" name="customer.prefix" value="${customer.prefix}"  style="width:150px" />
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
            	<input class="readonly_inp" type="text" disabled value="例：X501120004为生产编号，录入生产编号开头标识X,J等开头。"  style="width: 80%" />
            	</td>
				<td align="right">网址:</td>
				<td>
                    <input class="inp_text" type="text" name="customer.webSite" value="${customer.webSite}"  style="width:150px" />
				</td>
            </tr>
            <tr>
            	
            	<td align="right">发票抬头:</td>
				<td><input class="inp_text" type="text" name="customer.invoiceTitle" value="${customer.invoiceTitle}"  style="width: 80%" /></td>
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
            <tr>
                <td align="right">办事处:</td>
                <td colspan="3"><input class="inp_text" name="customer.office" type="text" value="${customer.office}"  style="width: 50%" /></td>
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
		</table>
	</div>
	<div class="tools_bar">
		<button type="button" class="btn" onclick="goToPage('${ctx}/customer/clientManage');">取 消</button>
		<button type="submit" class="btn btn-primary">保 存</button>
	</div>
</div>
</form>                                      
</body>
</html>