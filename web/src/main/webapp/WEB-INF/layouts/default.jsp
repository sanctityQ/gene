<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title><sitemesh:title/></title>
    <link href="${ctx}/static/css/easyui.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/css/icon.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/css/perfect-scrollbar.min.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/css/master.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/css/font-awesome.min.css" type="text/css" rel="stylesheet"/>
    <!--[if IE 7]>
    <link rel="stylesheet" href="css/font-awesome-ie7.min.css">
    <![endif]-->
    <script src="${ctx}/static/js/jquery.min.js"></script>
    <script src="${ctx}/static/js/jquery.easyui.min.js"></script>
    <script src="${ctx}/static/js/perfect-scrollbar.min.js"></script>
    <script src="${ctx}/static/js/index.js"></script>
    <script>
        var ctx = '${ctx}';
    </script>
    <sitemesh:head/>
</head>
<body>
<sitemesh:body/>
</body>
</html>