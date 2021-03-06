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
						var headStr = elements[j].name.slice(0,elements[j].name.indexOf("]")+2);
						var lastStr = elements[j].name.slice(elements[j].name.indexOf("]")+2);
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
		<%@ include file="/static/layouts/header.jsp"%>
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
			<legend><small>制作合成板 - 新增</small></legend>
	
				<div class="controls">
				    <label for="loginName" class="control-label">板号:</label>
					<input type="text" id="boardNo" name="boardNo" size="40" value="" class="required"/>
				    <input id="submit" class="btn btn-primary" type="button" value="查询(暂不能用)" onclick="ajaxSubmit();"/>&nbsp;&nbsp;
				    <input id="submit" class="btn btn-primary" type="button" value="添加生产编号" onclick="addProductNo();"/>&nbsp;&nbsp;

					<input type="radio" value="0" name="boardType" checked/>横排&nbsp;
					<input type="radio" value="1" name="boardType" />竖排
					<input type="hidden" value="0" name="type" />
				</div>
		</fieldset>

		<fieldset>
			<legend><small>排版</small></legend>
	       <div id="content" class="span12" style="display:">
	            <table border="0" cellpadding="3" cellspacing="1" width="90%" align="left" style="background-color: #b9d8f3;"
				    id="contentTable0" style="display:" class="table table-striped table-bordered table-condensed">

					<tr bgcolor='#F4FAFF'>
					    <td align="left"><font size="2">A1：</font>
					        <input name="boardHoles[0].holeNo" size="10" id="A1" type="text" value="A1"/>
					        <input name="boardHoles[0].primerProduct.id" size="10" id="boardHoles[0].primerProduct.id" type="text" value=""/>
					        <input name="boardHoles[0].primerProduct.productNo" size="10" id="boardHoles[0].primerProduct.productNo" type="text" value=""/>
					    </td>
					    <td align="left"><font size="2">A2：</font>
					        <input name="boardHoles[1].holeNo" size="10" id="A2" type="text" value="A2"/>
					        <input name="boardHoles[1].primerProduct.id" size="10" id="boardHoles[1].primerProduct.id" type="text" value=""/>
					        <input name="boardHoles[1].primerProduct.productNo" size="10" id="boardHoles[1].primerProduct.productNo" type="text" value=""/>
					    </td>
					    <td align="left"><font size="2">A3：</font>
					        <input name="boardHoles[2].holeNo" size="10" id="A3" type="text" value="A3"/>
					        <input name="boardHoles[2].primerProduct.id" size="10" id="boardHoles[2].primerProduct.id" type="text" value=""/>
					        <input name="boardHoles[2].primerProduct.productNo" size="10" id="boardHoles[2].primerProduct.productNo" type="text" value=""/>
					    </td>
					</tr>
					<tr bgcolor='#F4FAFF'>
					    <td align="left"><font size="2">B1：</font>
					        <input name="boardHoles[3].holeNo" size="10" id="B1" type="text" value="B1"/>
					        <input name="boardHoles[3].primerProduct.id" size="10" id="boardHoles[3].primerProduct.id" type="text" value=""/>
					        <input name="boardHoles[3].primerProduct.productNo" size="10" id="boardHoles[3].primerProduct.productNo" type="text" value=""/>
					    </td>
					    <td align="left"><font size="2">B2：</font>
					        <input name="boardHoles[4].holeNo" size="10" id="B2" type="text" value="B2"/>
					        <input name="boardHoles[4].primerProduct.id" size="10" id="boardHoles[4].primerProduct.id" type="text" value=""/>
					        <input name="boardHoles[4].primerProduct.productNo" size="10" id="boardHoles[4].primerProduct.productNo" type="text" value=""/>
					    </td>
					    <td align="left"><font size="2">B3：</font>
					        <input name="boardHoles[5].holeNo" size="10" id="B3" type="text" value="B3"/>
					        <input name="boardHoles[5].primerProduct.id" size="10" id="boardHoles[5].primerProduct.id" type="text" value=""/>
					        <input name="boardHoles[5].primerProduct.productNo" size="10" id="boardHoles[5].primerProduct.productNo" type="text" value=""/>
					    </td>

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
