<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<script type="text/javascript"src="${ctx}/static/jquery/jquery-1.7.2.js"></script>
<script type="text/javascript"src="${ctx}/static/jquery/json2.js"></script>
<script type="text/javascript">
var outbound = {
  "customerName":"",
  "customerPhoneNm":"",
  "orderNo":"",
  "handlerCode":"",
  "createTime":"",
  "makingNo":"",
  "operatorCode":"",
  "linkName":"",
  "makingDate":"",
  "commodityCode":"",
  "commodityName":""
}
function ajaxSubmit() {
	var outboundList = new Array();
	var i =0;
	jQuery("input[name='orderCheck']:checked").each(function(index){
      if ("checked" == jQuery(this).attr("checked")) {
          var index = this.value;
          outbound.customerName = jQuery("input[name='orderInfos["+index+"].customerName']")[0].value;
          outbound.customerPhoneNm = jQuery("input[name='orderInfos["+index+"].customerPhoneNm']")[0].value;
          outbound.orderNo = jQuery("input[name='orderInfos["+index+"].orderNo']")[0].value;
          outbound.handlerCode = jQuery("input[name='orderInfos["+index+"].handlerCode']")[0].value;
          outbound.createTime = jQuery("input[name='orderInfos["+index+"].createTime']")[0].value;
          outbound.makingNo = jQuery("input[name='orderInfos["+index+"].makingNo']")[0].value;
          outbound.operatorCode = jQuery("input[name='orderInfos["+index+"].operatorCode']")[0].value;
          outbound.linkName = jQuery("input[name='orderInfos["+index+"].linkName']")[0].value;
          outbound.makingDate = jQuery("input[name='orderInfos["+index+"].makingDate']")[0].value;
          outbound.commodityCode = jQuery("input[name='orderInfos["+index+"].commodityCode']")[0].value;
          outbound.commodityName = jQuery("input[name='orderInfos["+index+"].commodityName']")[0].value;
          outboundList[i]=outbound;
          i++
      }
	});
    $.ajax({
    	async:false,
        url: "${ctx}/print/printOutBound",
        type: "POST",
        data:JSON.stringify(outboundList),
        dataType: "json",
        success: function(data) {
            /* alert(data.length);
            for(var i=0;i<data.length;i++){
                alert(data[i].propertyPath +":"+data[i].message+"<br/>");
            } */
        }
    });
}
</script>
</head>

<body>
	<form id="inputForm"
	action="${ctx}/print/printOutBoundQuery" method="post" class="form-horizontal">
	    <table>
    	<tr>
    		<td>
    			订单号：<input type="text" name="orderNo"/>
    		</td>
    		<td>
    			客户代码：<input type="text" name="customerCode"/>
    		</td>
    	</tr>
    </table>
    <c:forEach  var="order"  items="${page.content}" varStatus="status">
	      <p>
	      	<span>&#149; 
<input type="checkbox"  name="orderCheck" size="50" value="${status.index}" onclick=""/>
<input type="text"  name="orderInfos[${status.index}].orderNo" size="50" value="${order.orderNo}" />
<input type="text"  name="orderInfos[${status.index}].customerName" size="50" value="${order.customerName}" />
<input type="text"  name="orderInfos[${status.index}].status" size="50" value="${order.status}" />
<input type="text"  name="orderInfos[${status.index}].productNoMinToMax" size="50" value="${order.productNoMinToMax}" />
<input type="text"  name="orderInfos[${status.index}].tbnTotal" size="50" value="${order.tbnTotal}" />
<input type="text"  name="orderInfos[${status.index}].createTime" size="50" value="${order.createTime}" />
<input type="text"  name="orderInfos[${status.index}].modifyTime" size="50" value="${order.modifyTime}" />
<input type="text"  name="orderInfos[${status.index}].customerPhoneNm" size="50" value="${order.customerPhoneNm}" />
<input type="text"  name="orderInfos[${status.index}].handlerCode" size="50" value="${order.handlerCode}" />
<input type="text"  name="orderInfos[${status.index}].makingNo" size="50" value="${order.makingNo}" />
<input type="text"  name="orderInfos[${status.index}].operatorCode" size="50" value="${order.operatorCode}" />
<input type="text"  name="orderInfos[${status.index}].linkName" size="50" value="${order.linkName}" />
<input type="text"  name="orderInfos[${status.index}].makingDate" size="50" value="${order.makingDate}" />
<input type="text"  name="orderInfos[${status.index}].commodityCode" size="50" value="${order.commodityCode}" />
<input type="text"  name="orderInfos[${status.index}].commodityName" size="50" value="${order.commodityName}" />
	      	</span>
	      </p>   
	</c:forEach>
    <input type="submit" style="cursor: pointer;"/>
    <input type="button" value="打印出库单" onclick="ajaxSubmit()" style="cursor: pointer;"/>
	</form>
</body>
</html>