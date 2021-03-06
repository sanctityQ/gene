<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ary" value="${param.ary}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<link href="${ctx}/static/css/easyui.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/icon.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/perfect-scrollbar.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/master.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/font-awesome.min.css" />
<script src="${ctx}/static/js/jquery.min.js" ></script>
<script src="${ctx}/static/js/jquery.easyui.min.js" ></script>
<script src="${ctx}/static/js/perfect-scrollbar.min.js" ></script>
<script src="${ctx}/static/js/index.js" ></script>
<script src="${ctx}/static/js/json2.js"></script>
</head>
<body>
<div class="tools">
	<table width="100%">
	    <input type="hidden" id="productNoArray" name="productNoArray" value="${ary}"/>
	    <input type="hidden" id="boardType" name="boardType" value="1"/>
	    <input type="hidden" id="operationType" name="operationType" value="synthesis"/>
		<tr>
			<td align="right" width="100">合成板名称:</td>
			<td><input class="inp_text" type="text" id="boardNo" name="boardNo" value="" style="width: 100%" />
			    <ul id="seachBoardList"></ul>
			</td>
			<td><font size="2" color="red">提示：如您计划录入的合成板名称在模糊查询下拉列表中，请点击下拉列表的合成板名称进行选择使用。</font></td> 
		</tr>
        <tr>
            <td colspan="2" height="10"></td>
        </tr>
	</table>
    <div class="btn_group">
        <button class="btn btn-primary" id="mutualBtn" disabled onclick="exchangePosition();">交换位置</button>
        <ul class="right board_sequence" id="boardSequence">
            <li title="横排" onclick="makeBoard('holeList','0','1');" id="boardType0"></li>
            <li title="竖排" class="vertical selected" onclick="makeBoard('holeList','1','1');" id="boardType1"></li>
            <li class="notes">排列方式：</li>
        </ul>
        <p class="right pipe_number"><span id="totals"></span></p>
    </div>
</div>
<div id="board_box">
    <div class="board_padding">
        <table width="100%" id="holeList"></table>
    </div>
</div>
<div class="tools_bar tabbing">
    <button class="btn" onclick="goToPage('productionData.jsp');" type="button">取 消</button>
    <button class="btn btn-primary" onclick="saveBoard();" type="button">保 存</button>
</div>
<script src="${ctx}/static/js/makeBoard.js" ></script>
<script src="${ctx}/static/js/vagueSeachMakeBoard.js"></script>
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