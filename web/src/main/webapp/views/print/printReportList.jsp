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
	function exportFile(flag){
		
		var selectFlags = document.getElementsByName("selectFlag");
		var orderNos = document.getElementsByName("orderNo");
		var check = false;
		var orderNo = "";
		for(var j = 0; j < selectFlags.length; j++)
		{
			if(selectFlags[j].checked == true){
				orderNo = orderNos[j].value;
				check = true;
			}
		}
		
		if(check){
			form.action = "${ctx}/print/exportFile/"+orderNo+"/"+flag;
    		form.submit();
		}else{
			alert("请选择订单号！");
		}
	}

	
</script>

</head>

<body topmargin="0" leftmargin="0" rightmargin="0" onload="">
  <form name="form" action="${ctx}/print/exportReport/" method="post" class="form-horizontal">
     <table border="1" cellpadding="3" cellspacing="1" width="100%" style="background-color: #b9d8f3;" id="contentTable" class="common">
		<thead>
		<tr><%@ include file="/static/layouts/header.jsp"%></tr>
	    <tr>
			<td><input class="btn btn-primary" type="button" value="导出报告单" onclick="exportFile('0')"/></td>
			<td><input class="btn btn-primary" type="button" value="导出信封" onclick="exportFile('1')"/></td>
	    </tr>
		<tr style="text-align: center; COLOR: #0076C8; BACKGROUND-COLOR: #F4FAFF; font-weight: bold">
			<td><font size="4"></font></td>
			<td>选择</td>
			<td><font size="4">订单号</font></td>
			<td><font size="4">客户姓名</font></td>
			<td><font size="4">生产编号</font></td>
			<td><font size="4">碱基总数</font></td>
			<td><font size="4">状态</font></td>
			<td><font size="4">导入时间</font></td>
			<td><font size="4">修改时间</font></td>
		</tr>
		</thead>
		<% int i=1;	%>
		<tbody>
			<c:forEach  var="order"  items="${page.content}" varStatus="status">
				<tr bgcolor='#F4FAFF'>
<td nowrap="nowrap" align="left"><%=i++ %></td>
<td align="left"><input type="radio"  name="selectFlag" value="" /></td>
<td align="left"><input type="text"  name="orderNo" size="10" value="${order.orderNo}" /></td>
<td align="left"><input type="text"  name="customerName" size="10" value="${order.customerName}" /></td>
<td align="left"><input type="text"  name="productNoMinToMax" size="10" value="${order.productNoMinToMax}" /></td>
<td align="left"><input type="text"  name="tbnTotal" size="10" value="${order.tbnTotal}" /></td>
<td align="left"><input type="text"  name="status" size="10" value="${order.status}" /></td>
<td align="left"><input type="text"  name="createTime" size="10" value="${order.createTime}" /></td>
<td align="left"><input type="text"  name="modifyTime" size="10" value="${order.modifyTime}" /></td>


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
