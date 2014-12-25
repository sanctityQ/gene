<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查看详细数据</title>
<script type="text/javascript">

</script>
</head>

<body>
	<form id="inputForm" action="" method="post" class="form-horizontal">
	
		<table style="width:90%">
		  <tr>
		     <td>订单号： </td>
		     <td>${primerProduct.order.orderNo}</td>
		     <td>生产编号： </td>
		     <td>${primerProduct.productNo}</td>
		  </tr>
		  <tr>
		     <td>Primer引物名称： </td>
		     <td>${primerProduct.primeName}</td>
		     <td>序列： </td>
		     <td>${primerProduct.geneOrder}</td>
		  </tr>
		  <tr>
		     <td>nmol总量： </td>
		     <td>
				<c:forEach items="${primerProduct.primerProductValues}" var="primerProductValue"  varStatus="status">
					<c:if test="${primerProductValue.type == 'nmolTotal'}">
					${primerProductValue.value}
					</c:if>
				</c:forEach>
            </td>
		     <td>nmol/tube： </td>
		     <td>		     
				<c:forEach items="${primerProduct.primerProductValues}" var="primerProductValue2"  varStatus="status">
					<c:if test="${primerProductValue2.type == 'nmolTB'}">
					${primerProductValue2.value}
					</c:if>
				</c:forEach>
			 </td>	
		  </tr>
		  <tr>
		     <td>OD总量： </td>
		     <td>
				<c:forEach items="${primerProduct.primerProductValues}" var="primerProductValue"  varStatus="status">
					<c:if test="${primerProductValue.type == 'odTotal'}">
					${primerProductValue.value}
					</c:if>
				</c:forEach>
			</td>
		     <td>OD/tube： </td>
		     <td>
				<c:forEach items="${primerProduct.primerProductValues}" var="primerProductValue"  varStatus="status">
					<c:if test="${primerProductValue.type == 'odTB'}">
					${primerProductValue.value}
					</c:if>
				</c:forEach>
			</td>
		  </tr>
		  <tr>
		     <td>纯化方式： </td>
		     <td>${primerProduct.purifyType}</td>
		     <td>5'修饰： </td>
		     <td>${primerProduct.modiFiveType}</td>
		  </tr>
		  <tr>
		     <td>3'修饰： </td>
		     <td>${primerProduct.modiThreeType}</td>
		     <td>中间修饰： </td>
		     <td>${primerProduct.modiMidType}</td>
		  </tr>
		  <tr>
		     <td>特殊单位： </td>
		     <td>${primerProduct.modiSpeType}</td>
		     <td>修饰价格： </td>
		     <td>${primerProduct.modiPrice}</td>
		  </tr>
		  <tr>
		     <td>碱基单价： </td>
		     <td>${primerProduct.baseVal}</td>
		     <td>纯化价格： </td>
		     <td>${primerProduct.purifyVal}</td>
		  </tr>
		  <tr>
		     <td>总价格： </td>
		     <td>${primerProduct.totalVal}</td>
		     <td>描述： </td>
		     <td>${primerProduct.remark}</td>
		  </tr>
		  <tr>
		      <td colspan="2"></td>
		     <td><input class="btn btn-primary" type="button" value="确定" onclick="window.close()"/></td>
		  </tr>
		</table>
	</form>
</body>
</html>
