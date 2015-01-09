<%@ page contentType="text/html;charset=UTF-8"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<link href="css/easyui.css" type="text/css" rel="stylesheet" />
<link href="css/icon.css" type="text/css" rel="stylesheet" />
<link href="css/perfect-scrollbar.min.css" type="text/css" rel="stylesheet" />
<link href="css/master.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="css/font-awesome.min.css" />
<!--[if IE 7]>
  <link rel="stylesheet" href="css/font-awesome-ie7.min.css">
<![endif]-->
<script src="js/jquery.min.js" ></script>
<script src="js/jquery.easyui.min.js" ></script>
<script src="js/perfect-scrollbar.min.js" ></script>
<script src="js/index.js" ></script>
</head>
<body>
<div class="page_padding">
	<div class="content_box import_error">
		<div class="error_box">
			<h2>数据导入失败</h2>
			<p>数据包含以下错误信息，请修正后重新导入</p>
			<ul>
				<li>OD与nmol是互斥的</li>
				<li>必须录⼊“纯化方式”入数据</li>
			</ul>
		</div>
	</div>	
	<div class="tools">
		<button type="buttin" class="btn" onclick="goToPage('orderImport.html');">重新导入</button>
	</div>
</div>
</body>
</html>