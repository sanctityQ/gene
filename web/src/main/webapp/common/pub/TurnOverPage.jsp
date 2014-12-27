<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<title></title>
<script src="${ctx}/common/pub/TurnOverPage.js" type="text/javascript"></script>
</head>
<body >
<table class=common>
    <tr>

        <input type="hidden" name="totalPage" value="${page.totalPages}"/>
        <input type="hidden" name="pageNo"  value='${page.number+1}'/>
        
        <c:if test="${page.totalPages>=0}">
            <td width="60%" align="center" >
                <div align="right">
                
                                                    满足条件的记录为   ${page.totalElements} 条

                                                     第 ${page.number+1} 页/共 ${page.totalPages} 页
                   
                    <c:if test="${page.number+1 !=1 }">
                     <a href="javascript:FirstPage()">首页</a>
                    </c:if>

                    <c:if test="${page.number+1 ==1 }">
                     <font color="#808080">首页</font>
                    </c:if>

                     <c:if test="${page.number+1 >1 }">
                          <a href="javascript:PrePage(${page.number+1})">前页</a>
                     </c:if>

                     <c:if test="${page.number+1 <=1 }">
                           <font color="#808080">前页</font>
                     </c:if>
 
                     <c:if test="${ page.totalPages > page.number+1}">
                          <a href="javascript:NextPage(${page.number+1})">后页</a>
                     </c:if>

                     <c:if test="${ page.totalPages <= page.number+1}">
                          <font color="#808080">后页</font>
                     </c:if>

                     <c:if test="${ page.totalPages > page.number+1}">
                     <a href="javascript:LastPage()">尾页</a>
                     </c:if>

                     <c:if test="${ page.totalPages <= page.number+1}">
                     <font color="#808080">尾页</font>　
                     </c:if>

                         跳到<input type="text" name="changepage" size="2" class="common" style="width:3%" value='${page.number+1}'  maxlength="10" >
                                        页 <img style='cursor:hand' align="absmiddle" src="${ctx}/static/images/bgGo.gif" onClick="ChangePage()">
                </div>
            </td>
        </c:if>
    </tr>
</table>
</body>
</html>


