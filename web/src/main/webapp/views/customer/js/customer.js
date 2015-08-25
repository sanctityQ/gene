function delRow(obj){
	//参数为A标签对象   
	var row = obj.parentNode.parentNode; //A标签所在行    
	var tb = row.parentNode; 	//当前表格   
	var rowIndex = row.rowIndex; //A标签所在行下标   
	tb.deleteRow(rowIndex-1);    //删除当前行}
}

var customerSave=function(){
	var messAge = '';
	if($("#customercode").val()==''){
		messAge += "请录入客户代码。\n";
	}
	if($("#customername").val()==''){
		messAge += "请录入客户公司。\n";
	}
	if($("#customerFlag").val()==''){
		messAge += "请选择客户性质。\n";
	}
	if($("#customerFlag").val()=='2' && $("#customerPrefix").val()!=''){
		messAge += "客户性质选择‘直接客户’时不需要录入生产编号开头，直接使用梓熙生物的配置。\n";
	}
	if($("#customerFlag").val()=='0'){
		if($("#haveZiXi").val()!='' && $("#haveZiXi").val()!=$("#customerid").val()){
    		messAge += "系统中客户性质已存在‘梓熙’。\n";
		}
		if($("#seachUserName").val()!=''){
    		messAge += "客户性质选择‘梓熙’时不需要录入业务员。\n";
		}
	}else{
		if($("#vagueUserCode").val()==''){
			messAge += "请选择业务员(点选下拉列表中的结果)。\n";
		}
	}
	//检验联系人信息必录
	var linkName = false;
	var inputs = document.getElementsByTagName("input");

	for (i=0;i<inputs.length;i++){
		var strname = inputs[i].name;

		if( strname.substr(strname.length-6, strname.length)  == '].name'){
			if(document.getElementsByName(strname)[0].value.trim() == ''){
				
				if(!linkName){
					messAge += "请录入‘联系人信息’的姓名。\n";
				}
				linkName = true;
			}
		}
	}
	
	if(messAge!=''){
		alert(messAge);
		return false;
	}
	$("#customerfm").submit();
}



//动态增加和删除表格行的内容
var addLinker=function(){
	   var maxIndex = $("#maxIndex").val();
	   maxIndex = parseInt(maxIndex);
	   $("#maxIndex").val(maxIndex+1)
	   
       //创建tr元素
       var trElemnet = document.createElement("tr");
       //创建td元素
       var td1Element = document.createElement("td");
       var td2Element = document.createElement("td");
       var td3Element = document.createElement("td");
       var td4Element = document.createElement("td");
       td4Element.align="center";
       //创建按钮
       var delElement = document.createElement("input");
       delElement.type="button";
       delElement.value="删除";
       delElement.className="btn";
       delElement.align="center";
       
       
       var inputElement1 = document.createElement("input");
       inputElement1.type="text";
       inputElement1.value="";
       inputElement1.name ="customer.customerContactss["+maxIndex+"].name";
       inputElement1.className="inp_text";
       inputElement1.style.width="90%";
       
       var inputElement2 = document.createElement("input");
       inputElement2.type="text";
       inputElement2.value="";
       inputElement2.name ="customer.customerContactss["+maxIndex+"].phoneNo";
       inputElement2.className="inp_text";
       inputElement2.style.width="90%";

       var inputElement3 = document.createElement("input");
       inputElement3.type="text";
       inputElement3.value="";
       inputElement3.name ="customer.customerContactss["+maxIndex+"].email";
       inputElement3.className="inp_text";
       inputElement3.style.width="90%";
       
       //为按钮添加单击事件
       delElement.onclick=function(){
           //删除按钮所在的tr对象
           trElemnet.parentNode.removeChild(trElemnet);
       }
       td4Element.appendChild(delElement);
       
       td1Element.appendChild(inputElement1);
       td2Element.appendChild(inputElement2);
       td3Element.appendChild(inputElement3);
       
       //将td元素添加到tr元素中
       trElemnet.appendChild(td1Element);
       trElemnet.appendChild(td2Element);
       trElemnet.appendChild(td3Element);
       trElemnet.appendChild(td4Element);
       
       //将tr元素添加到tbody元素中
       document.getElementById("tbodyID").appendChild(trElemnet);
 }