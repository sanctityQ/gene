<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="msg" uri="http://mvc.one.sinosoft.com/validation/msg" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
<link href="${ctx}/static/css/easyui.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/icon.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/perfect-scrollbar.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/master.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/font-awesome.min.css" />
<!--[if IE 7]>
  <link rel="stylesheet" href="css/font-awesome-ie7.min.css">
<![endif]-->
<script src="${ctx}/static/js/jquery.min.js" ></script>
<script src="${ctx}/static/js/jquery.easyui.min.js" ></script>
<script src="${ctx}/static/js/perfect-scrollbar.min.js" ></script>
<script src="${ctx}/static/js/index.js" ></script>
<script type="text/javascript">
var ctx = '${ctx}';
</script>
</head>
<body>
<div class="page_padding">
	<div class="content_box">
		<h2>生产标签配置</h2>
        <table width="100%" class="configure_table">
            <tr>
                <td colspan="3" height="10"></td>
            </tr>
            <tr>
                <td align="right">客户代码：</td>
                <td><input class="inp_text" type="text" value="" style="width: 80%" /></td>
                <!-- <td><button type="button" class="btn">配 置</button></td> -->
            </tr>
            <tr>
                <td align="right">打印列数：</td>
                <td colspan="2" id="columnsNumber">
                    <label><input type="radio" value="1" name="number" checked /> 一列</label>
                    <label><input type="radio" value="2" name="number" /> 二列</label>
                    <label><input type="radio" value="3" name="number" /> 三列</label>
                </td>
            </tr>
<!--             <tr>
                <td align="right">生产编号开头：</td>
                <td>
                    <input class="inp_text" type="text" value="" style="width: 80%" />
                </td>
                <td></td>
            </tr> -->
            <tr>
                <td colspan="3">
                    <table class="set_configure" cellpadding="0" cellspacing="0">
                        <tr>
                            <td></td>
                            <td><h2>生产标签</h2></td>
                            <td></td>
                            <td><h2>待配置项</h2></td>
                        </tr>
                        <tr id="setRows">
                            <td width="60">
                                <div class="set_btn_l icon-long-arrow-up" onclick="moveConfigure('up');"></div>
                                <div class="set_btn_l icon-long-arrow-down" onclick="moveConfigure('down');"></div>
                            </td>
                            <td>
                                <ul id="printList">
                                    <li value="a">生产编号</li>
                                    <li value="b">引物名称</li>
                                    <li value="c">OD/Tube</li>
                                    <li value="d">MW</li>
                                    <li value="e">Tm(℃)</li>
                                    <li value="f">100pmole/μl</li>
                                    <li value="g">修饰</li>
                                </ul>
                            </td>
                            <td width="60">
                                <div class="set_btn icon-long-arrow-left" onclick="moveConfigure('left');"></div>
                                <div class="set_btn icon-long-arrow-right" onclick="moveConfigure('right');"></div>
                            </td>
                            <td>
                                <ul id="configureList">
                                    <li>订单号</li>
                                    <li>生产编号</li>
                                    <li>引物名称</li>
                                    <li>OD/Tube</li>
                                    <li>nmole/tube</li>
                                    <li>管数</li>
                                    <li>MW</li>
                                    <li>TM</li>
                                    <li>GC(%)</li>
                                    <li>Tm(℃)</li>
                                    <li>100pmole/μl</li>
                                    <li value="bark">备注/日期</li>
                                    <li>修饰</li>
                                    <li>加水量</li>
                                    
                                </ul>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="3" height="10"></td>
            </tr>
        </table>
		<div class="tools_bar tabbing">
			<button class="btn-primary submit" type="buttin" onclick="saveConfigure();">保存配置</button>
		</div>
	</div>
</div>
<script src="${ctx}/static/js/labelConfigure.js" ></script>
</body>
</html>