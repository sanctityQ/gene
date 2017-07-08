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
  var plateCtrler = require('./plate-controller');

  //表格主体部分
  var hotCtrler = {
    init: function() {
      updateSettings(hot);
      this.commentsPlugin = hot.getPlugin('comments');
      this.bindEvent();
    },
    //创建空行表格后，填充默认数据
    fillData: function(count) {
      var isPlateMode = plateCtrler.isPlateMode();
      var direction = plateCtrler.getDirection();
      var rowsPrefix = sngr.plateRows;

      if (direction == sngr.plateDirection.HORIZONTAL) {
        var result = hot.dataList.map(function(item, index) {
          var row = rowsPrefix[Math.ceil((index + 1) / 12)];
          var col = (index + 1) % 12;
          return _.extend({}, primer, item, {
            PrimerIndex: index + 1,
            Well: row + ':' + (col || 12)
          });
        });
        this.loadData(result);
      }

      if (direction === sngr.plateDirection.VERTICAL) {
        var result = hot.dataList.map(function(item, index) {
          var row = rowsPrefix[(index + 1) % 8 || 8]
          var col = Math.ceil((index + 1) / 8)
          return _.extend({}, primer, item, {
            PrimerIndex: index + 1,
            Well: row + ':' + col
          });
        });
        this.loadData(result);
      }
    },
    loadData: function(result) {
      hot.loadData(result);
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
      this.fillData(count);
    }
  }

  module.exports = hotCtrler;
});