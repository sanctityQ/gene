<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
  <head>
    <title>订单修改</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <link href="resources/css/jquery-ui-themes.css" type="text/css" rel="stylesheet"/>
    <link href="resources/css/axure_rp_page.css" type="text/css" rel="stylesheet"/>
    <link href="../data/styles.css" type="text/css" rel="stylesheet"/>
    <link href="../files/upLoadModify/styles.css" type="text/css" rel="stylesheet"/>
    <script src="../resources/scripts/jquery-1.7.1.min.js"></script>
    <script src="../resources/scripts/jquery-ui-1.8.10.custom.min.js"></script>
    <script src="../resources/scripts/axure/axQuery.js"></script>
    <script src="../resources/scripts/axure/globals.js"></script>
    <script src="../resources/scripts/axutils.js"></script>
    <script src="../resources/scripts/axure/annotation.js"></script>
    <script src="../resources/scripts/axure/axQuery.std.js"></script>
    <script src="../resources/scripts/axure/doc.js"></script>
    <script src="../data/document.js"></script>
    <script src="../resources/scripts/messagecenter.js"></script>
    <script src="../resources/scripts/axure/events.js"></script>
    <script src="../resources/scripts/axure/action.js"></script>
    <script src="../resources/scripts/axure/expr.js"></script>
    <script src="../resources/scripts/axure/geometry.js"></script>
    <script src="../resources/scripts/axure/flyout.js"></script>
    <script src="../resources/scripts/axure/ie.js"></script>
    <script src="../resources/scripts/axure/model.js"></script>
    <script src="../resources/scripts/axure/repeater.js"></script>
    <script src="../resources/scripts/axure/sto.js"></script>
    <script src="../resources/scripts/axure/utils.temp.js"></script>
    <script src="../resources/scripts/axure/variables.js"></script>
    <script src="../resources/scripts/axure/drag.js"></script>
    <script src="../resources/scripts/axure/move.js"></script>
    <script src="../resources/scripts/axure/visibility.js"></script>
    <script src="../resources/scripts/axure/style.js"></script>
    <script src="../resources/scripts/axure/adaptive.js"></script>
    <script src="../resources/scripts/axure/tree.js"></script>
    <script src="../resources/scripts/axure/init.temp.js"></script>
    <script src="../files/upLoadModify/data.js"></script>
    <script src="../resources/scripts/axure/legacy.js"></script>
    <script src="../resources/scripts/axure/viewer.js"></script>

  </head>
  <body>
  <html>
  <head>
    <title>订单信息</title>
  </head>
  <body>
  <form id="orderInfoForm"  action="${ctx}/customer/query" method="post">
    <table>
    	<tr>
    		<td>
    			客户代码：<input type="text" name="customerName"/>
    		</td>
    	</tr>
    </table>
    <table>
    <thead></thead>
    <tbody>
    <c:forEach  var="customer"  items="${page.content}" varStatus="status">
	      <p>
	      	<span>&#149; 
<input type="text"  name="orderNo" size="50" value="${customer.code}" />
<input type="text"  name="customerName" size="50" value="${customer.invoiceTitle}" />
<input type="text"  name="status" size="50" value="${customer.webSite}" />

	      	</span>
	      </p>   
	</c:forEach>
	</tbody>
	</table>	
    <input type="submit" style="cursor: pointer;"/>
    </form>
  </body>
</html>
