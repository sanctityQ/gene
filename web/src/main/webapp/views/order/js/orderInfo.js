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
        	productNo: ''
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
    var primerProducts = bigToSmall.datagrid('getData').rows;
    console.log(primerProducts);

    $.ajax({
		type : "post",
		contentType: 'application/json',
        url: ctx + "/order/save",
		dataType : "json",
        data: {"primerProducts": primerProducts},
		success : function(data) {
			if(data != null){
				goToPage('/gene/views/order/orderList.jsp');
			}
		},
		error:function(){
			alert("数据保存失败，请重试！");
		}
	});
    
}
function copyRow(e){
    var tr = $(e).parents('tr');
    var id = tr.attr('id');
    var ind = tr.index() + 1;
    var next = $(tr.clone());
    var row = $('#bigToSmall').datagrid('getData').rows[tr.index()];
    //复制删除原row对象中id，保存时产生新id
    row.primerProductOperations[0].id="";
    for(var i=0;i<row.primerProductValues.length;i++){
    	row.primerProductValues[i].id="";
    }
    bigToSmall.datagrid('insertRow',{
        index: ind, // index start with 0
        row: {
        //复制页面展示数据
        productNo              :row.productNo+"1",
        primeName              :row.primeName,
        geneOrder              :row.geneOrder,
        tbn                    :row.tbn,
        purifyType             :row.purifyType,
        modiPrice              :row.modiPrice,
        baseVal                :row.baseVal,
        purifyVal              :row.purifyVal,
        totalVal               :row.totalVal,
        fromProductNo          :row.productNo,
        //复制收集全量数据，下面为行对象其他属性赋值。
        comCode                :row.comCode,
        geneOrder              :row.geneOrder,
        modiFiveType           :row.modiFiveType,
        modiMidType            :row.modiMidType,
        modiPrice              :row.modiPrice,
        modiSpeType            :row.modiSpeType,
        modiThreeType          :row.modiThreeType,
        operationType          :row.operationType,
        order                  :row.order,
        primeName              :row.primeName,
        primerProductOperations:row.primerProductOperations,
        primerProductValues    :row.primerProductValues,
        remark                 :row.remark,
        tbn                    :row.tbn,
        totalVal               :row.totalVal
        }
    });
    bigToSmall.datagrid('beginEdit',ind);
}

/**
 * 初始化加载导入订单列表
 */
var getProduct=function(orderNo){
	$.ajax({
		type : "post",
		url : "/gene/order/productQuery",
		dataType : "json",
		data:"orderNo="+orderNo,
		success : function(data) {
			if(data != null){
				/*赋值 begin*/
                $("#code").val(data.customer.code);
                $("#name").val(data.customer.name);
                $("#leaderName").val(data.customer.leaderName);
                $("#invoiceTitle").val(data.customer.invoiceTitle);
                $("#payWays").val(data.customer.payWays);
                $("#address").val(data.customer.address);
                $("#phoneNo").val(data.customer.phoneNo);
                $("#email").val(data.customer.email);
                $("#webSite").val(data.customer.webSite);
                $("#customerUnit").val(data.customer.invoiceTitle);
                
                $("#createTime").html("<b>订购日期：</b>"+data.orderPage.content[0].createTime);
                $("#totalValue").html("<b class='bule'>订单总计：</b>￥ "+data.orderPage.content[0].totalValue);
                /*赋值 end*/
        		var total = data.orderPage.content[0].primerProducts.length;
        		var reSultdata = data.orderPage.content[0].primerProducts;
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#bigToSmall').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			alert("无法获取信息");
		}
	});
}
