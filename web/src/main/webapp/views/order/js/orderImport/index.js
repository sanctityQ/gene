
define(function(require, exports, module) {
  // 通过 require 引入依赖
  require('handsontable');
  require('handlebars');
  require('underscore');

  var hotCtrler = require('./hot-controller');
  var formCtrler = require('./form-controller');
  var plateCtrler = require('./plate-controller');

  hotCtrler.init();
  formCtrler.init();
  plateCtrler.init();
});