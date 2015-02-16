<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
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
<script type="text/javascript">
var orderNo = ${orderNo};
var ctx = '${ctx}';
</script>
</head>
<form id="deliveryLabelfm"  action="${ctx}/delivery/deliveryLabelPrint" method="post" class="form-horizontal">
<body>

<div class="page_padding">
	<div class="content_box">
		<h2>发货标签</h2>
		<div class="import_box">
			<i class="icon-pencil"></i>请输入需导出的板号或生产编号，点击“发货标签”按钮下载文件。
			<br />
			<input class="inp_text" type="text" id="boardNoOrProductNo" value="" style="width: 300px" />
			<input type="hidden" id="noType" value=""/>
			<ul id="seachBoardList"></ul>
		</div>

		<div class="import_box" style="padding-bottom: 50px;">
			<button class="btn-primary submit" type="button" onclick="deliveryLabelPrint();">发货标签</button>
		</div>
	</div>
</div>

<script src="${ctx}/views/delivery/js/deliveryLabel.js" ></script>
<script src="${ctx}/static/js/vagueSeachBoardOrproductNo.js"></script>
</body>
</form>
</html>