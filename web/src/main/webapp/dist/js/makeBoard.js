var productNoArray = $('#productNoArray').val();
var boardNo = $('#boardNo').val();
$(function(){
    $('#boardSequence').on("click","li",sequenceClick);
    setBoardHeight();
    makeBoard('holeList','1','0');
    $("#holeList").on("click","div.hole_box",holesClick);
})
function sequenceClick(){
    $(this).addClass('selected').siblings().removeClass('selected');
}
function setBoardHeight(){
    var h = $(document).height() - 117;
    $('#board_box').height(h);
}
function makeBoard(id,flag,orderFlag){
	
	var board = $('#'+id);
    var tBody = '';
    board.empty();
    $.ajax({
    	url : "/gene/synthesis/makeBoardEdit",
        type:'POST',
        dataType:'json',
		data:{
			flag: flag,
			boardNo: $('#boardNo').val(),
			productNoStr: $('#productNoArray').val(),
			operationType:'synthesis',
			orderFlag: orderFlag
        },
        success: function(data){
            $('#boardType').val(data.boardType);
            var total = data.total;
            var rows = data.rows;
            $('#totals').text(total);
            for(var i = 0; i < rows.length; i++){
                var row = 'row' + (i+1);
                var hole = rows[i][row];
                var tr = '<tr>';
                for(var j = 0; j < hole.length; j++){
                    tr += '<td><div class="hole_box"><div class="hole">'+hole[j].No+'</div><div class="tag">'+hole[j].tag+'</div></div></td>';
                }
                tr += '</tr>';
                tBody += tr;
            }
            board.append(tBody);
        }
    });
}
function saveBoard(){
	var boardNo = $.trim($('#boardNo').val());
	
	if(boardNo == ""){
		alert("请输入板号。");
		return;
	}
	
	progress();
	
	var holeStr = "";
	var holeNos = $('#holeList').find('div.hole');
	var holeTags = $('#holeList').find('div.tag');

	holeNos.each(function(i){
        var hole = $(this).text();
        var tag = $(holeTags.eq(i)).text();
        if( hole != '' ){
        	holeStr += tag +":"+ hole +";";
        }
    });
	
	holeStr = holeStr.substring(0,holeStr.length-1);
	//alert(holeStr);
	
	$.ajax({
		type : "post",
		url : "/gene/synthesis/submitBoard",
		dataType : "json",
		data:{
			holeStr: holeStr,
			boardNo: boardNo,
			boardType: $('#boardType').val()
        },
		success:function(data) {
			$.messager.progress('close');
			if(data.success){
				$.messager.alert('系统提示',data.mess,'',function(){
					goToPage('/gene/views/synthesis/productionData.jsp');
				});
			}else{
				alert(data.mess);
			}
		},
		error:function(){
			$.messager.progress('close');
			alert("合成板数据保存失败！");
		}
	});
	
}
function holesClick(){
    var number = $(this).children('div.hole').text();
    if(number != ''){
        $(this).toggleClass('selected');
    }else{
        $.messager.alert('系统消息：','该孔没有生产编号，不能进行相关操作。')
    }
    var selects = $('#holeList').find('div.selected');
    var mutual = $('#mutualBtn');
    if(selects.length == 2){
        mutual.removeAttr('disabled');
    }else{
        mutual.attr('disabled','disabled');
    }
}
function exchangePosition(){
    var selects = $('#holeList').find('div.selected');
    var one = selects.eq(0).children('div.hole');
    var tow = selects.eq(1).children('div.hole');
    var oneText = '',towText = '';
    selects.each(function(i){
        var No = $(this).children('div.hole').text();
        if(i == 1){
            oneText = No;
        }else{
            towText = No;
        };
    });
    one.text(oneText);
    tow.text(towText);
}