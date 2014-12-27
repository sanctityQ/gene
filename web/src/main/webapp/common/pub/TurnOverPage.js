
function currPage( page ){
	document.forms[0].pageNo.value = page;
	document.forms[0].action = document.forms[0].preRequestPath.value;
	document.forms[0].submit();
  } 

function PrePage( page ){
	currPage( page-1 );
   }

function NextPage( page ){
	currPage( page+1 );
   }

function ChangePage(){

	var page = document.forms[0].changepage.value;
	if (CheckPage(page)) currPage(page);			    
}

function FirstPage(){

	var page = 1;	
	currPage(page);			    
}

function LastPage(){

	var page = document.forms[0].totalPage.value;
	currPage(page);			    
}

function CheckPage(pageNo){
  
  var page = parseInt(pageNo);	
  var totalPage=parseInt(document.forms[0].totalPage.value);
	
	if (isNaN(page))
	{
		alert("请输入合法的页数！");
    return false;
	}
	
	if (page.toString().length!=pageNo.length)
	{
		alert("请输入整数类型的页数！");
    return false;
	}
	
	if (page<1) 
	{
		alert("跳转的页数不能小于1！");
    return false;
	}
	
	if (page>totalPage) 
	{
		alert("跳转的页数不能大于总页数！");
    return false;
	}
	
	return true;
				    
}
