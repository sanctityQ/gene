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
    	var pageIdList = document.getElementsByName("primerProductPage.id");//页面选择的生产编号id
		var pageProductNoList = document.getElementsByName("primerProductPage.productNo");//页面选择的生产编号
		var elements = document.getElementsByTagName("input");
		
		//alert("页面生产编号长度="+pageIdList.length);
		
		
		if(pageIdList.length!=0){
			for(var i = 0;i<pageIdList.length;i++){
				//alert("页面生产编号="+productNoList[i].value);
				
				// 如果为空，循环放入
				for(var j = 0; j < elements.length; j++) 
				{
					if(elements[j].name.slice(0,10) == "boardHoles")
					{
						//boardHoles[0].holeNo
						var headStr = elements[j].name.slice(0,14);
						var lastStr = elements[j].name.slice(14);
						if ( lastStr == "primerProduct.id"){
							var div = document.getElementById(elements[j].name);
							if ( div.value == pageIdList[i].value){
								break;
							}
							if ( div.value == ""){
								div.value = pageIdList[i].value;
								document.getElementById(headStr+"primerProduct.productNo").value = pageProductNoList[i].value;
								break;
							}
							
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
	action="${ctx}/synthesis/submitBoard/" method="post" class="form-horizontal">
	   
	   		<fieldset>
       			<legend><small>页面选择的生产数据id和生产编号</small></legend>
       			<div class="controls">
		     <%-- 把页面选择的生产编号和id，存在此处，然后通过js对空的空进行赋值 --%>
			<c:forEach items="${primerProductPages}" var="primerProductPage">
				<tr>
				   <td align="left">
				    <input type="text" id="productNo" name="primerProductPage.id" value="${primerProductPage.id}"/>
				   </td>
				   <td align="left">
				   		<input type="text" size="12"  name="primerProductPage.productNo" size="50" 
				   		      	<c:if test="${not empty primerProductPage.productNo}">
				   		            value="${primerProductPage.productNo}" 
				   		        </c:if> 
				   		        <c:if test="${empty primerProductPage.productNo}">
				   		            value="${primerProductPage.outProductNo}" 
				   		        </c:if> 
				   		      />
				   </td><br>
				</tr>
			</c:forEach>
			</div>
	   		
	   		</fieldset>
					
		<fieldset>
			<legend><small>制作合成板 - 修改</small></legend>
	
				<div class="controls">
				    <label for="loginName" class="control-label">板号id:</label>
					<input type="text" name="id" size="10" value="${board.id}" class="required"/>
					<label for="loginName" class="control-label">板号:</label>
					<input type="text" id="boardNo" name="boardNo" size="40" value="${board.boardNo}" class="required"/>

					<input type="radio" value="0" name="boardType" ${board.boardType eq "0" || null == board.boardType ? "checked='checked'" : ''}/>横排&nbsp;
					<input type="radio" value="1" name="boardType" ${board.boardType eq "1" ? "checked='checked'" : ''}/>竖排
					<input type="hidden" value="0" name="type" />
				    <input class="btn btn-primary" type="button" value="添加生产编号" onclick="addProductNo();"/>&nbsp;&nbsp;
				</div>
		</fieldset>

		<fieldset>
			<legend><small>排版</small></legend>
	       <div id="content" class="span12" style="display:">
	            <table border="0" cellpadding="3" cellspacing="1" width="90%" align="left" style="background-color: #b9d8f3;"
				    id="contentTable0" style="display:" class="table table-striped table-bordered table-condensed">

					<tr bgcolor='#F4FAFF'>
<c:forEach items="${board.boardHoles}" var="boardHole" varStatus="status">
 <td align="left"><font size="2">${boardHole.holeNo}：</font>
     <input name="boardHoles[${status.index}].id" size="10" type="text" value="${boardHole.id}"/>
     <input name="boardHoles[${status.index}].holeNo" size="10" id="A1" type="text" value="${boardHole.holeNo}"/>
     <input name="boardHoles[${status.index}].primerProduct.id" size="10" id="boardHoles[${status.index}].primerProduct.id" type="text" value="${boardHole.primerProduct.id}"/>
     <input name="boardHoles[${status.index}].primerProduct.productNo" size="10" id="boardHoles[${status.index}].primerProduct.productNo" type="text" value="${boardHole.primerProduct.productNo}"/>
 </td>
</c:forEach>
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
