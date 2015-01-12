$(function(){
    $('#boardSequence').on("click","li",sequenceClick);
    setBoardHeight();
    makeBoard('holeList','board.json');
    $("#holeList").on("click","div.hole_box",holesClick);
})
function sequenceClick(){
    $(this).addClass('selected').siblings().removeClass('selected');
}
function setBoardHeight(){
    var h = $(document).height() - 117;
    $('#board_box').height(h);
}
function makeBoard(id,url){
    var board = $('#'+id);
    var tBody = '';
    board.empty();
    $.ajax({
        url: url,
        type:'GET',
        dataType:'json',
        success: function(data){
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
    $.messager.alert('系统提示','合成板数据以保存！','',function(){
        goToPage('productionData.html');
    });
}
function holesClick(){
    $(this).toggleClass('selected');
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