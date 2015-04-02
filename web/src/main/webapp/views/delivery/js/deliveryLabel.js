
var deliveryLabelPrint = function(){
	var boardNo = $.trim($('#boardNoOrProductNo').val());
	var noType = $.trim($('#noType').val());
	
	if(boardNo == ""){
		alert("请输入板号或生产编号。");
		return;
	}
	var url = $('#deliveryLabelfm').attr("action");
	var path = url+'?boardNo='+boardNo+'&noType='+noType; 
	$('#deliveryLabelfm').attr("action", path).submit()
}