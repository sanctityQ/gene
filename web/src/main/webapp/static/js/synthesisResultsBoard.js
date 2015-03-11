$(function(){
    $('#boardSequence').on("click","li",sequenceClick);
    setBoardHeight();
    $("#holeList").on("click","div.hole_box",holesClick);
    synthesisBoard('holeList');
})
function sequenceClick(){
    $(this).addClass('selected').siblings().removeClass('selected');
}
function setBoardHeight(){
    var h = $(document).height() - 157;
    $('#board_box').height(h);
}
function holesClick(){
    var number = $(this).children('div.hole').text();
    if(number != ''){
        $(this).toggleClass('selected');
    }else{
        $.messager.alert('系统消息：','该孔没有生产编号，不能进行相关操作。')
    }
}
function selectAll(e){
    var btn = $(e);
    if(btn.text() == '全选'){
        $("#holeList").find('div.hole_box').each(function(){
            var box = $(this);
            var number = box.children('div.hole').text();
            if(number != ''){
                box.addClass('selected');
            }
        })
        btn.text('反选');
    }else{
        $("#holeList").find('div.selected').removeClass('selected');
        btn.text('全选');
    }
}
function synthesisBoard(id){
    var board = $('#'+id);
    var tBody = '';
    board.empty();
    $.ajax({
    	url : "/gene/synthesis/makeBoardEdit",
        type:'POST',
        dataType:'json',
		data:{
			flag: '',
			oldFlag:'1',
			boardNo: $('#boardNo').val(),
			productNoStr: '',
			operationType:'synthesis'
        },
        success: function(data){
            var total = data.total;
            var rows = data.rows;
            $('#totals').text(total);
            for(var i = 0; i < rows.length; i++){
                var row = 'row' + (i+1);
                var hole = rows[i][row];
                var tr = '<tr>';
                for(var j = 0; j < hole.length; j++){
                    tr += '<td><div class="hole_box"><div class="hole">'+hole[j].No+'</div><div class="tag">'+hole[j].tag+'</div><div class="reason" style="display:none"></div></div></td>';
                }
                tr += '</tr>';
                tBody += tr;
            }
            board.append(tBody);
            //默认都成功
            //$("#holeList").find('div.hole_box').addClass('selected');
            //setSucceed(true);
        }
    });
}
function saveBoard(){
	
    var boardHoles = saveBoardData();
    //console.log(boardHoles);

    var win = $.messager.progress({
        title:'系统消息',
        msg:'请稍候…',
        text:'页面保存中…'
    });
    
    $.ajax({
    	url : "/gene/synthesis/submitSynthesis",
    	type : "post",
        dataType : "json",
        data: {
        	"boardHoles":boardHoles,
        	"boardNo":$('#boardNo').val()
        },
		success : function(data) {
			if(data != null){
			    $.messager.alert('系统提示','合成板数据已保存！','',function(){
			        goToPage('/gene/views/synthesis/synthesisResults.jsp');
			    });
			}
		},
		error:function(){
			$.messager.progress('close');
			alert("数据保存失败，请重试！");
		}
	});

}
//保存合成板数据方法
function saveBoardData(){
    var holeList = $('#holeList').find('div.hole_box');
    var data = [];
    holeList.each(function(){
        var holeBox = $(this);
        var result = holeBox.attr('class');
        if(result != 'hole_box' && result != 'hole_box selected'){
            var holeNo = holeBox.children('div.tag').text();
            var productNo = holeBox.children('div.hole').text();
            var reason = holeBox.children('div.reason').text();
            var ary = '';
            var failFlag = '';
            switch (result){
                case 'hole_box succeed':
                	failFlag = '0';
                    ary = '成功';
                    break;
                case 'hole_box lose':
                	failFlag = '1';
                    ary = '失败';
                    break;
                case 'hole_box compounded':
                	failFlag = '2';
                    ary = '重新合成';
                    break;
                case 'hole_box regain':
                	failFlag = '3';
                    ary = '重新分装';
                    break;
            }
            if(productNo !=''){
            	//alert(holeNo+"  "+productNo+"  "+failFlag+"  "+reason);
            	data.push({
            		"holeNo":holeNo,
            		"primerProduct.productNo":productNo,
            		"failFlag":failFlag,
            		"remark":reason
            	});
            }
        }
    })
     //alert(data);
    return data;
}
function setSucceed(ok){
    var selects = $('#holeList').find("div.selected");
    var result = '',icon = '';
    if(ok){
        result = 'succeed';
        icon = '<i class="icon-ok"></i>';
    }else{
        result = 'lose';
        icon = '<i class="icon-remove"></i>';
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
                        $('#inputCause').dialog('close');
                        selects.each(function(){
                            var status = $(this);
                            var tag = status.children('div.tag');
                            if(!status.hasClass(result)){
                                status.removeClass().addClass('hole_box ' + result);
                                tag.children('i').remove();
                                tag.prepend(icon);
                            }else{
                                status.removeClass('selected');
                            };
                            status.children('div.reason').text(text);
                        });
                    }
                }]
            });
        }else{
            selects.each(function(){
                var status = $(this);
                var tag = status.children('div.tag');
                if(!status.hasClass(result)){
                    status.removeClass().addClass('hole_box ' + result);
                    tag.children('i').remove();
                    tag.prepend(icon);
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
