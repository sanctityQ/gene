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
<script type="text/javascript">
var boardNo = ${boardNo};
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
			<td>${boardNo}</td>
			<input type="hidden" id="boardNo" name="boardNo" value="${boardNo}"/>
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
    <button class="btn" onclick="goToPage('views/synthesis/measureResults.jsp');" type="">取 消</button>
    <button class="btn btn-primary" onclick="saveBoard('measure');" type="">保 存</button>
</div>
<div id="inputCause" class="easyui-dialog" data-options="closed:true"><textarea class="inp_text" style="width: 376px;height: 102px;"></textarea></div>
<script src="${ctx}/static/js/commonResultsBoard.js"></script>
<script type="text/javascript">
$(function(){
    $('#boardSequence').on("click","li",sequenceClick);
    setBoardHeight();
    $("#holeList").on("click","div.hole_box",holesClick);
    boardEditMeasure(${measuredata});
})

function boardEditMeasure(data){
 
	data = $.parseJSON(data);//String to json
    var board = $('#holeList');
    var tBody = '';
	var typeFlag = data.typeFlag;
	var typeDesc = data.typeDesc;
    var total = data.total;
    var rows = data.rows;
    $('#totals').text(total);
    for(var i = 0; i < rows.length; i++){
        var row = 'row' + (i+1);
        var hole = rows[i][row];
        var tr = '<tr>';
        for(var j = 0; j < hole.length; j++){
            var identifying = hole[j].identifying;
            if(identifying != ''){
                tr += '<td><div class="hole_box"><div class="hole">'+hole[j].No+'</div><div class="identifying">'+hole[j].identifying+'</div><div class="tag">'+hole[j].tag+'</div></div></td>';
            }else{
                tr += '<td><div class="hole_box"><div class="hole">'+hole[j].No+'</div><div class="tag"><i class="icon-ok"></i>'+hole[j].tag+'</div></div></td>';
            }
        }
        tr += '</tr>';
        tBody += tr;
    }
    if(typeFlag == '1'){
    	board.append(tBody);
    }else{
    	$.messager.alert("系统提示", "所选择板号有不符合可"+typeDesc+"状态的生产数据，请确认！");
    }
}
</script>
</body>
</html>