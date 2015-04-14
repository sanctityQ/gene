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
</script>
</head>
<body>
<form id="molecularfm" action="${ctx}/productManage/save" method="post">
<div class="page_padding">
	<div class="content_box info margin_btoom">
		<h2>产品信息</h2>
		<table width="100%" class="order_info">
			<tr>
				<td colspan="4" height="10"></td>
			</tr>
			<tr>
			    <td align="right">&nbsp;</td>
                <td >&nbsp;<input type="hidden" name="productMolecular.id" value="${productMolecular.id}" /></td>
				<td align="right">产品代码:</td>
                <td><input id="productCode" name="productMolecular.productCode" value="${productMolecular.productCode}" class="inp_text" type="text" value="" style="width:150px" /></td>
            </tr>
            <tr>
                <td colspan="4" height="10"></td>
            </tr>
			<tr>
                <td align="right">类别:</td>
				<td>
                    <select id="productCategories" name="productMolecular.productCategories" value="${productMolecular.productCategories}" class="my_select" style="width: 150px;" name="state">
                        <option value="modiThreeType">3'端修饰</option>
                        <option value="modiFiveType">5'端修饰</option>
                        <option value="modiMidType">中间修饰</option>
                        <option value="modiSpeType">特殊单体</option>
                    </select>
				</td>
				<td align="right">修饰分子量:</td>
				<td><input id="modifiedMolecular" name="productMolecular.modifiedMolecular" value="${productMolecular.modifiedMolecular}" class="inp_text" type="text" value="" style="width:150px" /></td>
			</tr>
			<tr>
                <td colspan="4" height="10"></td>
            </tr>
			<tr>
			    <td align="right">&nbsp;</td>
                <td >&nbsp;</td>
				<td align="right">是否有效标识:</td>
				<td>
				    <select id="validate" name="productMolecular.validate" value="${productMolecular.validate}" class="my_select" style="width: 150px;" name="state">
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
		<button type="" class="btn" onclick="goToPage('${ctx}/productManage/productMolecularList');">取 消</button>
		<button type="button" class="btn btn-primary" onclick="productMolecularSave();">保 存</button>
	</div>
</div>
</form>
<script src="${ctx}/views/productManage/js/productManage.js" ></script>
</body>
</html>