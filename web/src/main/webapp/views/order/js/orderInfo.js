var bigIndex = undefined,bigToSmall = $('#bigToSmall'),editIndex = 0;
function cellStyler(value,row,index){
    return 'color:red;';
}
function formatOper(val,row,index){
	if(row.nmolTotal>=50 || row.odTotal>=10){
      return '<a href="javascript:;" onclick="copyRow(this)">复制</a>';
    }
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
        	
        }
    });
    var ind = $(".datagrid-btable").find('tr:last').attr("datagrid-row-index");
    bigToSmall.datagrid('beginEdit',ind);
}
function onClickRow(index){
  if (bigIndex != index){
    if (bigendEditing()){
      editIndex = index;
      bigToSmall.datagrid('selectRow', index)
          .datagrid('beginEdit', index);

      bigIndex = index;
    } else {
      bigToSmall.datagrid('selectRow', bigIndex);
    }
  }
  	//序列
    var geneOrder = bigToSmall.datagrid('getEditor', {index:index,field:'geneOrder'});
    //碱基数
	var tbn = bigToSmall.datagrid('getEditor', {index:index,field:'tbn'});
	var tbnVal = $(tbn.target).numberbox('getValue');
	//修饰单价
	var modiPrice = bigToSmall.datagrid('getEditor', {index:index,field:'modiPrice'});
	var modiPriceVal = $(modiPrice.target).numberbox('getValue');
	//碱基单价
    var baseVal = bigToSmall.datagrid('getEditor', {index:index,field:'baseVal'});
    var baseValVal = $(baseVal.target).numberbox('getValue');
    //纯化价格
    var purifyVal = bigToSmall.datagrid('getEditor', {index:index,field:'purifyVal'});
    var purifyValVal = $(purifyVal.target).numberbox('getValue');
    //总价格
    var totalObj = bigToSmall.datagrid('getEditor', {index:index,field:'totalVal'});
    //自动计算
    geneOrder.target.textbox({
    	onChange : function(){
    		$(tbn.target).numberbox('setValue',this.value.length);
    		calculateByGeneOrder(this.value.length,modiPriceVal,baseValVal,purifyValVal,totalObj);
        }
    });
    modiPrice.target.numberbox({
    	onChange : function(){
    		calculateByGeneOrder(tbnVal,this.value,baseValVal,purifyValVal,totalObj);
        }
    });
    baseVal.target.numberbox({
    	onChange : function(){
    		calculateByGeneOrder(tbnVal,modiPriceVal,this.value,purifyValVal,totalObj);
        }
    });
    purifyVal.target.numberbox({
    	onChange : function(){
    		calculateByGeneOrder(tbnVal,modiPriceVal,baseValVal,this.value,totalObj);
        }
    });
    
}

var calculateByGeneOrder=function(tbnVal,modiPriceVal,baseValVal,purifyValVal,totalObj){
	//修饰单价修改计算 修饰单价+碱基单价*碱基数+纯化价格
	var mul = accMul(baseValVal,tbnVal);
	var addOne = accAdd(mul,purifyValVal);
    var total = accAdd(addOne,modiPriceVal);
    $(totalObj.target).numberbox('setValue',total);
    var primerProducts = bigToSmall.datagrid('getData').rows;
    var sumTotal = 0;
    for(var i=0;i<primerProducts.length;i++){
    	sumTotal = accAdd(sumTotal,primerProducts[i].totalVal);
    }
    console.log("sumTotal="+sumTotal);
}

function getChanesSave(){
	bigToSmall.datagrid('endEdit',editIndex);
    var primerProducts = bigToSmall.datagrid('getData').rows;
    console.log(primerProducts);
    $.ajax({
		type : "post",
        url: ctx + "/order/save",
		dataType : "html",
        data: {
            "primerProducts": primerProducts,
            "orderNo": orderNo
        },
		success : function(data) {
			if(data == "sucess"){
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
    
    bigToSmall.datagrid('insertRow',{
        index: ind, // index start with 0
        row: {
        //复制页面展示数据
        productNo              :row.productNo+"1",
        primeName              :row.primeName,
        geneOrder              :row.geneOrder,
        tbn                    :row.tbn,
        nmolTotal              :"",
        nmolTB                 :"",
        odTotal                :"",
        odTB                   :"",
        purifyType             :row.purifyType,
        modiPrice              :row.modiPrice,
        baseVal                :row.baseVal,
        purifyVal              :row.purifyVal,
        totalVal               :"",
        fromProductNo          :row.productNo,
        //复制收集全量数据，下面为行对象其他属性赋值。
        comCode                :row.comCode,
        geneOrder              :row.geneOrder,
        modiFiveType           :row.modiFiveType,
        modiMidType            :row.modiMidType,
        modiPrice              :"",
        modiSpeType            :row.modiSpeType,
        modiThreeType          :row.modiThreeType,
        operationType          :row.operationType,
        order                  :row.order,
        primeName              :row.primeName,
        remark                 :row.remark
        //primerProductOperations:"",
        //primerProductValues    :""
        }
    });
    //row.primerProductOperations[ind].id="";
    for(var i=0;i<row.primerProductValues[ind].length;i++){
    	row.primerProductValues[i].id="";
    }
    bigToSmall.datagrid('beginEdit',ind);
}

/**
 * 初始化加载导入订单列表
 */
var getProduct=function(){
	$.ajax({
		type : "post",
		url : ctx + "/order/productQuery",
		dataType : "json",
		data:"orderNo="+orderNo,
		success : function(data) {
			if(data != null){
        		var total = data.order.primerProducts.length;
        		var reSultdata = data.order.primerProducts;
        		var jsonsource = {total: total, rows: reSultdata};
        		$('#bigToSmall').datagrid("loadData",jsonsource);
			}
		},
		error:function(){
			alert("无法获取信息");
		}
	});

}
/*加法*/
var accAdd = function(arg1,arg2){ 
	var r1,r2,m; 
	try{
	  r1=arg1.toString().split(".")[1].length
	}catch(e){
	  r1=0
	} 
	try{
	  r2=arg2.toString().split(".")[1].length
	}catch(e){
	  r2=0
	} 
	m=Math.pow(10,Math.max(r1,r2)) 
	return (arg1*m+arg2*m)/m 
} 
/*乘法*/
var accMul = function(arg1,arg2) 
{ 
var m=0,s1=arg1.toString(),s2=arg2.toString(); 
try{m+=s1.split(".")[1].length}catch(e){} 
try{m+=s2.split(".")[1].length}catch(e){} 
return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m) 
}
