<%@ page import="java.util.Date" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%--@ page import="com.opensymphony.xwork2.ActionContext" --%>
<%@ page import="java.util.Locale" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--@taglib prefix="rc" uri="http://util.one.sinosoft.com/RCDate" --%>
<%--@taglib uri="/struts-tags" prefix="s" --%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script>
    var ctx = '${ctx}';
</script>
<%
    //Locale locale = Locale.getDefault();
   // Locale.setDefault(Locale.CHINESE);


%>
<div id="header" class="span12">
    <div id="title">
        <a href="${ctx}/logout" style="float:right;margin-top:14px"><font size="2">退出登录&nbsp; </font></a>
        <span class="pull-right" style="margin:14px 8px 0 0"> ${date}&nbsp;&nbsp;${welInfo}</span>

        <h1>
                                
            <small>--菜单
                <s:iterator value="#request.test2" >
                    <rc:rcDate format="yyyy/MM/dd hh:mm:ss" />
                </s:iterator>
                <s:iterator value="#request.test3"  var="tt">
                    <rc:rcDate format="yyyy/MM/dd hh:mm:ss" name="createTime"/>
                </s:iterator>
                <rc:rcDate format="yy-MM-dd" name="t1"/>
                <rc:rcDate format="yy-MM-dd" value="${t1}"/>
            </small>
        </h1>
    </div>

    <div id="menu">
        <ul class="nav nav-tabs">
            <li id="user-tab"><a href="${ctx}/synthesis/preQuery">安排合成</a></li>
        </ul>
    </div>
</div>


