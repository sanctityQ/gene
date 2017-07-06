define(function(require, exports, module) {
  // 通过 require 引入依赖
  require('handsontable');
  require('handlebars');

  var hotCtrler = require('./hot-controller');
  var formCtrler = require('./form-controller');

  hotCtrler.init();
  formCtrler.init();
});