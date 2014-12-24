<%@ page import="java.util.Date" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script>
    var ctx = '${ctx}';
</script>
<div id="header" class="span12">
    <div id="title">
        <h1>
            <small>--菜单 </small>
        </h1>
    </div>

    <div id="menu">
        <ul class="nav nav-tabs">
            <li id="user-tab"><a href="${ctx}/synthesis/preMakeTableQuery">制作合成板</a></li>
            <li id="user-tab"><a href="${ctx}/synthesis/preSynthesisQuery">录入合成结果</a></li>
        </ul>
    </div>
</div>


