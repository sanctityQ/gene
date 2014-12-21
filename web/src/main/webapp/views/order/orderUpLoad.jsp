<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
  <head>
    <title>订单导入</title>
    <script type="text/javascript">
      /* jQuery(function(){
    		jQuery("#u0_img").click(
    		  function(){
    		     $("#inputForm").submit();
    		  }		
    		);
    		jQuery("#u1").click(
    		  function(){
    		     $("#inputForm").submit();
    		  }		
	    	);
    	}); */
    </script>
  </head>
  <body>
  <form id="inputForm" modelAttribute="user" action="${ctx}/order/upload" method="post" enctype="multipart/form-data"
	class="form-horizontal">
    <div id="base" class="">



      <!-- paragraph (形状) -->
      <div id="u15" class="ax_文本" data-label="paragraph">
        <!-- Unnamed () -->
        <div id="u16" class="text">
          <p><span>请输入客户姓名或客户代码</span><span>。</span></p>
        </div>
      </div>

      <!-- textfield (文本框(单行)) -->
      <div id="u17" class="ax_文本框_单行_">
        <input id="customerCode" name="customerCode" type="text" data-label="textfield"/>
      </div>

      <!-- textfield - file upload (文本框(单行)) -->
      <div id="u28" class="ax_文本框_单行_">
        <input type="file" id="file" name="file" style="cursor: pointer;"/>
        <input type="submit" style="cursor: pointer;"/>
      </div>

      <!-- button - file upload browse (形状) -->

      <!-- Unnamed (新母版 1) -->

    </div>
    </form>
  </body>
</html>
