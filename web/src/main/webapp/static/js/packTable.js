function exportPackTable(){
	
	var boardNo = $.trim($('#boardNo').val());
	
	if(boardNo == ""){
		alert("请输入板号。");
		return;
	}
	
	document.form.submit();

}