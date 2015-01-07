<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta charset="utf-8">
    <link href="${ctx}/static/css/easyui.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/css/icon.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/css/perfect-scrollbar.min.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/css/master.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/font-awesome.min.css"/>
    <!--[if IE 7]>
    <link rel="stylesheet" href="${ctx}/static/css/font-awesome-ie7.min.css">
    <![endif]-->
    <script src="${ctx}/static/js/jquery.min.js"></script>
    <script src="${ctx}/static/js/jquery.easyui.min.js"></script>
    <script src="${ctx}/static/js/perfect-scrollbar.min.js"></script>
    <script>
        var ctx = '${ctx}';
    </script>
</head>
<body class="easyui-layout" data-options="animate:false">
<div data-options="region:'north'">
    <div class="header">
        <img src="${ctx}/static/images/logo.png" alt="">
        <ul class="user_tool">
            <li class="logout"><a href="#"><i class="icon-signout"></i><span>退出</span></a></li>
            <li><a href="#"><i class="icon-user"></i><span>Administrator</span></a></li>
        </ul>
    </div>
</div>
<div data-options="region:'west',split:false" id="westLayout">
    <%@ include file="/WEB-INF/layouts/leftMenu.jsp"%>
</div>
<div id="hideWest" onclick="toggleWest(this);"></div>
<div data-options="region:'center'" id="layoutCont">
    <div class="easyui-tabs" data-options="fit:true,border:false,plain:true" id="tabList">
    </div>
</div>
</body>
</html>
<script src="${ctx}/static/js/index.js"></script>