<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<script src="${ctx}/static/js/ajaxfileupload.js" ></script>
<script src="${ctx}/static/js/index.js" ></script>
<%
ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
String customerFlag = user.getUser().getCustomer().getCustomerFlag();
%>
</head>
<body>
<form name="form" action="${ctx}/synthesis/uploadDetect" method="post" enctype="multipart/form-data" class="form-horizontal">
<input type="hidden" id="customerFlag" name="customerFlag" value="<%=customerFlag %>"/>
<div class="page_padding">
	<div class="content_box">
		<h2>上传检测文件</h2>
		<div class="import_box" style="line-height: 16px;">
			<i class="icon-upload-alt"></i>请上传需要关联的文件(pdf,word)【文件命名规则：生产编号-描述.pdf，例如：503223817-张三.pdf】。
			<br /><br />
			<div class="file_box">
				<input name="file" type="file" onchange="document.getElementById('viewfile').value=this.value;" class="file" id="upload" /> 
				<input name="file" type="text" id="viewfile" class="inp_text" value="请选择文件…" style="width: 220px;" /> 
				<label class="btn" for="upload">浏览…</label>
			</div>
		</div>
		<div class="import_box" style="padding-bottom: 50px;">
			<button class="btn-primary submit" type="" onclick="return goToResultsBoard()">检测结果</button>
		</div>
	</div>
</div>
</form>
<script type="text/javascript">
function goToResultsBoard(){
	
	if($("#customerFlag").val()!='0'){
		alert("只有梓熙生物公司的用户才可以使用此功能。");
		return false;
	}
	
	var viewfile = $.trim($('#viewfile').val());

	if(viewfile == "请选择文件…"){
		alert("请选择文件。");
		return false;
	}
	
	if(!lastname()){
		return false;
	}

}

function lastname(){
	 //获取欲上传的文件路径
	var filepath = document.getElementById("viewfile").value;
	//为了避免转义反斜杠出问题，这里将对其进行转换
	var re = /(\\+)/g;
	var filename=filepath.replace(re,"#");//对路径字符串进行剪切截取
	var one=filename.split("#");//获取数组中最后一个，即文件名
	var two=one[one.length-1];//再对文件名进行截取，以取得后缀名
	var three=two.split(".");//获取截取的最后一个字符串，即为后缀名
	var last=three[three.length-1];//添加需要判断的后缀名类型
	var tp ="rar,zip,doc,docx,pdf";//返回符合条件的后缀名在字符串中的位置
	var rs=tp.indexOf(last);//如果返回的结果大于或等于0，说明包含允许上传的文件类型
	if(rs>=0){
	     return true;
	 }else{
		 alert("请选择"+tp+"格式的文件！");
		 return false;
     }
}

</script>
</body>
</html>