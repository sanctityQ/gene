var bigIndex = undefined,bigToSmall = $('#bigToSmall'),editIndex = 0;
function cellStyler(value,row,index){
    return 'color:red;';
}
function cellStyler1(value,row,index){
    return 'background-color:#ffee00;';
}
function formatOper1(val,row,index){
    var text = val.toString();
    var iIndex = text.indexOf('I');
    var uIndex = text.indexOf('U');
    var span = null,newVal = null;
    if(!iIndex){
        span = '<span style="color: red;">I</span>';
        newVal = span + val.slice(iIndex + 1);
        return newVal;
    }else if(!uIndex){
        span = '<span style="color: red;">U</span>';
        newVal = span + val.slice(uIndex + 1);
        return newVal;
    }else{
        return val;
    }
}
function formatOper(val,row,index){
    var toggle = row.del;
    if(toggle){
        return '<a href="javascript:;" onclick="deletRow('+index+')">删除</a>';
    }else{
        return '<a href="javascript:;" onclick="copyRow(this)">复制</a>';
    }
}
function deletRow(index){
    var tr = bigToSmall.datagrid('selectRow',index);
    var row = bigToSmall.datagrid('getSelections');
    $.messager.confirm('系统消息','确认删除选中的数据?',function(ok){
        alert(row[0].productid)
        bigToSmall.datagrid('deleteRow',index);
    });
    editIndex = editIndex -1;
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
	alert("");
    bigToSmall.datagrid('insertRow',{
        index: 0,
        row: {
            del:true,
            itemid: 'EST-0',
            productid:'K9-DL-00'
        }
    });
    var ind = $(".datagrid-btable").find('tr:first');
    setTimeout(function(){
        bigToSmall.datagrid('beginEdit',editIndex+1);
        ind.trigger('click');
    },100);

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
    var ed = bigToSmall.datagrid('getEditor', {index:editIndex,field:'unitcost'});
    var listPrice = bigToSmall.datagrid('getEditor', {index:editIndex,field:'listprice'});
    var listNumber = $(listPrice.target).numberbox('getValue');
    var inp = $(ed.target).parent().find('input.datagrid-editable-input');
    inp.bind("keyup",function(){
        var number = inp.val();
        calculateFn($(listPrice.target),number,listNumber,editIndex);
    });
}
function calculateFn(target,onNumber,listNumber,rowIndex){
    var ed = bigToSmall.datagrid('getEditor', {index:rowIndex,field:'listprice'});
    var inp = $(ed.target).parent().find('input.datagrid-editable-input');
    var total = onNumber * 0.5 + listNumber;
    target.numberbox('setValue',total);
    alert(inp.val())
}
function getChanesSave(){
    bigToSmall.datagrid('endEdit',editIndex);
    var data = bigToSmall.datagrid('getChanges');
    alert('生产编号:'+data[0].itemid + '\n引物名称:'+data[0].productid);
    goToPage('orderList.html');
}
function copyRow(e){
    var tr = $(e).parents('tr');
    var id = tr.attr('id');
    var ind = tr.index() + 1;
    bigToSmall.datagrid('insertRow',{
        index: ind, // index start with 0
        row: {
            del:true,
            itemid: '新生产编号',
            productid: 30,
            listprice: 'some messages'
        }
    });
    setTimeout(function(){
        tr.next().trigger('click');
    },100);
}