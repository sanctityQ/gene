<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<link href="${ctx}/static/css/easyui.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/icon.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/perfect-scrollbar.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/master.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/font-awesome.min.css" />
<!--[if IE 7]>
  <link rel="stylesheet" href="css/font-awesome-ie7.min.css">
<![endif]-->
<script src="${ctx}/static/js/jquery.min.js" ></script>
<script src="${ctx}/static/js/jquery.easyui.min.js" ></script>
<script src="${ctx}/static/js/perfect-scrollbar.min.js" ></script>
<script src="${ctx}/static/js/index.js" ></script>
</head>
<body>
<div class="page_padding">
	<div class="content_box import_error">
		<div class="error_box">
			<h2>数据导入失败</h2>
			<p>数据包含以下错误信息，请修正后重新导入</p>
			<ul></ul><div id="errors"></div></ul>
			<c:forEach var="item" items="${errors}" varStatus="status">     
		      <p><span>&#149; ${item}</span></p>   
		    </c:forEach> 
		</div>
	</div>	
	<div class="tools">
		<button type="submit" class="btn" onclick="goToPage('${ctx}/order/import');">重新导入</button>
	</div>
</div>
<script type="text/javascript">
/* getErrors();
var getErrors=function(){
	for(var i=0;i<errors.length;i++){
		$("#errors").append("<li>"+errors[i]+"</li><p>");
	}
} */
</script>
</body>
</html>