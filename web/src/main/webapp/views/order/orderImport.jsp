<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>订单导入</title>
</head>
<body>
<div class="page_padding">
	<div class="content_box">
		<h2>导入单订信息</h2>
		<div class="import_box">
			<i class="icon-group"></i>请输入客户姓名或客户代码。
			<br />
			<input class="inp_text" id="seachCustom" type="text" name="" value="" style="width: 300px" />
			<ul id="seachList"></ul>
		</div>
		<div class="import_box" style="line-height: 16px;">
			<i class="icon-upload-alt"></i>上传您的excel模板，系统将根据您导入的信息生成订单。
			<br />
			<a href="#" class="down_excel">下载excel模板文件…</a>
			<br /><br />
			<div class="file_box">
				<input type="file" onchange="document.getElementById('viewfile').value=this.value;" class="file" id="upload" /> 
				<input name="" type="text" id="viewfile" class="inp_text" value="请选择文件…" style="width: 220px;" /> 
				<label class="btn" for="unload">浏览…</label>
			</div>
		</div>
		<div class="import_box" style="padding-bottom: 50px;">
			<button class="btn-primary submit" type="buttin" onclick="goToPage('orderInfo.html');">生成订单</button>
		</div>
	</div>
</div>
<script src="js/vagueSeach.js" ></script>
</body>
</html>