<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<link href="${ctx}/static/css/handsontable.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/bootstrap.min.css" />
<link rel="stylesheet" href="${ctx}/dist/css/order/order.css">
<title>订单导入</title>
</head>
<body>

	<!--notice start-->
	<div class="notice-section">
		<strong> 注：如使用Excel上传订单，请下载最新的Excel模板。</strong>
	</div>
	<!--notice end-->

	<!--step start-->
	<div class="step-section clearfix">
		<div class="order-properties-wrapper form-horizontal">
			<div class="order-item-left">
				<div class="delivery-type-wrapper form-group clearfix">
					<label for="" class="control-label fl">交付形式:</label>
					<div class="control-input fl">
						<select name="" id="" class="form-control" disabled="disabled">
							<option value="Dry">干粉</option>
							<option value="Wet">液体</option>
							<option value="Mix">Mix</option>
						</select>
					</div>
				</div>
				<div class="unit-wrapper form-group clearfix">
					<label for="" class="control-label fl">
						<i class="icon-question-sign icon-help"></i>需求量单位<span class="field-required">*</span>:
					</label>
					<div class="control-input fl">
						<div class="radio-group">
							<input type="radio" checked="checked" id="OD" name="IsNmole" value="0">
							<label class="radio-inline" for="OD">OD</label>
						</div>
						<div class="radio-group">
							<input type="radio" checked="checked" id="nmole" name="IsNmole" value="1">
							<label class="radio-inline" for="nmole">nmole</label>
						</div>
					</div>
				</div>
				<div class="unit-wrapper form-group clearfix">
					<label for="" class="control-label fl">
						<i class="icon-question-sign icon-help"></i>
						引物数量<span class="field-required">*</span>:
					</label>
					<div class="control-input fl">
						<div class="input-group">
							<input type="text" id="J-rowAmount" class="amount" maxlength="5" size="5">
							<span class="input-group-btn">
								<button id="J-createRow" class="btn-action" type="button">应用</button>
							</span>
						</div>
					</div>
				</div>
			</div>
			<div class="order-item-right">
				<div class="unit-wrapper form-group clearfix">
					<label for="" class="control-label fl">
						<i class="icon-question-sign icon-help"></i>订单名称:
					</label>
					<div class="control-input fl">
						<input type="text" class="order-name text" name="orderName">
					</div>
				</div>
				<div class="unit-wrapper form-group clearfix">
					<label for="" class="control-label fl">
						<i class="icon-question-sign icon-help"></i>备注:
					</label>
					<div class="control-input fl">
						<textarea class="order-desc" cols="20" name="orderDesc" rows="3"></textarea>
					</div>
				</div>
			</div>
		</div>
		<div class="order-plate-wrapper">
			<div class="plate-control-wrapper">
				<div class="control-header control-btn">
					<div class="switch" id="J-switchType" data-type="2">
						<span class="glyphicon glyphicon-refresh"></span>
						<span class="plate-tube">切换至按板提交</span>
					</div>
					<i class="icon-question-sign icon-help"></i>
				</div>
				<div class="contorl-plate-form" id="J-plateForm">
					<div class="form-group clearfix">
						<label for="" class="control-label fl">板号:</label>
						<div class="control-input fl">
							<select name="" id="J-plateChoice" class="form-control">
								<option value="1">1</option>
							</select>
						</div>
					</div>
					<div class="form-group clearfix">
						<label for="" class="control-label fl">
							<i class="icon-question-sign icon-help"></i>
							板名<span class="field-required">*</span>:
						</label>
						<div class="control-input fl">
							<div class="input-group">
								<input type="text" class="amount" maxlength="5" size="5">
								<span class="input-group-btn">
									<button class="btn-action" type="button">应用</button>
								</span>
							</div>
						</div>
					</div>
					<div class="control-btn">
						<div class="switch" id="J-switchDirection" data-direction="2">
							<span class="glyphicon glyphicon-refresh"></span>
							<span class="plate-tube">切换至&nbsp;&nbsp;<span class="placeholder-text">横向提交</span></span>
						</div>
					</div>
					<div class="control-btn">
						<div class="switch" id="J-markEmpty" data-marking="false">
							<span class="plate-tube">标记空孔</span>
						</div>
					</div>
				</div>
			</div>
			<div class="view-wrapper">
				<div class="image-preview" id="J-imagePreview">
					<div class="image">
						<img src="${ctx}/static/images/Tube-CN.png" alt="">
						<div class="link">
							<a href="#" target="_blank">纯化方式使用指南</a>
						</div>
					</div>
					<div class="notice">
						<div class="title">注意事项:</div>
						<p class="notice-item">1.鼠标移至各项查看注释，其中*为必填</p>
						<p class="notice-item">2.长度大于60mer的引物建议选择hPAGE纯化</p>
						<p class="notice-item">3.如需特殊修饰请在备注栏中注明</p>
					</div>
				</div>
				<div class="plate-preview" id="J-platePreview">
					<div class="title" id="J-plateInfo">预览第1板，共1板</div>
					<div class="tb-header">
						<span class="icon-arrow-right icon-direction"></span>
						<span class="icon-arrow-down icon-direction"></span>
						<%
							int[] intArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
							request.setAttribute("intArray", intArray);
						%>
						<c:forEach items="${intArray}" var="item">
							<span class="tb-header-item">${item}</span>
						</c:forEach>
					</div>
					<div class="tb-body highlight">
						<%
							String[] array = { "A", "B", "C", "D", "E", "F","G","H"};
							request.setAttribute("array", array);
						%>
						<c:forEach items="${array}" var="indicator">
							<div class="tb-row">
								<div class="cell">
									<span class="tb-row-indicator">${indicator}</span>
								</div>
								<c:forEach items="${intArray}" var="item" varStatus="status">
									<div class="cell" data-tag="${indicator}:${status.index + 1}">
										<span class="circle transparent-circle" data-tag="${indicator}:${status.index + 1}"></span>
									</div>
								</c:forEach>
							</div>
						</c:forEach>
					</div>
					<div class="tb-legend">
						<div class="item">
							<span class="circle normal-circle"></span>
							<span>已填充</span>
						</div>
						<div class="item">
							<span class="circle valid-circle"></span>
							<span>有效</span>
						</div>
						<div class="item">
							<span class="circle invalid-circle"></span>
							<span>无效</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--step end-->

	<div class="sample-section">
		<div class="row clearfix">
			<div class="btn-section">
				<span class="btn btn-sm btn-primary" id="J-fillAll">按照第一行填写</span>
				<span class="btn btn-sm btn-danger" id="J-clearAll">清除引物信息</span>
			</div>
			<div class="legend">
				<div class="item">
					<span class="text">符号注释</span>
				</div>
				<div class="item">
					<span class="text">
						<span class="glyphicon glyphicon-download"></span>
						按顺序填充
					</span>
				</div>
				<div class="item">
					<span class="text">
						<span class="glyphicon glyphicon-circle-arrow-down"></span>
						全部填充
					</span>
				</div>
				<div class="item">
					<span class="text">
						<i class="icon-caret-down"></i>
						从下拉框中选择
					</span>
				</div>
				<div class="item">
					<span class="text">
						<span class="glyphicon glyphicon-asterisk field-required"></span>
						必填项
					</span>
				</div>
				<div class="item">
					<span class="text">
						<span class=" glyphicon glyphicon-remove-circle"></span>
						全部清除
					</span>
				</div>
			</div>
		</div>
		<div class="table-wrapper" id="tableWrapper"></div>
	</div>

	<div class="footer navbar-fixed-bottom">
		<div class="ga-footer row">
			<div class="col-lg-7 col-md-7 ga-footer-glass-wrapper"></div>
			<div class="col-lg-5  col-md-5 ga-footer-controls-wrapper">
				<div class="btn-group" role="group">
					<button class="btn  btn-large btn-danger" type="button">取消</button>
					<button id="btnSave" class="btn btn-large btn-primary enable-switch" type="button">保存草稿</button>
					<button id="btnSubmitCheck" class="btn btn-large btn-default-action enable-switch" type="button"> 保存&amp;下一步</button>
				</div>
			</div>
		</div>
	</div>

	<script id="hot-headercol-template" type="text/x-handlebars-template">
		<div class="col-header">
			{{#if isTooltip}}
				<a href="javascript:;" title="{{tooltipText}}" class="easyui-tooltip">{{colName}}</a>
			{{ else }}
				<span class="col-name">
					{{colName}}
				</span>
			{{/if}}
			<span class="field-required" data-reqid="{{colid}}">
				{{#if isRequired}}
            		*
            	{{/if}}
			</span>
			{{#if colid}}
				<div class="hot-col-action">
					{{#if fillseq}}
						<span id="{{colid}}FillSeq" data-prop="{{prop}}" data-action="fillseq"
							  class="glyphicon glyphicon-download"></span>
					{{/if}}
					{{#if fillcopy }}
					<span id="{{colid}}Fill" data-prop="{{prop}}" data-action="fill"
						  class="glyphicon glyphicon-circle-arrow-down"></span>
					{{/if}}
					<span id="{{colid}}Clear" data-prop="{{prop}}" data-action="clear" class=" glyphicon glyphicon-remove-circle" aria-hidden="true"></span>
				</div>
			{{/if}}
		</div>
	</script>

	<script id="plate-cell-template" type="text/x-handlebars-template">
		<div class="title" id="J-plateInfo">预览第{{plateId}}板，共{{plates}}板</div>
		{{#if isHorizontal}}
			<div class="tb-header highlight">
		{{else}}
			<div class="tb-header">
		{{/if}}
			<span class="icon-arrow-right icon-direction"></span>
			<span class="icon-arrow-down icon-direction"></span>
			{{#each plateCols}}
				<span class="tb-header-item">{{this}}</span>
			{{/each}}
		</div>
		{{#if isHorizontal}}
			<div class="tb-body">
		{{else}}
			<div class="tb-body highlight">
		{{/if}}
			{{#each plateRows}}
				<div class="tb-row">
					<div class="cell">
						<span class="tb-row-indicator">{{this}}</span>
					</div>
					{{#each ../plateCols}}
						<div class="cell" data-tag="{{../this}}:{{this}}">
							<span class="circle transparent-circle" data-tag="{{../this}}:{{this}}"></span>
						</div>
					{{/each}}
				</div>
			{{/each}}
		</div>
		<div class="tb-legend">
			<div class="item">
				<span class="circle normal-circle"></span>
				<span>已填充</span>
			</div>
			<div class="item">
				<span class="circle valid-circle"></span>
				<span>有效</span>
			</div>
			<div class="item">
				<span class="circle invalid-circle"></span>
				<span>无效</span>
			</div>
		</div>
	</script>

	<script src="${ctx}/static/js/lib/sea.js" ></script>
	<script src="${ctx}/views/order/js/orderImport/orderConfig.js" ></script>
	<script>
      seajs.config({
        base: "${ctx}/static/js/lib/",
        alias: {
          jquery: 'jquery.min.js',
		  json: 'json2.js',
		  easyui: 'jquery.easyui.min.js',
          handlebars: 'handlebars-v2.0.0.js',
		  handsontable: 'handsontable/index.js',
		  underscore: 'underscore.min.js'
        }
      });

      // 加载入口模块
      seajs.use("${ctx}/views/order/js/orderImport/index.js")
	</script>
</body>
</html>