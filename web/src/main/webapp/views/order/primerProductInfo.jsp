<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title></title>
</head>
<%
ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
%>
<body>
<form id="orderForm" name="form" action="${ctx}/order/primerProductInfo" method="post">
    <div class="page_padding">
        <div class="content_box info margin_btoom">
            <h2>用户信息</h2>
            <table width="100%" class="order_info">
                <tr>
                    <td colspan="4" height="10"></td>
                </tr>
            <tr>
				<td align="right">生产编号:</td>
				<td><input id="product_No" name="product_No" class="readonly_inp" type="text"  style="width: 80%" value="${primerProduct.productNo}" disabled/></td>
				<td align="right">订单号:</td>
				<td><input id="orderNo" name="orderNo" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.order.orderNo}" disabled/></td>
			</tr>
			<tr>
				<td align="right">外部生产编号:</td>
				<td><input id="outProductNo" name="outProductNo" class="readonly_inp" type="text"  style="width: 80%" value="${primerProduct.outProductNo}" disabled/></td>
				<td align="right">引物名称:</td>
				<td><input id="primeName" name="primeName" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.primeName}" disabled/></td>
			</tr>
			<tr>
				<td align="right">引物序列:</td>
				<td><input id="geneOrder" name="geneOrder" class="readonly_inp" type="text"  style="width: 80%" value="${primerProduct.geneOrder}" disabled/></td>
				<td align="right">带修饰引物序列:</td>
				<td><input id="geneOrderMidi" name="geneOrderMidi" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.geneOrderMidi}" disabled/></td>
			</tr>
			<tr>
				<td align="right">纯化方式:</td>
				<td><input id="purifyType" name="purifyType" class="readonly_inp" type="text"  style="width: 80%" value="${primerProduct.purifyType}" disabled/></td>
			</tr>
			<tr>
				<td align="right">5修饰类型:</td>
				<td><input id="modiFiveType" name="modiFiveType" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.modiFiveType}" disabled/></td>
				<td align="right">3修饰类型:</td>
				<td><input id="modiThreeType" name="modiThreeType" class="readonly_inp" type="text"  style="width: 80%" value="${primerProduct.modiThreeType}" disabled/></td>
			</tr>
			<tr>
				<td align="right">中间修饰:</td>
				<td><input id="modiMidType" name="modiMidType" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.modiMidType}" disabled/></td>
				<td align="right">特殊单体:</td>
				<td><input id="modiSpeType" name="modiSpeType" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.modiSpeType}" disabled/></td>
			</tr>
			<tr>
				<td align="right">OD总量:</td>
				<td><input id="odTotal" name="odTotal" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.odTotal}" disabled/></td>
				<td align="right">OD/Tube:</td>
				<td><input id="odTB" name="odTB" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.odTB}" disabled/></td>
			</tr>
			<tr>
				<td align="right">nmole总量:</td>
				<td><input id="nmolTotal" name="nmolTotal" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.nmolTotal}" disabled/></td>
				<td align="right">nmole/Tube:</td>
				<td><input id="nmolTB" name="nmolTB" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.nmolTB}" disabled/></td>
			</tr>
			<tr>
				<td align="right">碱基数:</td>
				<td><input id="tbn" name="tbn" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.tbn}" disabled/></td>
				<td align="right">分装管数:</td>
				<td><input id="tb" name="tb" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.tb}" disabled/></td>
			</tr>
			<tr>
				<td align="right">修饰价格:</td>
				<td><input id="modiPrice" name="modiPrice" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.modiPrice}" disabled/></td>
				<td align="right">碱基单价:</td>
				<td><input id="baseVal" name="baseVal" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.baseVal}" disabled/></td>
			</tr>
			<tr>
				<td align="right">纯化价格:</td>
				<td><input id="purifyVal" name="purifyVal" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.purifyVal}" disabled/></td>
				<td align="right">总价格:</td>
				<td><input id="totalVal" name="totalVal" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.totalVal}" disabled/></td>
			</tr>
			<tr>
				<td align="right">描述:</td>
				<td><input id="remark" name="remark" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.remark}" disabled/></td>
				<td align="right">状态:</td>
				<td><input id="operationType" name="operationType" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.operationTypeDesc}" disabled/></td>
			</tr>
			<tr>
				<td align="right">板号:</td>
				<td><input id="boardNo" name="boardNo" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.boardNo}" disabled/></td>
				<td align="right">归属机构代码:</td>
				<td><input id="comCode" name="comCode" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.comCode}" disabled/></td>
			</tr>
			<tr>
				<td align="right">循环重回次数:</td>
				<td><input id="backTimes" name="backTimes" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.backTimes}" disabled/></td>
				<td align="right">测值体积:</td>
				<td><input id="measureVolume" name="measureVolume" class="readonly_inp" type="text" style="width: 80%" value="${primerProduct.measureVolume}" disabled/></td>
			</tr>
			<tr>
				<td align="right">最后操作时间:</td>
				<td><input id="modifyTime" name="modifyTime" class="readonly_inp" type="text" style="width: 80%" value="<fmt:formatDate value="${primerProduct.modifyTime}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/></td>
			</tr>              
                <tr>
                    <td colspan="4" height="10"></td>
                </tr>
            </table>
        </div>
       <div class="content_box info margin_btoom">
            <h2>操作轨迹信息</h2>
            <table width="100%" class="order_info" style="BORDER-COLLAPSE: collapse" borderColor=#000000 border="1">
                <tr>
                    <td align="center" width="5%">序号</td>
                    <td align="center" width="15%">操作类型</td>
                    <td align="center" width="10%">循环次数</td>
                    <td align="center" width="15%">操作人</td>
                    <td align="center" width="15%">操作时间</td>
                    <td align="center" width="40%">失败原因</td>
                </tr>
            <c:forEach var="ppo" items="${primerProduct.primerProductOperations}" varStatus="status">
            <tr>
                  <td align="center">${status.index}</td>
                  <td align="left">${ppo.typeDesc}</td>
                  <td align="left">${ppo.backTimes}</td>
                  <td align="left">${ppo.userName}</td>
                  <td align="left"><fmt:formatDate value="${ppo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                  <td align="left">${ppo.failReason}</td>
			</tr>
            </c:forEach>
            </table>
        </div>
        <div class="tools_bar">
            <button type="button" class="btn" onclick="history.back()">返回</button>
        </div>
    </div>
</form>
<script>
 
</script>
</body>
</html>