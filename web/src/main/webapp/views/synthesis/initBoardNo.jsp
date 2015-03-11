<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
 <div class="content_box">
     <h2>未处理列表</h2>
     <table id="boardData" class="easyui-datagrid" data-options="singleSelect: true,striped:true,method: 'post',pagination:true,fitColumns:true">
         <thead>
         <tr>
             <th data-options="field:'boardNo',width:100,sortable:true">板号</th>
             <th data-options="field:'boardType',width:50,sortable:true">板类型</th>
             <th data-options="field:'createTime',width:80,sortable:true">创建时间</th>
             <th data-options="field:'createUser',width:50,sortable:true">创建人</th>
             <th data-options="field:'operationTypeDesc',width:50,sortable:true">状态</th>
             <th data-options="field:'_operate',align:'center',width:100,formatter:formatOper">操作</th>
         </tr>
         </thead>
     </table>
<div id="inputCause" class="easyui-dialog" data-options="closed:true"><textarea class="inp_text" style="width: 376px;height: 102px;"></textarea></div>
 </div> 
<script type="text/javascript">

initBoardNo();

//初始化板号 查询方法
function initBoardNo(){

  $.ajax({
      type:'post',
      url : "/gene/synthesis/initBoardNo",
      dataType:'json',
		data:{
			operationType: $("#operationType").val()
      },
      success:function(data){
			if(data != null){
      		var total = data.totalElements;
      		var reSultdata = data.content;
      		var jsonsource = {total: total, rows: reSultdata};
      		$('#boardData').datagrid("loadData",jsonsource);
			}
      }
  })
};


//板号 查询方法
function dealBoardNo(){
	
	var gridOpts = $('#boardData').datagrid('getPager').data("pagination").options;
  
	$.ajax({
      type:'post',
      url : "/gene/synthesis/initBoardNo",
      dataType:'json',
		data:{
			operationType: $("#operationType").val()
			,
			pageNo: gridOpts.pageNumber,
			pageSize: gridOpts.pageSize
      },
      success:function(data){
			if(data != null){
      		var total = data.totalElements;
      		var reSultdata = data.content;
      		var jsonsource = {total: total, rows: reSultdata};
      		$('#boardData').datagrid("loadData",jsonsource);
			}
      }
  })
};


$(function(){
  var dg = $('#boardData');
  var opts = dg.datagrid('options');
  var pager = dg.datagrid('getPager');
  
  pager.pagination({
      pageSize:20,
      onSelectPage:function(pageNum, pageSize){
          opts.pageNumber = pageNum;
          opts.pageSize = pageSize;

          pager.pagination('refresh',{
              pageNumber:pageNum,
              pageSize:pageSize
          });
          dealBoardNo();
      }
  });
});

function formatOper(val,row,index){
	var row = $('#boardData').datagrid('getData').rows[index];
	var operationType = $("#operationType").val();
	var url = "'/gene/views/synthesis/"+operationType+"ResultsBoard.jsp?boardNo="+row.boardNo+"'";
  return '<a href="javascript:;"  onclick="goToPage('+url+')"><i class="icon-pencil"></i>录入结果</a>';
};

</script>