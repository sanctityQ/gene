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

	function changeFlagValue(){
		var checkFlagList = document.getElementsByName("checkFlag");
		var productNoValueList = document.getElementsByName("productNoValue");
		var productNoList = document.getElementsByName("productNo");
		
		if(checkFlagList.length!=0){
			for(var i = 0;i<checkFlagList.length;i++){
				if(checkFlagList[i].checked==true){
					productNoValueList[i].value = productNoList[i].value;
				}else{
					productNoValueList[i].value = "0";
				}
			}
		}
		
		
	}
</script>

</head>

<body>
	<div class="container">
		<%@ include file="/static/layouts/header.jsp"%>
		<div id="content" class="span12">
		<form id="inputForm"
		  action="${ctx}/synthesis/synthesisEdit/" method="post" class="form-horizontal">
            <table border="1" cellpadding="3" cellspacing="1" width="90%" align="left" style="background-color: #b9d8f3;"
			    id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr style="text-align: center; COLOR: #0076C8; BACKGROUND-COLOR: #F4FAFF; font-weight: bold">
						<td><font size="4"></font></td>
						<td><font size="4">板号id</font></td>
						<td><font size="4">板号</font></td>
						<td><font size="4">板类型</font></td>
						<td><font size="4">创建时间</font></td>
						<td><font size="4">创建人</font></td>
					</tr>
				</thead>
				<% int i=1;	%>
				<tbody>
					<c:forEach items="${boards}" var="board" varStatus="status">
						<tr bgcolor='#F4FAFF'>
							<td nowrap="nowrap" align="left"><%=i++ %></td>
							<td align="left"><input type="text" size="2" name="id" size="50" value="${board.id}"/></td>
							<td align="left">${board.boardNo}<a href="${ctx}/synthesis/synthesisEdit/${board.id}" id="editLink-${board.id}">录入</a></td>
							<td align="left">${board.boardType eq "0" || null == board.boardType ? '横排' : '竖排'}</td>
							<td align="left"><fmt:formatDate value="${board.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td align="left">${board.createUser}</td>
						</tr>
					</c:forEach>
				</tbody>
				
			
			</table>
			</form>
		</div>

	</div>
</body>
</html>
