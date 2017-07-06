//tb.js
//http://docs.handsontable.com/0.32.0/tutorial-introduction.html

'use strict';

define(function(require, exports, module) {
  var primer = require('./hot-primer');
  var updateSettings = require('./hot-updateSettings');
  var renderer = require('./hot-renderer');
  var validator = require('./hot-validator');
  var buildHeader = require('./hot-buildHeader');
  var addTip = require('./hot-addTip');
  var hot = require('./hot-init');

  //表格主体部分
  var hotCtrler = {
    init: function() {
      updateSettings(hot);
      this.commentsPlugin = hot.getPlugin('comments');
      this.bindEvent();
    },
    //添加注释
    setCommentAtCell: function(row, col, comment) {
      this.commentsPlugin.setCommentAtCell(row, col, comment);
    },
    bindEvent: function() {
      addTip();
    },
    insert: function(count) {
      hot.alter('insert_row', hot.countRows(), count);
    }
  }

  module.exports = hotCtrler;
});