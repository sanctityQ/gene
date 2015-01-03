<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<script type="text/javascript">

	function selectAll() {
		var elements = document.getElementsByTagName("input");
		
		for(var j = 0; j < elements.length; j++) 
		{
			if(elements[j].name.slice(0,elements[j].name.indexOf("[")) == "primerProductList.primerProducts")
			{   
				var headStr = elements[j].name.slice(0,elements[j].name.indexOf("]")+2);
				var lastStr = elements[j].name.slice(elements[j].name.indexOf("]")+2);
				if ( lastStr == "selectFlag"){
					var div = document.getElementById(elements[j].name);
					var all = document.getElementById("all");

					if (all.checked == true){
					    div.checked = true;
					}else{
						div.checked = false;
					}
				}
			}
		}
		
	}
	
	function exportReport(){
		form.submit();
	}
	
	function viewPrimerProduct(primerProductId){
		window.open("${ctx}/synthesis/viewPrimerProduct/"+primerProductId,""+primerProductId,"resizable=1,scrollbars=1,width=700,height=600");
	}
	
	
	
</script>

</head>

<body topmargin="0" leftmargin="0" rightmargin="0" onload="">
  <form name="form" action="${ctx}/print/exportReport/" method="post" class="form-horizontal">
     <table border="1" cellpadding="3" cellspacing="1" width="100%" style="background-color: #b9d8f3;" id="contentTable" class="common">
		<thead>
		<tr><%@ include file="/static/layouts/header.jsp"%></tr>
	    <tr>
			<td><input class="btn btn-primary" type="button" value="导出报告单" onclick="exportReport()"/></td>
	    </tr>
		<tr style="text-align: center; COLOR: #0076C8; BACKGROUND-COLOR: #F4FAFF; font-weight: bold">
			<td><font size="4"></font></td>
			<td>全选<input type="checkbox" id="all" name="all" value="" onclick="selectAll()"/></td>
			<td><font size="4">生产编号id</font></td>
			<td><font size="4">生产编号</font></td>
			<td><font size="4">板号</font></td>
			<td><font size="4">序列</font></td>
			<td><font size="4">OD总量</font></td>
			<td><font size="4">OD/TB</font></td>
			<td><font size="4">NUML总量</font></td>
			<td><font size="4">NUML/TB</font></td>
			<td><font size="4">碱基数</font></td>
			<td><font size="4">纯化方式</font></td>
			<td><font size="4">修饰</font></td>
			<td><font size="4">状态</font></td>
			<td><font size="4">重回次数</font></td>
		</tr>
		</thead>
		<% int i=1;	%>
		<tbody>
			<c:forEach items="${page.content}" var="primerProduct"  varStatus="status">
				<tr bgcolor='#F4FAFF'>
<input type="hidden" name="primerProductList.primerProducts[${status.index}].order.id" value="${primerProduct.order.id}" />
<input type="hidden" name="primerProductList.primerProducts[${status.index}].order.orderNo" value="${primerProduct.order.orderNo}" />
<input type="hidden" name="primerProductList.primerProducts[${status.index}].order.customerCode" value="${primerProduct.order.customerCode}" />

<td nowrap="nowrap" align="left"><%=i++ %></td>
<td><input type="checkbox" id="selectFlag" name="primerProductList.primerProducts[${status.index}].selectFlag" value="" onclick=""/></td>

<td align="left">
    <input type="text" size="2" name="primerProductList.primerProducts[${status.index}].id" size="50" value="${primerProduct.id}" />
</td>
<td align="left">
  
   	<c:if test="${not empty primerProduct.productNo}">
   		<input type="hidden" size="12"  name="primerProductList.primerProducts[${status.index}].productNo" size="50" value="${primerProduct.productNo}" />
   		<a href="javascript:viewPrimerProduct(${primerProduct.id});">${primerProduct.productNo}</a>
   	</c:if> 
   	<c:if test="${empty primerProduct.productNo}">
   		<input type="hidden" size="12" name="primerProductList.primerProducts[${status.index}].outProductNo" size="50" value="${primerProduct.outProductNo}" />
   		<a href="javascript:viewPrimerProduct(${primerProduct.id});">${primerProduct.outProductNo}</a>
   	</c:if>
</td>
<td align="left"><input type="text" size="12"  name="primerProductList.primerProducts[${status.index}].geneOrder" size="10" value="${primerProduct.geneOrder}" /></td>
<td align="left">${primerProduct.geneOrder}</td>
<td align="left">${primerProduct.odTotal}</td>
<td align="left">${primerProduct.odTB}</td>
<td align="left">${primerProduct.nmolTotal}</td>
<td align="left">${primerProduct.nmolTB}</td>
<td align="left">${primerProduct.odTotal}</td>
<td align="left">${primerProduct.tbn}</td>
<td align="left">${primerProduct.purifyType}</td>
<td align="left">${primerProduct.operationTypeDesc}</td>
<td align="left">${primerProduct.backTimes}</td>

<input type="hidden" name="primerProductList.primerProducts[${status.index}].odTotal" value="${primerProduct.odTotal}"/>
<input type="hidden" name="primerProductList.primerProducts[${status.index}].odTB"    value="${primerProduct.odTB}"/>
<input type="hidden" name="primerProductList.primerProducts[${status.index}].primeName"    value="${primerProduct.primeName}"/>
<input type="hidden" name="primerProductList.primerProducts[${status.index}].modiFiveType"    value="${primerProduct.modiFiveType}"/>
<input type="hidden" name="primerProductList.primerProducts[${status.index}].modiThreeType"    value="${primerProduct.modiThreeType}"/>
<input type="hidden" name="primerProductList.primerProducts[${status.index}].modiMidType"    value="${primerProduct.modiMidType}"/>
<input type="hidden" name="primerProductList.primerProducts[${status.index}].modiSpeType"    value="${primerProduct.modiSpeType}"/>

<c:forEach items="${primerProduct.primerProductValues}" var="primerProductValue"  varStatus="status1">
<input type="hidden" name="primerProductList.primerProducts[${status.index}].primerProductValues[${status1.index}].id" value="${primerProductValue.id}" />
<input type="hidden" name="primerProductList.primerProducts[${status.index}].primerProductValues[${status1.index}].type" value="${primerProductValue.type}" />
<input type="hidden" name="primerProductList.primerProducts[${status.index}].primerProductValues[${status1.index}].typeDesc" value="${primerProductValue.typeDesc}" />
<input type="hidden" name="primerProductList.primerProducts[${status.index}].primerProductValues[${status1.index}].value" value="${primerProductValue.value}" />
</c:forEach>

				</tr>
			</c:forEach>
		</tbody>
		<tfoot>
		    <!-- 翻页 -->
		      <table width="100%" class="common" align="center" cellpadding="0" cellspacing="0">
		           <tr>
		               <%@ include file="/common/pub/TurnOverPage.jsp" %>
		           </tr>
		           
		           <!-- 传递条件 -->
		           <input type="hidden" name="preRequestPath" value="${preRequestPath}" />
		           <input type="hidden" name="pageSize" value="${pageSize}" />
		           <input type="hidden" name="customer_code" value="${customer_code}" />
		           <input type="hidden" name="tbn1" value="${tbn1}" />
		           <input type="hidden" name="tbn2" value="${tbn2}" />
		           <input type="hidden" name="modiFlag" value="${modiFlag}" />
		           <input type="hidden" name="purifytype" value="${purifytype}" />
		           
		      </table>
		</tfoot>
	  </table>
	  <br>
	</form>
</body>
</html>
