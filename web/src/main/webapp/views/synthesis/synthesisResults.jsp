<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<script src="${ctx}/static/js/json2.js"></script>
</head>
<body>
<div class="page_padding">
	<div class="content_box margin_btoom">
	    <input type="hidden" id="operationType" name="operationType" value="synthesis"/>
		<h2>录入合成结果</h2>
		<div class="import_box" style="padding-top: 20px;">
			<i class="icon-pencil"></i>请输入板号，点击“合成结果”按钮，进入结果录入页面。
			<br />
			<input class="inp_text" type="text" autocomplete="off" id="boardNo" name="boardNo" value="" style="width: 300px" />
			<ul id="seachBoardList"></ul>
		</div>

		<div class="import_box" style="padding-bottom: 50px;">
			<button class="btn-primary submit" type="button" onclick="goToResultsBoard()">合成结果</button>
		</div>
	</div>
	<%@ include file="/views/synthesis/initBoardNo.jsp"%> <!--  未处理板号初始化页面 -->
</div>
<script src="${ctx}/static/js/vagueSeachBoard.js" ></script>
<script type="text/javascript">

function goToResultsBoard(){
	var boardNo = $.trim($('#boardNo').val());
	
	if(boardNo == ""){
		alert("请输入板号。");
		return;
	}
    goToPage("/gene/views/synthesis/synthesisResultsBoard.jsp?boardNo="+boardNo);
}
</script>
</body>
</html>