var bigIndex = undefined,bigToSmall = $('#bigToSmall');
function cellStyler(value,row,index){
    return 'color:red;';
}
function formatOper(val,row,index){
    return '<a href="javascript:;" onclick="copyRow(this)">复制</a>';
}
function bigendEditing(){
  if (bigIndex == undefined){return true}
  if (bigToSmall.datagrid('validateRow', bigIndex)){
    bigToSmall.datagrid('endEdit', bigIndex);
    bigIndex = undefined;
    return true;
  } else {
    return false;
  }
}
function appendRow(){
    bigToSmall.datagrid('appendRow',{
        row: {
            itemid: '新生产编号'
        }
    });
    var ind = $(".datagrid-btable").find('tr:last').attr("datagrid-row-index");
    bigToSmall.datagrid('beginEdit',ind);
}
function onClickRow(index){
  if (bigIndex != index){
    if (bigendEditing()){
      bigToSmall.datagrid('selectRow', index)
          .datagrid('beginEdit', index);
      bigIndex = index;
    } else {
      bigToSmall.datagrid('selectRow', bigIndex);
    }
  }
}
function getChanesSave(){
    var data = bigToSmall.datagrid('getChanges');
    alert(data.length + "条数据更改");
    goToPage('orderList.html');
}
function copyRow(e){
    var tr = $(e).parents('tr');
    var id = tr.attr('id');
    var ind = tr.index() + 1;
    var next = $(tr.clone());
    bigToSmall.datagrid('insertRow',{
        index: ind, // index start with 0
        row: {
            itemid: '新生产编号',
            productid: 30,
            listprice: 'some messages'
        }
    });
    bigToSmall.datagrid('beginEdit',ind);
}
/**
 * 初始化加载导入订单列表
 */
alert(orderNo)
getProduct();
$(document).ready(function() {
	alert('22222')
	$.ajax({
		type : "post",
		url : "gene/order/productQuery",
		dataType : "json",
		data:"orderNo="+orderNo,
		success : function(data) {
			if(data != null){
                $("#code").val(data.customerCode);
        		var total = data.primerProducts.length;
        		var reSultdata = data.primerProducts;
        		var jsonDate = $.parseJSON(reSultdata)
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#bigToSmall').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			alert("无法获取信息");
		}
	});
});
