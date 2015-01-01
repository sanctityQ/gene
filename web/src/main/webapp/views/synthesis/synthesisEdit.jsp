<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title></title>
<script type="text/javascript">

    function selectAll() {
		var elements = document.getElementsByTagName("input");
		
		for(var j = 0; j < elements.length; j++) 
		{
			if(elements[j].name.slice(0,10) == "boardHoles")
			{   
				var headStr = elements[j].name.slice(0,elements[j].name.indexOf("]")+2);
				var lastStr = elements[j].name.slice(elements[j].name.indexOf("]")+2);
				if ( lastStr == "primerProduct.selectID"){
					var div = document.getElementById(elements[j].name);
					div.checked = true;
				}
			}
		}
		
    }

    function setValue(flag) {
    	
		var elements = document.getElementsByTagName("input");
		
		for(var j = 0; j < elements.length; j++) 
		{
			if(elements[j].name.slice(0,10) == "boardHoles")
			{   
				var headStr = elements[j].name.slice(0,elements[j].name.indexOf("]")+2);
				var lastStr = elements[j].name.slice(elements[j].name.indexOf("]")+2);
				if ( lastStr == "primerProduct.selectID"){
					var selectIDDiv = document.getElementById(elements[j].name);
					var productNoDiv = document.getElementById(headStr+"primerProduct.productNo");
					var selectFlagDiv = document.getElementById(headStr+"primerProduct.selectFlag");
					
					if ( selectIDDiv.checked){
						if( flag == '1' ){
							productNoDiv.style.backgroundColor = "green";
						}else{
							productNoDiv.style.backgroundColor = "red";
						}
						selectFlagDiv.value = flag;
						selectIDDiv.checked = false;
					}
					
				}
			}
		}
		
    }
    
    //录入失败原因
    function writeFailReason(){
    	
    	
    	window.open("${ctx}/synthesis/failReason/","newwindow","resizable=1,scrollbars=1,width=600,height=300");
    }

    function submitSynthesis(){
    	
		var elements = document.getElementsByTagName("input");
		
		for(var j = 0; j < elements.length; j++) 
		{
			if(elements[j].name.slice(0,10) == "boardHoles")
			{
				var headStr = elements[j].name.slice(0,elements[j].name.indexOf("]")+2);
				var lastStr = elements[j].name.slice(elements[j].name.indexOf("]")+2);
				if ( lastStr == "primerProduct.selectFlag"){
					var selectFlagDiv = document.getElementById(elements[j].name);
					if( selectFlagDiv.value == '0' ){
						alert("还有未选择的生产编码，请确认！");
						return false;
					}
					
				}
			}
		}
		
		form.submit();
		
    }
    
	
</script>
</head>

<body>
	<form name="form" action="${ctx}/synthesis/submitSynthesis/" method="post" class="form-horizontal">
		<%@ include file="/static/layouts/header.jsp"%>
	   
		<fieldset>
			<legend><small>录入合成结果</small></legend>
	
				<div class="controls">
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				    <input class="btn btn-primary" type="button" value="全选" onclick="selectAll();"/>&nbsp;&nbsp;
				    <input class="btn btn-primary" type="button" value="成功" onclick="setValue('1');"/>&nbsp;&nbsp;
				    <input class="btn btn-primary" type="button" value="失败" onclick="writeFailReason();"/>&nbsp;&nbsp;
				    
					<input type="hidden" name="id" value="${board.id}" />
					<input type="hidden" name="boardNo" value="${board.boardNo}" />
					<input type="hidden" name="boardType" value="${board.boardType}" />
					<input type="hidden" name="type"  value="${board.type}" />
					<input type="hidden" name="createTime"  value="<fmt:formatDate value="${board.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />" />
					<input type="hidden" name="createUser"  value="${board.createUser}"/>
					<input type="hidden" name="failReason" id="failReason"  value=""/>
					
				</div>
		</fieldset>

		<fieldset>
			<legend><small>排版</small></legend>
	       <div id="content" class="span12" style="display:">
	            <table border="0" cellpadding="3" cellspacing="1" width="90%" align="left" style="background-color: #b9d8f3;"
				    id="contentTable0" style="display:" class="table table-striped table-bordered table-condensed">

					<tr bgcolor='#F4FAFF'>
<c:forEach items="${board.boardHoles}" var="boardHole" varStatus="status">
 <td align="left"><font size="2">${boardHole.holeNo}:</font>
<input name="boardHoles[${status.index}].primerProduct.selectFlag" id="boardHoles[${status.index}].primerProduct.selectFlag" type="hidden" value="0"/>
<input name="boardHoles[${status.index}].primerProduct.selectID" id="boardHoles[${status.index}].primerProduct.selectID" type="checkbox" value="0"/>
<input name="boardHoles[${status.index}].id" size="10" type="hidden" value="${boardHole.id}"/>
<input name="boardHoles[${status.index}].board.id" size="10" type="hidden" value="${boardHole.board.id}"/>
<input name="boardHoles[${status.index}].board.boardNo" size="10" type="hidden" value="${boardHole.board.boardNo}"/>
<input name="boardHoles[${status.index}].holeNo" size="10" type="hidden" value="${boardHole.holeNo}"/>
<input name="boardHoles[${status.index}].createTime" size="10" type="hidden" value="<fmt:formatDate value="${boardHole.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />"/>
<input name="boardHoles[${status.index}].createUser" size="10" type="hidden" value="${boardHole.createUser}"/>
<input name="boardHoles[${status.index}].primerProduct.id" size="1" type="text" id="boardHoles[${status.index}].primerProduct.id" value="${boardHole.primerProduct.id}"/>
<input name="boardHoles[${status.index}].primerProduct.order.id" size="1" type="hidden" id="boardHoles[${status.index}].primerProduct.order.id" value="${boardHole.primerProduct.order.id}"/>
<input name="boardHoles[${status.index}].primerProduct.order.orderNo" size="1" type="hidden" id="boardHoles[${status.index}].primerProduct.order.orderNo" value="${boardHole.primerProduct.order.orderNo}"/>
<input name="boardHoles[${status.index}].primerProduct.productNo" size="10" type="text" id="boardHoles[${status.index}].primerProduct.productNo" value="${boardHole.primerProduct.productNo}" />
<input name="boardHoles[${status.index}].primerProduct.outProductNo" type="hidden" value="${boardHole.primerProduct.outProductNo}"/>
<input name="boardHoles[${status.index}].primerProduct.fromProductNo" type="hidden" value="${boardHole.primerProduct.fromProductNo}"/>
<input name="boardHoles[${status.index}].primerProduct.primeName" type="hidden" value="${boardHole.primerProduct.primeName}"/>
<input name="boardHoles[${status.index}].primerProduct.geneOrder" type="hidden" value="${boardHole.primerProduct.geneOrder}"/>
<input name="boardHoles[${status.index}].primerProduct.purifyType" type="hidden" value="${boardHole.primerProduct.purifyType}"/>
<input name="boardHoles[${status.index}].primerProduct.modiFiveType" type="hidden" value="${boardHole.primerProduct.modiFiveType}"/>
<input name="boardHoles[${status.index}].primerProduct.modiThreeType" type="hidden" value="${boardHole.primerProduct.modiThreeType}"/>
<input name="boardHoles[${status.index}].primerProduct.modiMidType" type="hidden" value="${boardHole.primerProduct.modiMidType}"/>
<input name="boardHoles[${status.index}].primerProduct.modiSpeType" type="hidden" value="${boardHole.primerProduct.modiSpeType}"/>
<input name="boardHoles[${status.index}].primerProduct.modiPrice" type="hidden" value="${boardHole.primerProduct.modiPrice}"/>
<input name="boardHoles[${status.index}].primerProduct.baseVal" type="hidden" value="${boardHole.primerProduct.baseVal}"/>
<input name="boardHoles[${status.index}].primerProduct.purifyVal" type="hidden" value="${boardHole.primerProduct.purifyVal}"/>
<input name="boardHoles[${status.index}].primerProduct.totalVal" type="hidden" value="${boardHole.primerProduct.totalVal}"/>
<input name="boardHoles[${status.index}].primerProduct.remark" type="hidden" value="${boardHole.primerProduct.remark}"/>
<input name="boardHoles[${status.index}].primerProduct.operationType" type="hidden" value="${boardHole.primerProduct.operationType}"/>
<input name="boardHoles[${status.index}].primerProduct.boardNo" type="hidden" value="${boardHole.primerProduct.boardNo}"/>
<input name="boardHoles[${status.index}].primerProduct.comCode" type="hidden" value="${boardHole.primerProduct.comCode}"/>
<input name="boardHoles[${status.index}].primerProduct.backTimes" type="hidden" value="${boardHole.primerProduct.backTimes}"/>
<input name="boardHoles[${status.index}].primerProduct.reviewFileName" type="hidden" value="${boardHole.primerProduct.reviewFileName}"/>
 </td>
</c:forEach>
					</tr>

				</table>

			</div>
		</fieldset>
			<div class="form-actions">
				<input id="save" class="btn btn-primary" type="button" value="保存" onclick="submitSynthesis()"/>&nbsp;
				<input id="cancel" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
	</form>
	
</body>
</html>
