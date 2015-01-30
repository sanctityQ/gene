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
function boardEdit(id,operationType){
    var board = $('#'+id);
    var tBody = '';
    board.empty();
    $.ajax({
    	url : "/gene/synthesis/boardEdit",
        type:'POST',
        dataType:'json',
		data:{
			operationType:operationType,
			boardNo: $('#boardNo').val()
        },
        success:function(data){
        	var typeFlag = data.typeFlag;
        	var typeDesc = data.typeDesc;
            var total = data.total;
            var rows = data.rows;
            //$('#totals').text(total);
            for(var i = 0; i < rows.length; i++){
                var row = 'row' + (i+1);
                var hole = rows[i][row];
                var tr = '<tr>';
                for(var j = 0; j < hole.length; j++){
                    var identifying = hole[j].identifying;
                    if(identifying != ''){
                        tr += '<td><div class="hole_box"><div class="hole">'+hole[j].No+'</div><div class="identifying">'+hole[j].identifying+'</div><div class="tag">'+hole[j].tag+'</div></div></td>';
                    }else{
                    	tr += '<td><div class="hole_box"><div class="hole">'+hole[j].No+'</div><div class="tag">'+hole[j].tag+'</div><div class="reason" style="display:none"></div></div></td>';
                    }
                }
                tr += '</tr>';
                tBody += tr;
            }
            if(typeFlag == '1'){
            	board.append(tBody);
            }else{
            	$.messager.alert("系统提示", "所选择板号有不符合可"+typeDesc+"状态的生产数据，请确认！");
            }
            //默认都成功
            //$("#holeList").find('div.hole_box').addClass('selected');
            //setSucceed(true);
        },
		error:function(){
			alert("查询数据失败，请重试！");
		}
    });
}
function saveBoard(operationType){
	
    var boardHoles = saveBoardData();

    $.ajax({
    	url : "/gene/synthesis/submitBoardEdit",
    	type : "post",
        dataType : "json",
        data: {
        	"operationType":operationType,
        	"boardHoles":boardHoles,
        	"boardNo":$('#boardNo').val()
        },
		success : function(data) {
			if(data != null){
			    $.messager.alert('系统提示','合成板数据已保存！','',function(){
			    	if(operationType == 'measure'){
			    		goToPage('/gene/synthesis/measureResults');
			    	}else{
			    		goToPage('/gene/views/synthesis/'+operationType+'Results.jsp');
			    	}
			    });
			}
		},
		error:function(){
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
            	//alert(holeNo+"  "+productNo+"  "+failFlag);
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
                        var ary = [];
                        var text = $('#inputCause .inp_text').val();
                        $('#inputCause').dialog('close');
                        selects.each(function(){
                            var status = $(this);
                            var tag = status.children('div.tag');
                            var No = status.children('div.hole').text();
                            if(!status.hasClass(result)){
                                status.removeClass().addClass('hole_box ' + result);
                            }else{
                                status.removeClass('selected');
                            };
                            status.children('div.reason').text(text);
                            tag.prepend(icon);
                            ary.push(No);
                        });
                        //alert('选中:'+ary+"\n原因:"+text)
                    }
                }]
            });
        }else{
            selects.each(function(){
                var status = $(this);
                var tag = status.children('div.tag');
                if(!status.hasClass(result)){
                    status.removeClass().addClass('hole_box ' + result);
                }else{
                    status.removeClass('selected');
                };
                tag.prepend(icon);
            })
        };
    }else{
        $.messager.alert('系统提示','您没有选中任何孔号，请选择相应的孔号。')
    };
    $('#selectAll').text('全选');
}

function setSucceedDouble(ok,lose){
    var selects = $('#holeList').find("div.selected");
    var result = '',icon = '',identifying = null;
    if(ok){
        result = 'succeed';
        icon = '<i class="icon-ok"></i>';
    }else{

        icon = '<i class="icon-remove"></i>';
        if(lose == 'compose'){
            result = 'compounded';
            identifying = '<div class="compound icon-mail-reply-all"></div>';
        }else{
            result = 'regain';
            identifying = '<div class="compound icon-dropbox"></div>';
        }
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
                            var tag = status.children('div.tag');
                            if(!status.hasClass(result)){
                                status.removeClass().addClass('hole_box ' + result);
                            }else{
                                status.removeClass('selected');
                            };
                            $(this).append(identifying);
                            tag.prepend(icon);
                        })
                    }
                }]
            });
        }else{
            selects.each(function(){
                var status = $(this);
                var tag = status.children('div.tag');
                if(!status.hasClass(result)){
                    status.removeClass().addClass('hole_box ' + result);
                }else{
                    status.removeClass('selected');
                };
                tag.prepend(icon);
            })
        };
    }else{
        $.messager.alert('系统提示','您没有选中任何孔号，请选择相应的孔号。')
    };
    $('#selectAll').text('全选');
}