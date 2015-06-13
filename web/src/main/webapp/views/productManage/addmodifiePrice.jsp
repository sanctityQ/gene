<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
<script type="text/javascript">
var ctx = '${ctx}';
var modifyFlag = '${modifyFlag}';
</script>
</head>
<body>
<form id="modiPricefm" action="${ctx}/productManage/saveModiPrice" method="post">
<div class="page_padding">
	<div class="content_box info margin_btoom">
		<h2>修饰价格配置</h2>
		<table width="100%" class="order_info">
			<tr>
				<td colspan="4" height="10"></td>
			</tr>
			<tr>
			    <td align="right">&nbsp;</td>
                <td >&nbsp;<input type="hidden" name="modifiedPrice.id" value="${modifiedPrice.id}" /></td>
				<td align="right">修饰类型:</td>
                <td>
                <input type="hidden" id="modiType" name="modifiedPrice.modiType" value="${modifiedPrice.modiType}" />
                <span id="andType" style="display:none;">
                <select id="modiTypegroup5" class="my_select" style="width: 150px;" >
                <option value="">请选择</option>
                </select>
                &nbsp; and &nbsp;
                </span>
                <select id="modiTypeTemp" class="my_select" style="width: 150px;" >
                <option value="">请选择</option>
                </select>
                </td>
            </tr>
            <tr>
                <td colspan="4" height="10"></td>
            </tr>
			<tr>
                <td align="right">修饰类别:</td>
				<td>
				    <input type="hidden" id="categoriesType" value="${modifiedPrice.productCategories}" />
                    <select id="modiPriceCategories" name="modifiedPrice.productCategories" class="my_select" style="width: 150px;">
                        <option value="">请选择</option>
                        <option value="groupType">5'and 3'端修饰</option>
                        <option value="modiThreeType">3'端修饰</option>
                        <option value="modiFiveType">5'端修饰</option>
                        <option value="modiMidType">中间修饰</option>
                        <option value="modiSpeType">特殊单体</option>
                    </select>
				</td>
				<td align="right">修饰价格:</td>
				<td><input id="modiPrice" name="modifiedPrice.modiPrice" value="${modifiedPrice.modiPrice}" class="inp_text" type="text" value="" style="width:150px" /></td>
			</tr>
			<tr>
                <td colspan="4" height="10"></td>
            </tr>
			<tr>
			    <td align="right">&nbsp;</td>
                <td >&nbsp;</td>
				<td align="right">是否有效标识:</td>
				<td>
				    <select id="validate" name="modifiedPrice.validate" value="${modifiedPrice.validate}" class="my_select" style="width: 150px;" >
                        <option value="1">有效</option>
                        <option value="0">无效</option>
                    </select>
				</td>
			</tr>
			<tr>
				<td colspan="3" height="10"></td>
			</tr>
		</table>
	</div>
	<div class="tools_bar">
		<button type="button" class="btn" onclick="goToPage('${ctx}/productManage/modifiePricedList');">取 消</button>
		<button type="button" class="btn btn-primary" onclick="modiPriceSave();">保 存</button>
	</div>
</div>
</form>
<script src="${ctx}/views/productManage/js/productManage.js" ></script>
<script type="text/javascript">

$(document).ready(function(){ 
	if(modifyFlag){
		$("#modiPriceCategories").val($("#categoriesType").val());
		if($("#categoriesType").val()!='groupType'){
			//加载下拉选择项
			getselectInfo();
			//赋值下来
			$("#modiTypeTemp").val($("#modiType").val());
	    }else{
	    	$("#modiPriceCategories").val('modiThreeType');
	    	getselectInfo();
			getselectGroup();
			$("#modiPriceCategories").val($("#categoriesType").val());
			var textVal = $("#modiType").val();
			$("#modiTypegroup5").val(textVal.split("and")[0].trim());
			$("#modiTypeTemp").val(textVal.split("and")[1].trim());
	    }
	}
    var validate = "${modifiedPrice.validate}";
    if(validate!=""){
   	 $("#validate").val(validate);
    }
})

</script>
</body>
</html>