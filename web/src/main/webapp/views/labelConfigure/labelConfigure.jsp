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
                <!-- <td><input class="inp_text" type="text" value="" style="width: 80%" /></td> -->
                <td>
                <input class="inp_text" type="text" autocomplete="off" id="seachCustom" name="customerName" value="" style="width: 80%" />
			    <input class="inp_text" id="customerCode" type="hidden" name="customerCode" value=""/>
			    <ul id="seachList"></ul>
			    </td>
                <td><button type="button" class="btn" onclick="searchConfigure()">查询</button></td>
            </tr>
            <tr>
                <td align="right">打印列数：</td>
                <td colspan="2" id="columnsNumber">
                    <label><input type="radio" value="1" name="number" /> 一列</label>
                    <label><input type="radio" value="2" name="number" /> 二列</label>
                    <label><input type="radio" value="3" name="number" checked/> 三列</label>
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
                                    <li value="productNo">生产编号</li>
                                    <li value="primeName">引物名称</li>
                                    <li value="odTB">OD/Tube</li>
                                    <li value="MW">MW</li>
                                    <li value="TM">TM</li>
                                    <li value="100pmoleμl">100pmole/μl</li>
                                    <li value="midi">修饰</li>
                                </ul>
                            </td>
                            <td width="60">
                                <div class="set_btn icon-long-arrow-left" onclick="moveConfigure('left');"></div>
                                <div class="set_btn icon-long-arrow-right" onclick="moveConfigure('right');"></div>
                            </td>
                            <td>
                                <ul id="configureList">
                                    <li value="orderNo">订单号</li>
                                    <li value="nmolTB">nmole/tube</li>
                                    <li value="tb">管数</li>
                                    <li value="GC">GC(%)</li>
                                    <li value="remark">备注/日期</li>
                                    <li value="μgOD">μg/OD</li>
                                    <li value="siteNo">位置号</li>
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
<script src="${ctx}/static/js/vagueSeach.js" ></script>
</body>
</html>