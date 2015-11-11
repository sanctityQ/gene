<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
String customerFlag = user.getUser().getCustomer().getCustomerFlag();
boolean isZiXi = false;
if ("0".equals(customerFlag)){
	isZiXi = true;
}
%>
<div id="scllor">
<div class="content">
<div class="easyui-accordion">
	<div title="订单管理" data-options="iconCls:'order'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/order/import" id="orderImport"><i class="icon-upload-alt"></i>订单导入</li>
			<li url="${ctx}/order/orderList" id="orderList"><i class="icon-file-text"></i>订单信息</li>
<% if (isZiXi){%>
			<li url="${ctx}/order/orderExamine" id="orderExamine"><i class="icon-thumbs-up-alt"></i>订单审核</li>
			<li url="${ctx}/order/orderAudit" id="orderAudit"><i class="icon-file-text"></i>修改订单信息</li>
<% }%>
		</ul>
	</div>
<% if (isZiXi){%>
	<div title="安排合成" data-options="iconCls:'compound'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/synthesis/productionData" id="productionData"><i class="icon-keyboard"></i>制作合成板</li>
			<li url="${ctx}/synthesis/heChengZhuPaiBan"  id="heChengZhuPaiBan"><i class="icon-th-large"></i>合成柱排版</li>
			<li url="${ctx}/synthesis/synthesisResults" id="synthesisResults"><i class="icon-edit-sign"></i>合成结果</li>
			<li url="${ctx}/synthesis/machineTable" id="machineTable"><i class="icon-desktop"></i>导出上机表</li>
			<li url="${ctx}/synthesis/packTable" id="packTable"><i class="icon-dropbox"></i>导出分装表</li>
		</ul>
	</div>
	<div title="修饰与氨解" data-options="iconCls:'adorn'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/synthesis/decorate" id="decorate"><i class="icon-star-half-full"></i>修饰</li>
			<li url="${ctx}/synthesis/ammoniaResults" id="aminolysis"><i class="icon-th-large"></i>氨解</li>
			<li url="${ctx}/statistics/hplcChunHuaBiao" id="hplcChunHuaBiao"><i class="icon-th-large"></i>HPLC纯化表</li>
		</ul>
	</div>
	<div title="纯化" data-options="iconCls:'purifying'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/synthesis/purifyResults" id="aminolysis1"><i class="icon-pencil"></i>纯化结果</li>
		</ul>
	</div>
	<div title="测值" data-options="iconCls:'observed'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/synthesis/measureResults" id="measuredValue"><i class="icon-pencil"></i>测值结果</li>
		</ul>
	</div>
	<div title="分装" data-options="iconCls:'packing'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/synthesis/packResults" id="aminolysis2"><i class="icon-pencil"></i>分装结果</li>
		</ul>
	</div>
	<div title="烘干" data-options="iconCls:'dry'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/synthesis/bakeResults" id="aminolysis3"><i class="icon-pencil"></i>烘干结果</li>
		</ul>
	</div>
	<div title="检测" data-options="iconCls:'test'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/synthesis/detectResults" id="aminolysis4"><i class="icon-upload-alt"></i>上传检测文件</li>
			<li url="${ctx}/synthesis/detectResultsBoard" id="detectionResult"><i class="icon-pencil"></i>检测结果</li>
		</ul>
	</div>
	<div title="发货" data-options="iconCls:'delivery'" style="height:250px;">
		<ul class="sbu_menu">
			<li url="${ctx}/delivery/deliveryResults" id="consignmentProcessing"><i class="icon-truck"></i>发货处理</li>
			<li url="${ctx}/delivery/deliveryRecall" id="deliveryRecall"><i class="icon-retweet"></i>发货召回</li>
			<li url="${ctx}/delivery/deliveryList" id="deliveryList"><i class="icon-file-text-alt"></i>发货清单</li>
		    <li url="${ctx}/statistics/chuKuTongJi" id="chuKuTongJi"><i class="icon-th-large"></i>出库统计</li>
		    <li url="${ctx}/statistics/duiZhangDan" id="duiZhangDan"><i class="icon-th-large"></i>对账单</li>
		    <li url="${ctx}/statistics/yinWuJinDuBiao" id="yinWuJinDuBiao"><i class="icon-th-large"></i>引物生产进度表</li>
		    <li url="${ctx}/statistics/xiuShiJinDuBiao" id="xiuShiJinDuBiao"><i class="icon-th-large"></i>修饰进度表</li>
		</ul>
	</div>
<% }%>
	<div title="报告单" data-options="iconCls:'report'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/print/createReport" id="createReport"><i class="icon-file-text"></i>生成报告单</li>
		</ul>
	</div>
	<div title="信封打印" data-options="iconCls:'mail'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/print/envelopePrint" id="envelopePrint"><i class="icon-print"></i>信封打印</li>
		</ul>
	</div>
	<div title="出库单打印" data-options="iconCls:'outbound'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/delivery/chukuList" id="chukuList"><i class="icon-file-text-alt"></i>出库单打印</li>
		</ul>
	</div>
<% if (isZiXi){%>
	<div title="打印标签" data-options="iconCls:'print'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/print/productionLabel" id="productionLabel"><i class="icon-bookmark"></i>生产标签</li>
			<li url="${ctx}/labelConfigure/config" id="labelConfigure"><i class="icon-cogs"></i>生产标签配置</li>
			<li url="${ctx}/delivery/deliveryLabel" id="deliveryLabel"><i class="icon-bookmark-empty"></i>发货标签</li>
		</ul>
	</div>
    <div title="用户管理" data-options="iconCls:'salesman'" style="height:200px;">
        <ul class="sbu_menu">
            <li url="${ctx}/user/manageQuery" id="manageQuery"><i class="icon-cogs"></i>用户管理</li>
        </ul>
    </div>
	<div title="客户管理" data-options="iconCls:'custom'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/customer/clientManage" id="clientManage"><i class="icon-cogs"></i>客户管理</li>
		</ul>
	</div>
	<div title="产品管理" data-options="iconCls:'product'" style="height:200px;">
		<ul class="sbu_menu">
			<li url="${ctx}/productManage/productMolecularList" id="productMolecularList"><i class="icon-bookmark"></i>修饰分子量配置</li>
			<li url="${ctx}/productManage/modifiePricedList" id="modifiePricedList"><i class="icon-bookmark"></i>修饰基础价格配置</li>
		</ul>
	</div>
<% }%>
</div>
</div>
</div>
<script src="${ctx}/static/js/menu.js" ></script>