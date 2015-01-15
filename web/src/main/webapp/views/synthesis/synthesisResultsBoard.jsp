<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
<link href="css/easyui.css" type="text/css" rel="stylesheet" />
<link href="css/icon.css" type="text/css" rel="stylesheet" />
<link href="css/perfect-scrollbar.min.css" type="text/css" rel="stylesheet" />
<link href="css/master.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="css/font-awesome.min.css" />
<!--[if IE 7]>
  <link rel="stylesheet" href="css/font-awesome-ie7.min.css">
<![endif]-->
<script src="js/jquery.min.js" ></script>
<script src="js/jquery.easyui.min.js" ></script>
<script src="js/perfect-scrollbar.min.js" ></script>
<script src="js/index.js" ></script>
</head>
<body>
<style>
    .panel-tool{display: none;}
</style>
<div class="tools">
	<table width="100%">
		<tr>
			<td align="right" width="100">合成板名称:</td>
			<td>梓熙生物GHL-2015-01-23</td>
		</tr>
        <tr>
            <td colspan="4" height="10"></td>
        </tr>
	</table>
    <div class="btn_group">
        <p class="right pipe_number">NOML / TUB * 96</p>
        <button class="btn" id="selectAll" onclick="selectAll(this);">全选</button>
        <button class="btn btn-success" onclick="setSucceed(true);">成功</button>
        <button class="btn btn-danger" onclick="setSucceed(false);">失败</button>
    </div>
</div>
<div id="board_box">
    <div class="board_padding">
        <table width="100%" id="holeList"></table>
    </div>
</div>
<div class="tools_bar tabbing">
    <button class="btn" onclick="goToPage('synthesisResults.html');" type="">取 消</button>
    <button class="btn btn-primary" onclick="saveBoard();" type="">保 存</button>
</div>
<div id="inputCause" class="easyui-dialog" data-options="closed:true"><textarea class="inp_text" style="width: 376px;height: 102px;"></textarea></div>
<script src="js/synthesisResultsBoard.js" ></script>
</body>
</html>