<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<% 
String boardNoStr = new String(request.getParameter("boardNo").getBytes("ISO-8859-1"),"UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<script src="${ctx}/static/js/json2.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
<style>
    .panel-tool{display: none;}
</style>
<div class="tools">
	<table width="100%">
		<tr>
			<td align="right" width="100">合成板名称:</td>
			<td><%=boardNoStr%></td>
			<input type="hidden" id="boardNo" name="boardNo" value="<%=boardNoStr%>"/>
		</tr>
        <tr>
            <td colspan="4" height="10"></td>
        </tr>
	</table>
    <div class="btn_group">
        <p class="right pipe_number"><span id="totals"></span></p>
        <button class="btn" id="selectAll" onclick="selectAll(this);">全选</button>
        <button class="btn btn-success" onclick="setSucceed(true);">成功</button>
        <button class="btn btn-danger" onclick="setSucceed(false);">失败</button>
    </div>
</div>
<div id="board_box">
    <div class="board_padding">
        <table width="100%" id="holeList"></table>
    </div>
</div>
<div class="tools_bar tabbing">
    <button class="btn" onclick="window.history.back();" type="">取 消</button>
    <button class="btn btn-primary" onclick="saveBoard('purify');" type="">保 存</button>
</div>
<div id="inputCause" class="easyui-dialog" data-options="closed:true"><textarea class="inp_text" style="width: 376px;height: 102px;"></textarea></div>
<script src="${ctx}/static/js/commonResultsBoard.js"></script>
<script type="text/javascript">
$(function(){
    $('#boardSequence').on("click","li",sequenceClick);
    setBoardHeight();
    $("#holeList").on("click","div.hole_box",holesClick);
    boardEdit('holeList','purify');
})
</script>
</body>
</html>