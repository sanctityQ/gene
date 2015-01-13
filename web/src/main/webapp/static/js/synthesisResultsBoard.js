$(function(){
    $('#boardSequence').on("click","li",sequenceClick);
    setBoardHeight();
    $("#holeList").on("click","div.hole_box",holesClick);
    makeBoard('holeList','board.json');
})
function sequenceClick(){
    $(this).addClass('selected').siblings().removeClass('selected');
}
function setBoardHeight(){
    var h = $(document).height() - 157;
    $('#board_box').height(h);
}
function holesClick(){
    $(this).toggleClass('selected');
}
function selectAll(e){
    var btn = $(e);
    if(btn.text() == '全选'){
        $("#holeList").find('div.hole_box').addClass('selected');
        btn.text('反选');
    }else{
        $("#holeList").find('div.hole_box').removeClass('selected');
        btn.text('全选');
    }

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
                    tr += '<td><div class="hole_box"><div class="hole">'+hole[j].No+'</div><div class="tag"><i class="icon-ok"></i>'+hole[j].tag+'</div></div></td>';
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
        goToPage('synthesisResults.html');
    });
}
function setSucceed(ok){
    var selects = $('#holeList').find("div.selected");
    var result = '';
    if(ok){
        result = 'succeed';
    }else{
        result = 'lose';
    }
    if(selects.length){
        if(!ok){
            $('#inputCause').dialog({
                title: '请输入失败原因',
                width: 400,
                height: 200,
                closed: false,
                cache: false,
                modal: true,
                buttons:[{
                    text:'取 消',
                    handler:function(){
                        $('#inputCause').dialog('close');
                        return false;
                    }
                },{
                    text:'确 定',
                    handler:function(){
                        var text = $('#inputCause .inp_text').val();
                        alert(text);
                        $('#inputCause').dialog('close');
                        selects.each(function(){
                            var status = $(this);
                            if(!status.hasClass(result)){
                                status.removeClass().addClass('hole_box ' + result);
                            }else{
                                status.removeClass('selected');
                            };
                        })
                    }
                }]
            });
        }else{
            selects.each(function(){
                var status = $(this);
                if(!status.hasClass(result)){
                    status.removeClass().addClass('hole_box ' + result);
                }else{
                    status.removeClass('selected');
                };
            })
        };
    }else{
        $.messager.alert('系统提示','您没有选中任何孔号，请选择相应的孔号。')
    };
    $('#selectAll').text('全选');
}
