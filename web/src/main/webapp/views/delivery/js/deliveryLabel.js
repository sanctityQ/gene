
var deliveryLabelPrint = function(){
	var boardNo = $.trim($('#boardNo').val());
	
	if(boardNo == ""){
		alert("请输入板号或生产编号。");
		return;
	}
	var url = $('#deliveryLabelfm').attr("action");
	var path = url+'?boardNo='+boardNo; 
	$('#deliveryLabelfm').attr("action", path).submit();
	$('#deliveryLabelfm').attr("action", url);
	
}