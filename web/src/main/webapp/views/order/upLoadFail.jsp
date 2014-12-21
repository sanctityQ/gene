<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
  <head>
    <title>导入失败</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <link href="resources/css/jquery-ui-themes.css" type="text/css" rel="stylesheet"/>
    <link href="resources/css/axure_rp_page.css" type="text/css" rel="stylesheet"/>
    <link href="data/styles.css" type="text/css" rel="stylesheet"/>
    <link href="files/upLoadFail/styles.css" type="text/css" rel="stylesheet"/>
    <script src="../resources/scripts/jquery-1.7.1.min.js"></script>
    <script src="../resources/scripts/jquery-ui-1.8.10.custom.min.js"></script>
    <script src="../resources/scripts/axure/axQuery.js"></script>
    <script src="../resources/scripts/axure/globals.js"></script>
    <script src="../resources/scripts/axutils.js"></script>
    <script src="../resources/scripts/axure/annotation.js"></script>
    <script src="../resources/scripts/axure/axQuery.std.js"></script>
    <script src="../resources/scripts/axure/doc.js"></script>
    <script src="data/document.js"></script>
    <script src="../resources/scripts/messagecenter.js"></script>
    <script src="../resources/scripts/axure/events.js"></script>
    <script src="../resources/scripts/axure/action.js"></script>
    <script src="../resources/scripts/axure/expr.js"></script>
    <script src="../resources/scripts/axure/geometry.js"></script>
    <script src="../resources/scripts/axure/flyout.js"></script>
    <script src="../resources/scripts/axure/ie.js"></script>
    <script src="../resources/scripts/axure/model.js"></script>
    <script src="../resources/scripts/axure/repeater.js"></script>
    <script src="../resources/scripts/axure/sto.js"></script>
    <script src="../resources/scripts/axure/utils.temp.js"></script>
    <script src="../resources/scripts/axure/variables.js"></script>
    <script src="../resources/scripts/axure/drag.js"></script>
    <script src="../resources/scripts/axure/move.js"></script>
    <script src="../resources/scripts/axure/visibility.js"></script>
    <script src="../resources/scripts/axure/style.js"></script>
    <script src="../resources/scripts/axure/adaptive.js"></script>
    <script src="../resources/scripts/axure/tree.js"></script>
    <script src="../resources/scripts/axure/init.temp.js"></script>
    <script src="files/upLoadFail/data.js"></script>
    <script src="../resources/scripts/axure/legacy.js"></script>
    <script src="../resources/scripts/axure/viewer.js"></script>
    <script type="text/javascript">
      $axure.utils.getTransparentGifPath = function() { return 'resources/images/transparent.gif'; };
/*       $axure.utils.getOtherPath = function() { return 'resources/Other.html'; };
      $axure.utils.getReloadPath = function() { return 'resources/reload.html'; }; */
    </script>
  </head>
  <body>
    <div id="base" class="">

      <!-- Unnamed (形状) -->
      <div id="u0" class="ax_形状">
        <img id="u0_img" class="img " src="../images/upLoadFail/u0.png"/>
        <!-- Unnamed () -->
        <div id="u1" class="text">
          <p><span>&nbsp;</span></p>
        </div>
      </div>

      <!-- placeholder (形状) -->
      <div id="u2" class="ax_形状" data-label="placeholder">
        <img id="u2_img" class="img " src="../images/upLoadFail/placeholder_u2.png"/>
        <!-- Unnamed () -->
        <div id="u3" class="text">
          <p><span>错误</span></p><p><span>图标</span></p>
        </div>
      </div>

      <!-- paragraph (形状) -->
      <div id="u4" class="ax_文本" data-label="paragraph">
        <img id="u4_img" class="img " src="../resources/images/transparent.gif"/>
        <!-- Unnamed () -->
        <div id="u5" class="text">
          <p><span>数据导入失败</span></p>
        </div>
      </div>

      <!-- paragraph (形状) -->
      <div id="u6" class="ax_文本" data-label="paragraph">
        <img id="u6_img" class="img " src="../resources/images/transparent.gif"/>
        <!-- Unnamed () -->
        <div id="u7" class="text">
          <p><span>数据包含以下错误信息，请修正后重新导入</span></p>
        </div>
      </div>

      <!-- Unnamed (水平线) -->
      <div id="u8" class="ax_水平线">
        <img id="u8_start" class="img " src="../resources/images/transparent.gif" alt="u8_start"/>
        <img id="u8_end" class="img " src="../resources/images/transparent.gif" alt="u8_end"/>
        <img id="u8_line" class="img " src="../images/upLoadFail/u8_line.png" alt="u8_line"/>
      </div>

      <!-- paragraph (形状) -->
      <!-- paragraph (形状) -->
      <div id="u9" class="ax_文本" data-label="paragraph">
        <img id="u9_img" class="img " src="../resources/images/transparent.gif"/>
        <!-- Unnamed () -->
        <div id="u10" class="text">
        <c:forEach var="item" items="${errors}" varStatus="status">     
		      <p><span>&#149; ${item}</span></p>   
		</c:forEach> 
        </div>
      </div>

      <!-- button (形状) -->
      <div id="u11" class="ax_形状" data-label="button">
        <img id="u11_img" class="img " src="../images/upLoadFail/button_u11.png"/>
        <!-- Unnamed () -->
        <div id="u12" class="text">
          <p><span>重新</span><span>导入</span></p>
        </div>
      </div>

      <!-- Unnamed (新母版 1) -->

      <!-- Unnamed (形状) -->
      <div id="u14" class="ax_形状">
        <img id="u14_img" class="img " src="../images/订单管理/u1.png"/>
        <!-- Unnamed () -->
        <div id="u15" class="text">
          <p><span>&nbsp; &nbsp; </span><span>睿博兴科业务系统</span></p>
        </div>
      </div>

      <!-- paragraph (形状) -->
      <div id="u16" class="ax_文本" data-label="paragraph">
        <img id="u16_img" class="img " src="../resources/images/transparent.gif"/>
        <!-- Unnamed () -->
        <div id="u17" class="text">
          <p><span>您好，张大伟&nbsp; &nbsp; </span><span>&nbsp; </span><span>个人</span><span>设置 | 退出</span><span>系统</span></p>
        </div>
      </div>
    </div>
  </body>
</html>
