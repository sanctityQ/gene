
var columnsNumber = 1;
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
    var customerCode = $('#customerCode').val();
    if(customerCode==""){alert("请录入正确的客户代码！");return false;}
    var customerName = $('#seachCustom').val();
//    var ary = [];
    var config = {}
    if(print.length==0){alert("您提交的配置生产标签列为空，请正确选择！");return false;}
    print.each(function(){
        var text = $(this).text();
    	var valueStr = $(this).attr("value");
    	config[valueStr] = text;
    })
//    ary.push(config);
//    console.log(JSON.stringify(config));
    $.ajax({
		type : "post",
        url: ctx + "/labelConfigure/configSave",
		dataType : "html",
        data: {
            "customerCode": customerCode,
            "customerName": customerName,
            "columnsNumber":columnsNumber,
            "configList":JSON.stringify(config)
        },
		success : function(data) {
			if(data == "sucess"){
				alert("生产标签配置保存成功！")
			}else{
				alert(data)
			}
		},
		error:function(data){
			alert("配置保存失败，请重试！");
		}
	});
}