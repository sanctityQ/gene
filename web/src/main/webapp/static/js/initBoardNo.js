//初始化板号 查询方法
function initBoardNo(url){
	
    $.ajax({
        type:'post',
        url : "/gene/synthesis/initBoardNo",
        dataType:'json',
		data:{
			operationType: $("#operationType").val()
        },
        success:function(data){
            var initBoardNoList = $('#initBoardNoList');
            var html = '';
            if(data.length>0){
            	html  += '<ul class="xls_list"><li class="tip"><i class="icon-info-sign"></i>可选择的板号有：</li>';
            	for(var i = 0; i < data.length; i++){
            		var boardNo = data[i].boardNo;
            		html += '<li><i class="icon-paper-clip"></i><a href="javascript:;" onclick="lookBoardNo('+url+boardNo+')">'+boardNo+'</a></li>';
            	}
            	html += '</ul>';
            }
            alert(html);
            initBoardNoList.append(html);
        }
    })
};

var lookBoardNo=function(url){
	alert("111");
	goToPage(url);
}