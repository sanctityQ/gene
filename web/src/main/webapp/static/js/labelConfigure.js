var columnsNumber = 4;
$(function(){
    $('#columnsNumber').on('click','input',changeColumns)
    $('#setRows ul').on('click','li',setSelected)
});
function changeColumns(){
    var num = $(this).val();
    columnsNumber = num;
}
function setSelected(){
    $(this).toggleClass('selected');
}
//移动方法
function moveConfigure(direction){
    var configure = $('#configureList');
    var print = $('#printList');
    switch (direction){
        case 'left':
            var select = configure.children('li.selected');
            if(select.length){
                var total = print.children('li').length + select.length;
                print.append(select);
            }else{
                $.messager.alert('系统提示','您没有选择"待配置项"中的任何数据，请选择。');
            }
            break;
        case 'right':
            var select = print.children('li.selected');
            if(select.length){
                configure.append(select);
            }else{
                $.messager.alert('系统提示','您没有选择"生产标签"中的任何数据，请选择。');
            }
            break;
        case 'up':
            var select = print.children('li.selected');
            if(select.length){
                var next = select.first().prev();
                next.before(select);
            }else{
                $.messager.alert('系统提示','您没有选择"生产标签"中的任何数据，请选择。');
            }
            break;
        case 'down':
            var select = print.children('li.selected');
            if(select.length){
                var next = select.last().next();
                next.after(select);
            }else{
                $.messager.alert('系统提示','您没有选择"生产标签"中的任何数据，请选择。');
            }
            break;
    }
}
function saveConfigure(){
    var print = $('#printList').children('li');
    var ary = [];
    print.each(function(){
        var text = $(this).text();
        ary.push(text);
    })
    alert(ary);
}