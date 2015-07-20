
var deliveryLabelPrint = function(){
	var orderNo = $.trim($('#orderNo').val());
	
	if(orderNo == ""){
		alert("请输入订单号或生产编号。");
		return;
	}
	var url = $('#deliveryLabelfm').attr("action");
	var path = url+'?orderNo='+orderNo; 
	$('#deliveryLabelfm').attr("action", path).submit();
	$('#deliveryLabelfm').attr("action", url);
}