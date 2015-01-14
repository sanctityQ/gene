<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div id="scllor">
<div class="content">
<div class="easyui-accordion">
	<div title="订单管理" data-options="iconCls:'order'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/order/import" id="orderImport"><i class="icon-upload-alt"></i>订单导入</li>
			<li url="${ctx}/order/orderList" id="orderList"><i class="icon-file-text"></i>订单信息</li>
			<li url="${ctx}/order/orderExamine" id="orderExamine"><i class="icon-thumbs-up-alt"></i>订单审核</li>
		</ul>
	</div>
	<div title="安排合成" data-options="iconCls:'compound'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/synthesis/preMakeBoardQuery" id="syntheticPlate"><i class="icon-keyboard"></i>制作合成板</li>
			<li url="synthesisResults.html" id="synthesisResults"><i class="icon-edit-sign"></i>合成结果</li>
			<li url="machineTable.html" id="machineTable"><i class="icon-desktop"></i>导出上机表</li>
			<li url="packingTable.html" id="packingTable"><i class="icon-dropbox"></i>导出分装表</li>
		</ul>
	</div>
	<div title="修饰与氨解" data-options="iconCls:'adorn'" style="height:200px;">
		<ul class="sbu_menu">
			<li><i class="icon-star-half-full"></i>修饰</li>
			<li><i class="icon-th-large"></i>氨解</li>
		</ul>
	</div>
	<div title="纯化" data-options="iconCls:'purifying'" style="height:200px;">
		<ul class="sbu_menu">
			<li><i class="icon-pencil"></i>纯化结果</li>
		</ul>
	</div>
	<div title="测值" data-options="iconCls:'observed'" style="height:200px;">
		<ul class="sbu_menu">
			<li><i class="icon-pencil"></i>测值结果</li>
		</ul>
	</div>
	<div title="分装" data-options="iconCls:'packing'" style="height:200px;">
		<ul class="sbu_menu">
			<li><i class="icon-pencil"></i>分装结果</li>
		</ul>
	</div>
	<div title="烘干" data-options="iconCls:'dry'" style="height:200px;">
		<ul class="sbu_menu">
			<li><i class="icon-pencil"></i>烘干结果</li>
		</ul>
	</div>
	<div title="检测" data-options="iconCls:'test'" style="height:200px;">
		<ul class="sbu_menu">
			<li><i class="icon-upload-alt"></i>上传检测文件</li>
			<li><i class="icon-pencil"></i>检测结果</li>
		</ul>
	</div>
	<div title="发货" data-options="iconCls:'delivery'" style="height:200px;">
		<ul class="sbu_menu">
			<li><i class="icon-truck"></i>发货处理</li>
			<li><i class="icon-file-text-alt"></i>发货清单</li>
			<li><i class="icon-retweet"></i>发货召回</li>
		</ul>
	</div>
	<div title="报告单" data-options="iconCls:'report'" style="height:200px;">
		<ul class="sbu_menu">
			<li><i class="icon-file-text"></i>生成报告单</li>
		</ul>
	</div>
	<div title="信封打印" data-options="iconCls:'mail'" style="height:200px;">
		<ul class="sbu_menu">
			<li><i class="icon-print"></i>信封打印</li>
		</ul>
	</div>
	<div title="出库单打印" data-options="iconCls:'outbound'" style="height:200px;">
		<ul class="sbu_menu">
			<li><i class="icon-print"></i>出库单打印</li>
		</ul>
	</div>
	<div title="打印标签" data-options="iconCls:'print'" style="height:200px;">
		<ul class="sbu_menu">
			<li><i class="icon-bookmark"></i>生产标签</li>
			<li><i class="icon-cogs"></i>生产标签配置</li>
			<li><i class="icon-bookmark-empty"></i>发货标签</li>
		</ul>
	</div>
	<div title="业务员管理" data-options="iconCls:'salesman'" style="height:200px;">
		<ul class="sbu_menu">
			<li><i class="icon-cogs"></i>业务员管理</li>
		</ul>
	</div>
	<div title="客户管理" data-options="iconCls:'custom'" style="height:200px;">
		<ul class="sbu_menu">
			<li><i class="icon-cogs"></i>客户管理</li>
		</ul>
	</div>
	<div title="产品管理" data-options="iconCls:'product'" style="height:200px;">
		<ul class="sbu_menu">
			<li><i class="icon-cogs"></i>产品管理</li>
		</ul>
	</div>
</div>
</div>
</div>
<script src="${ctx}/static/js/menu.js" ></script>