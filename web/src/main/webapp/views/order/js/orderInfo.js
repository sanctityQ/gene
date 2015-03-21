var bigIndex = undefined,bigToSmall = $('#bigToSmall'),editIndex = 0;
//中间修饰
var modiMidArr = "Cy5,TAMRA,ROX,Dabcyl,Biotin,Digoxin,FAM,";
//特殊单体
var modiSpeArr = "dI,dU,dT-NH2,Spacer(C12),*,5-Methyl dC,";
function cellStyler(value,row,index){
	if(value!=''){
      return 'color:red;';
    }else{
      return '';	
    }
	
}
function deletRow(index){
    var tr = bigToSmall.datagrid('selectRow',index);
    var row = bigToSmall.datagrid('getSelections');
    $.messager.confirm('系统消息','确认删除选中的数据?',function(ok){
        bigToSmall.datagrid('deleteRow',index);
    });
    editIndex = editIndex -1;
}
function formatOper(val,row,index){
	var toggle = row.del;
    if(toggle){
        return '<a href="javascript:;" onclick="deletRow('+index+')">删除</a>';
    }else{
    	if(row.nmolTotal>=50 || row.odTotal>=10){
	      return '<a href="javascript:;" onclick="copyRow(this)">复制</a>';
	    }
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
	var row = $('#bigToSmall').datagrid('getData').rows[0];
	bigToSmall.datagrid('insertRow',{
        index: 0,
        row: {
        	 del:true,
        	 productNo              :"",
             primeName              :"",
             geneOrder              :"",
             tbn                    :"",
             nmolTotal              :"",
             nmolTB                 :"",
             odTotal                :"",
             odTB                   :"",
             purifyType             :"",
             midi                   :row.midi,
             modiPrice              :row.modiPrice,
             baseVal                :row.baseVal,
             purifyVal              :row.purifyVal,
             totalVal               :"",
             fromProductNo          :row.productNo,
             comCode                :row.comCode
        }
    });
    var ind = $(".datagrid-btable").find('tr:first').attr("datagrid-row-index");
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
  bindGridEvent();
}

function bindGridEvent(){
	var geneOrderMidiObj = bigToSmall.datagrid('getEditor', {index:editIndex,field:'geneOrderMidi'});
	var geneOrderMidi = $(geneOrderMidiObj.target).parent().find('input.datagrid-editable-input');
    var geneOrderObj = bigToSmall.datagrid('getEditor', {index:editIndex,field:'geneOrder'});
    var tbnObj = bigToSmall.datagrid('getEditor', {index:editIndex,field:'tbn'});
    var geneOrder = $(geneOrderObj.target).parent().find('input.datagrid-editable-input');
    var tbn = $(tbnObj.target).parent().find('input.datagrid-editable-input');
    
    var modiMidiObj = bigToSmall.datagrid('getEditor', {index:editIndex,field:'modiMidType'});
    var modiSpeObj = bigToSmall.datagrid('getEditor', {index:editIndex,field:'modiSpeType'});
    //修饰单价
	var row = bigToSmall.datagrid('getSelected');
	var modiPriceVal = row.modiPrice;
	var baseValVal = row.baseVal;
	var purifyValVal = row.purifyVal;
	
	geneOrderMidi.bind("keyup",function(){
		var Str = getGeneOrder(geneOrderMidi.val());
		var modiMidi = convertModi(getmodiType(geneOrderMidi.val(),modiMidArr));
		var modiSpe = convertModi(getmodiType(geneOrderMidi.val(),modiSpeArr));
        var num = Str.length;
        $(geneOrderObj.target).val(Str);
        $(modiMidiObj.target).val(modiMidi);
        $(modiSpeObj.target).val(modiSpe);
        $(tbnObj.target).numberbox('setValue',num);
        calculateByGeneOrder(num,modiPriceVal,baseValVal,purifyValVal,editIndex);
     });
	
    geneOrder.bind("keyup",function(){
        var num = geneOrder.val().length;
        $(tbnObj.target).numberbox('setValue',num);
        calculateByGeneOrder(num,modiPriceVal,baseValVal,purifyValVal,editIndex);
    });
    tbn.bind("keyup",function(){
        calculateByGeneOrder(tbn.val(),modiPriceVal,baseValVal,purifyValVal,editIndex);
    });
    
}


var calculateByGeneOrder=function(tbnVal,modiPriceVal,baseValVal,purifyValVal,index){
	//总价格
    var totalObj = bigToSmall.datagrid('getEditor', {index:index,field:'totalVal'});
    var inp = $(totalObj.target).parent().find('input.datagrid-editable-input');
    //修饰单价修改计算 修饰单价+碱基单价*碱基数+纯化价格
	var mul = accMul(baseValVal,tbnVal);
	var addOne = accAdd(mul,purifyValVal);
    var total = accAdd(addOne,modiPriceVal);
    $(totalObj.target).numberbox('setValue',total);
    sumtotal(inp.val(),index);
}

var sumtotal=function(newTotalVal,index){
    var primerProducts = bigToSmall.datagrid('getData').rows;
    var sumTotal = 0;
    for(var i=0;i<primerProducts.length;i++){
    	//dataGrid 编辑状态下无法获取当前行最新值。将当前行不参与计算
    	if(i==index){continue;}
    	sumTotal = accAdd(sumTotal,primerProducts[i].totalVal);
    }
    //汇总当前行最新值，求和。
    sumTotal = accAdd(sumTotal,newTotalVal)
    $("#totalValue").html('<b class="bule">订单总计：</b>￥ '+sumTotal+'<br />');
}

function getGeneOrder(str){
	var wordin = new Array();
	var wordout = new Array();
	var m=0,n=0; p=0;
    var count=0
    
    if(str_repeat("(",str)!=str_repeat(")",str)){
    	//alert("您录入的修饰方式格式有误，请您核实()是否成对出现!");
    	return str;
    }
    
	if(str.indexOf("(")<0 && str.indexOf(")")<0){
		return str;
	}

    for(var i=0;i<str.length;i++){ 
        if(str.charAt(i)=='('){ 
            if(count==0){ 
                m=i; 
				
				wordout.push(str.substring(p,m));
            }
            count++; 
        } 
        if(str.charAt(i)==')'){ 
            count--; 
            if(count==0){ 
                n=i; 
				p=n+1;
				if(str.lastIndexOf(')')==n){
				  wordout.push(str.substring(p,str.length));
				}
            } 
        } 
    }
	var strout = "";
	for(var k=0;k<wordout.length;k++){
		strout = strout+wordout[k];
	}
	return strout;
}

//获取序列中的修饰
function getmodiStr(str){
	var result = "";
    var m=0, n=0; 
    var count=0; 
    if(str_repeat("(",str)!=str_repeat(")",str)){
    	//alert("您录入的修饰方式格式有误，请您核实()是否成对出现!");
    	return str;
    }
    
	if(str.indexOf("(")<0 && str.indexOf(")")<0){
		return str;
	}
    for(var i=0;i<str.length;i++){ 
        if(str.charAt(i)=='('){ 
            if(count==0){ 
                m=i; 
            } 
            count++; 
        } 
        if(str.charAt(i)==')'){ 
            count--; 
            if(count==0){ 
                n=i; 
                result = result+str.substring(m+1,n)+",";
            } 
        } 
    } 
    return result;
}

//将修饰类型分类 field 基础数据串
function getmodiType(str,field){
	var type = "";
	var moditype = getmodiStr(str);

	var moditypes = moditype.split(",");
	for(var i=0;i<moditypes.length;i++){
		if(moditypes[i]==''){continue;}
		if(field.indexOf(moditypes[i]+",")>-1){
		  type = type+moditypes[i]+",";
		}
	}
	
	return type;
}

//统计某字符串出现次数 find 要查找字符串  ;
function countModiType(find,str){
	var reg = new RegExp(find,"g")
	var c = str.match(reg);
	return c.length;
}

//将某一类修饰中相同的修饰个数统计，拼接字符串
function convertModi(str){
	var strs = str.split(",")
	var strArray = new Array();
	var hashArray = new Array();
	for(var i=0;i<strs.length;i++){
		if(strs[i]==''){continue;}
		strArray.push(strs[i])
	}
	var result="";
	for(var j=0;j<strArray.length;j++){
	  var counts = countModiType(strArray[j],str);
	  var value = "";
	  if(counts!=1){
		  value = countModiType(strArray[j],str)+strArray[j];
	  }else{
		  value = strArray[j];
	  }
	  if(result.indexOf(value)<0){
		result = result+value+",";
	  }
	} 
	
	return result;
}

function str_repeat(sub,geneOrder)
{
   var sum=0;
   for(var k=0;k<geneOrder.length;k++)

   {
       if(geneOrder.charAt(k)==sub)
       {
           sum+=1;
       }
   }
   return sum;
}

function isRepeat(arr){
    var hash = {};
    for(var i in arr) {
        if(hash[arr[i]])
             return true;
        hash[arr[i]] = true;
    }
    return false;
}

function getChanesSave(){
	bigToSmall.datagrid('endEdit',editIndex);
    var primerProducts = bigToSmall.datagrid('getData').rows;
    var productNos = new Array();
    for(var i=0;i<primerProducts.length;i++){
    	productNos.push(primerProducts[i].productNo);
    }
    
    if(isRepeat(productNos)){
    	alert("您提交的生产编号存在重复，请修改后重新提交！");
    }
//    console.log(primerProducts);
    progress();
    $.ajax({
		type : "post",
        url: ctx + "/order/save",
		dataType : "html",
        data: {
            "primerProducts": primerProducts,
            "orderNo": orderNo
        },
		success : function(data) {
			$.messager.progress('close');
			if(data == "sucess"){
				goToPage(ctx+'/views/order/orderList.jsp');
			}
		},
		error:function(data){
			$.messager.progress('close');
			if(data.status==500){
			  alert("您提交的生产编号存在重复，请修改后重新提交！")
			}else{
			  alert("数据保存失败，请重试！");
		    }
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
        del:true,
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
        midi				   :row.midi,
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
        modiSpeType            :row.modiSpeType,
        modiThreeType          :row.modiThreeType,
        operationType          :row.operationType,
        order                  :row.order,
        primeName              :row.primeName,
        remark                 :row.remark,
        }
    });
    for(var i=0;i<row.primerProductValues[ind].length;i++){
    	row.primerProductValues[i].id="";
    }
    bigToSmall.datagrid('beginEdit',ind);
    setTimeout(function(){
        tr.next().trigger('click');
    },100);
}

/**
 * 初始化加载导入订单列表
 */
var getProduct=function(){
	progress();
	$.ajax({
		type : "post",
		url : ctx + "/order/productQuery",
		dataType : "json",
		data:"orderNo="+orderNo,
		success : function(data) {
			$.messager.progress('close');
			if(data != null){
				$("#totalValue").html('<b class="bule">订单总计：</b>￥ '+data.order.totalValue+'<br />');
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
