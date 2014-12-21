<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<script type="text/javascript">

    function ajaxSubmit() {
    	
    	alert("1");
        $.ajax({
            url: "${ctx}/synthesis/queryBoard/",
            type: "POST",
            data: {
                boardNo: $("#boardNo").val()
            },
            dataType: "json",
            success: function(data) {
                alert(data.length);
                //for(var i=0;i<data.length;i++){
                  //  alert(data[i].propertyPath +":"+data[i].message+"<br/>");
                //}
            },
            error:function(){
                alert("失败");
            }
        });
        alert("2");
    }
    
    
    function addProductNo() {
		var productNoList = document.getElementsByName("productNo");//页面选择的生产编号
		var elements = document.getElementsByTagName('input');
		
		if(productNoList.length!=0){
			for(var i = 0;i<productNoList.length;i++){
				//alert("页面生产编号="+productNoList[i].value);
				
				// 如果为空，循环放入
				for(var j = 0; j < elements.length; j++) 
				{
					if(elements[j].name.slice(0,5) == "mapa:")
					{
						var div = document.getElementById(elements[j].name.slice(5));
						if ( div.value == ""){
							div.value = productNoList[i].value;
							break;
						}
					}
				}
			}
		}
		
    }
    
</script>
</head>

<body>
	<div class="container">
		<%@ include file="/WEB-INF/layouts/header.jsp"%>
		<div id="content" class="span12">
	<form id="inputForm"
	action="${ctx}/synthesis/submitBoard/" method="post" enctype="multipart/form-data"
	class="form-horizontal">
	   
	   <%-- 把页面选择的生产编号和id，存在此处，然后通过js对空的空进行赋值 --%>
		<c:forEach items="${primerProducts}" var="primerProduct">
			<tr>
			    <input type="text" id="productNo" name="productNo" value="${primerProduct.productNo}"/><br>
			</tr>
		</c:forEach>
					
		<fieldset>
			<legend><small>制作合成板</small></legend>
	
				<div class="controls">
				    <label for="loginName" class="control-label">板号:</label>
					<input type="text" id="boardNo" name="boardNo" size="40" value="${board.boardNo}" class="required"/>
				    <input id="submit" class="btn btn-primary" type="button" value="查询" onclick="ajaxSubmit();"/>&nbsp;&nbsp;
				    <input id="submit" class="btn btn-primary" type="button" value="添加生产编号" onclick="addProductNo();"/>&nbsp;&nbsp;

					<input type="radio" value="0" name="boardType" ${board.boardType eq "0" || null == board.boardType ? "checked='checked'" : ''}/>横排&nbsp;
					<input type="radio" value="1" name="boardType" ${board.boardType eq "1" ? "checked='checked'" : ''}/>竖排
				</div>
		</fieldset>

		<fieldset>
			<legend><small>排版</small></legend>
	       <div id="content" class="span12" style="display:">
	            <table border="0" cellpadding="3" cellspacing="1" width="90%" align="left" style="background-color: #b9d8f3;"
				    id="contentTable0" style="display:" class="table table-striped table-bordered table-condensed">

					<tr bgcolor='#F4FAFF'>
					    <td align="left"><font size="2">A1：</font><input name="mapa:A1" size="10" id="A1" type="text" value="${A1}"/></td>
					    <td align="left"><font size="2">A2：</font><input name="mapa:A2" size="10" id="A2" type="text" value="${A2}"/></td>
					    <td align="left"><font size="2">A3：</font><input name="mapa:A3" size="10" id="A3" type="text" value="${A3}"/></td>
					    <td align="left"><font size="2">A4：</font><input name="mapa:A4" size="10" id="A4" type="text" value="${A4}"/></td>
					</tr>
					<tr bgcolor='#F4FAFF'>
					    <td align="left"><font size="2">B1：</font><input name="mapa:B1" size="10" id="B1" type="text" value="${B1}"/></td>
					    <td align="left"><font size="2">B2：</font><input name="mapa:B2" size="10" id="B2" type="text" value="${B2}"/></td>
					    <td align="left"><font size="2">B3：</font><input name="mapa:B3" size="10" id="B3" type="text" value="${B3}"/></td>
					    <td align="left"><font size="2">B4：</font><input name="mapa:B4" size="10" id="B4" type="text" value="${B4}"/></td>
					</tr>
					<tr bgcolor='#F4FAFF'>
					    <td align="left"><font size="2">C1：</font><input name="mapa:C1" size="10" id="C1" type="text" value="${C1}"/></td>
					    <td align="left"><font size="2">C2：</font><input name="mapa:C2" size="10" id="C2" type="text" value="${C2}"/></td>
					    <td align="left"><font size="2">C3：</font><input name="mapa:C3" size="10" id="C3" type="text" value="${C3}"/></td>
					    <td align="left"><font size="2">C4：</font><input name="mapa:C4" size="10" id="C4" type="text" value="${C4}"/></td>
					</tr>
				</table>

			</div>
		</fieldset>
			<div class="form-actions">
				<input id="submit" class="btn btn-primary" type="submit" value="制作合成板" />&nbsp;
				<input id="cancel" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
	</form>
	
</body>
</html>
