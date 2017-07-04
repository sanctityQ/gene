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
	
	var cpf = document.getElementsByName("customerPriceFlag");
	if(cpf.length==0){
		messAge += "请配置‘价格信息’。\n";
	}
	
	if(messAge!=''){
		alert(messAge);
		return false;
	}
	$("#customerfm").submit();
}



//动态增加和删除联系人表格行的内容
var addLinker=function(){
	   var maxLinkerIndex = $("#maxLinkerIndex").val();
	   maxLinkerIndex = parseInt(maxLinkerIndex);
	   $("#maxLinkerIndex").val(maxLinkerIndex+1)
	   
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
       inputElement1.name ="customer.customerContactss["+maxLinkerIndex+"].name";
       inputElement1.className="inp_text";
       inputElement1.style.width="90%";
       
       var inputElement2 = document.createElement("input");
       inputElement2.type="text";
       inputElement2.value="";
       inputElement2.name ="customer.customerContactss["+maxLinkerIndex+"].phoneNo";
       inputElement2.className="inp_text";
       inputElement2.style.width="90%";

       var inputElement3 = document.createElement("input");
       inputElement3.type="text";
       inputElement3.value="";
       inputElement3.name ="customer.customerContactss["+maxLinkerIndex+"].email";
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
       document.getElementById("tbodyLinker").appendChild(trElemnet);
 }

//动态增加和删除联系人表格行的内容
var addPrice=function(minTbnVal,maxTbnVal,minOdVal,maxOdVal,purifyType,baseVal,purifyVal){
	   var maxPriceIndex = $("#maxPriceIndex").val();
	   maxPriceIndex = parseInt(maxPriceIndex);
	   $("#maxPriceIndex").val(maxPriceIndex+1)
	   
       //创建tr元素
       var trElemnet = document.createElement("tr");
       //创建td元素
       var td1Element = document.createElement("td");
       var td2Element = document.createElement("td");
       var td3Element = document.createElement("td");
       var td4Element = document.createElement("td");
       var td5Element = document.createElement("td");
       var td6Element = document.createElement("td");
       var td7Element = document.createElement("td");
       
       var tdElementLast = document.createElement("td");
           tdElementLast.align="center";
       //创建按钮
       var delElement = document.createElement("input");
       delElement.type="button";
       delElement.value="删除";
       delElement.className="btn";
       delElement.align="center";
       
       
       var inputElement1 = document.createElement("input");
       inputElement1.type="text";
       inputElement1.value=minTbnVal;
       inputElement1.name ="customer.customerPrices["+maxPriceIndex+"].minTbn";
       inputElement1.className="inp_text";
       inputElement1.style.width="90%";
       
       var inputElement2 = document.createElement("input");
       inputElement2.type="text";
       inputElement2.value=maxTbnVal;
       inputElement2.name ="customer.customerPrices["+maxPriceIndex+"].maxTbn";
       inputElement2.className="inp_text";
       inputElement2.style.width="90%";

       var inputElement3 = document.createElement("input");
       inputElement3.type="text";
       inputElement3.value=minOdVal;
       inputElement3.name ="customer.customerPrices["+maxPriceIndex+"].minOd";
       inputElement3.className="inp_text";
       inputElement3.style.width="90%";
       
       var inputElement4 = document.createElement("input");
       inputElement4.type="text";
       inputElement4.value=maxOdVal;
       inputElement4.name ="customer.customerPrices["+maxPriceIndex+"].maxOd";
       inputElement4.className="inp_text";
       inputElement4.style.width="90%";
       
       var inputElement5 = document.createElement("select");
       inputElement5.options[0] = new Option("OPC", "OPC");
       inputElement5.options[1] = new Option("PAGE", "PAGE");
       inputElement5.options[2] = new Option("HPLC", "HPLC");
       
       inputElement5.options[purifyType].selected = true;
       inputElement5.name ="customer.customerPrices["+maxPriceIndex+"].purifyType";
       inputElement5.className="inp_text";
       inputElement5.style.width="90%";
       
       var inputElement6 = document.createElement("input");
       inputElement6.type="text";
       inputElement6.value=baseVal;
       inputElement6.name ="customer.customerPrices["+maxPriceIndex+"].baseVal";
       inputElement6.className="inp_text";
       inputElement6.style.width="90%";
       
       var inputElement7 = document.createElement("input");
       inputElement7.type="text";
       inputElement7.value=purifyVal;
       inputElement7.name ="customer.customerPrices["+maxPriceIndex+"].purifyVal";
       inputElement7.className="inp_text";
       inputElement7.style.width="90%";
       
       var inputElementHidden = document.createElement("input");
       inputElementHidden.type="hidden";
       inputElementHidden.name ="customerPriceFlag";
       
       //为按钮添加单击事件
       delElement.onclick=function(){
           //删除按钮所在的tr对象
           trElemnet.parentNode.removeChild(trElemnet);
       }
       tdElementLast.appendChild(delElement);
       
       td1Element.appendChild(inputElement1);
       td2Element.appendChild(inputElement2);
       td3Element.appendChild(inputElement3);
       td4Element.appendChild(inputElement4);
       td5Element.appendChild(inputElement5);
       td6Element.appendChild(inputElement6);
       td7Element.appendChild(inputElement7);
       td7Element.appendChild(inputElementHidden);
       
       //将td元素添加到tr元素中
       trElemnet.appendChild(td1Element);
       trElemnet.appendChild(td2Element);
       trElemnet.appendChild(td3Element);
       trElemnet.appendChild(td4Element);
       trElemnet.appendChild(td5Element);
       trElemnet.appendChild(td6Element);
       trElemnet.appendChild(td7Element);
       
       trElemnet.appendChild(tdElementLast);
       
       //将tr元素添加到tbody元素中
       document.getElementById("tbodyPrice").appendChild(trElemnet);
 }

var iniPrice=function(){
	
	var cpf = document.getElementsByName("customerPriceFlag");
	var execFlag = true;
	if(cpf.length>0){
		if(confirm("执行‘初始化价格数据’会覆盖已有价格数据，是否继续执行？")){
			execFlag = true;
		}else{
			execFlag = false;
		}
	}
	
	if(execFlag){
		document.getElementById("tbodyPrice").innerHTML="";//清空tbody
		$("#maxPriceIndex").val(0)
		
		//OPC
		addPrice('0.0','99.0', '0.0', '1.0',0,'0.30','0.0');
		addPrice('0.0','99.0', '1.0', '2.0',0,'0.40','0.0');
		addPrice('0.0','99.0', '2.0', '5.0',0,'0.50','0.0');
		addPrice('0.0','99.0', '5.0','10.0',0,'0.75','0.0');
		addPrice('0.0','99.0','10.0','20.0',0,'1.20','0.0');
		addPrice('0.0','99.0','20.0','30.0',0,'1.80','0.0');
		addPrice('0.0','99.0','30.0','40.0',0,'2.40','0.0');
		addPrice('0.0','99.0','40.0','50.0',0,'3.00','0.0');
		
		addPrice('100.0','300.0', '0.0', '2.0',0,'1.00','0.0');
		addPrice('100.0','300.0', '2.0', '5.0',0,'1.00','0.0');
		addPrice('100.0','300.0', '5.0','10.0',0,'1.20','0.0');
		addPrice('100.0','300.0','10.0','20.0',0,'2.40','0.0');
		addPrice('100.0','300.0','20.0','30.0',0,'3.60','0.0');
		addPrice('100.0','300.0','30.0','40.0',0,'4.80','0.0');
		addPrice('100.0','300.0','40.0','50.0',0,'6.00','0.0');
		
		//PAGE
		addPrice('0.0','99.0', '0.0', '2.0',1,'0.80','0.0');
		addPrice('0.0','99.0', '2.0', '5.0',1,'1.20','0.0');
		addPrice('0.0','99.0', '5.0','10.0',1,'1.80','0.0');
		addPrice('0.0','99.0','10.0','20.0',1,'1.92','0.0');
		addPrice('0.0','99.0','20.0','30.0',1,'2.88','0.0');
		addPrice('0.0','99.0','30.0','40.0',1,'3.84','0.0');
		addPrice('0.0','99.0','40.0','50.0',1,'4.80','0.0');
		
		addPrice('100.0','300.0', '0.0', '2.0',1,'2.00','0.0');
		addPrice('100.0','300.0', '2.0', '5.0',1,'3.00','0.0');
		addPrice('100.0','300.0', '5.0','10.0',1,'3.20','0.0');
		addPrice('100.0','300.0','10.0','20.0',1,'4.80','0.0');
		addPrice('100.0','300.0','20.0','30.0',1,'6.40','0.0');
		addPrice('100.0','300.0','30.0','40.0',1,'8.00','0.0');
		addPrice('100.0','300.0','40.0','50.0',1,'9.60','0.0');
		
		//HPLC
		addPrice('0.0','99.0', '0.0', '2.0',2,'0.40', '50.0');
		addPrice('0.0','99.0', '2.0', '5.0',2,'0.50', '50.0');
		addPrice('0.0','99.0', '5.0','10.0',2,'0.75', '80.0');
		addPrice('0.0','99.0','10.0','20.0',2,'1.20','110.0');
		addPrice('0.0','99.0','20.0','30.0',2,'1.80','140.0');
		addPrice('0.0','99.0','30.0','40.0',2,'2.40','170.0');
		addPrice('0.0','99.0','40.0','50.0',2,'3.00','200.0');
		
		addPrice('100.0','300.0', '0.0', '2.0',2,'1.00','0.0');
		addPrice('100.0','300.0', '2.0', '5.0',2,'1.00','0.0');
		addPrice('100.0','300.0', '5.0','10.0',2,'1.20','0.0');
		addPrice('100.0','300.0','10.0','20.0',2,'2.40','0.0');
		addPrice('100.0','300.0','20.0','30.0',2,'3.60','0.0');
		addPrice('100.0','300.0','30.0','40.0',2,'4.80','0.0');
		addPrice('100.0','300.0','40.0','50.0',2,'6.00','0.0');
		
	}
}