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
    //显示或隐藏列
    showOrhidenColumn: function() {
      //订阅模式中，对订阅者的执行顺序有要求，采取异步模式保证执行顺序
      setTimeout(function() {
        //按板提交，显示柱号、板号两列
        if (plateCtrler.isPlateMode()) {
          hot.updateSettings({
            colWidths: [40, 40, 40, 90, 270, 50, 60, 70, 80, 140, 140, 190, 1]
          });
        } else {
          //按管提交，隐藏柱号、板号两列
          hot.updateSettings({
            colWidths: [1, 1, 40, 90, 338, 60, 60, 70, 80, 140, 140, 190, 1]
          });
        }
      }, 0)
    },
    //1.创建空行表格后，填充默认数据
    //2.更改方向后
    fillData: function() {
      setTimeout(function() {
        var isPlateMode = plateCtrler.isPlateMode();
        var direction = plateCtrler.getDirection();
        var rowsPrefix = sngr.plateRows;

        //横向
        if (direction == sngr.plateDirection.HORIZONTAL) {
          var result = hot.getSourceData().map(function(item, index) {
            var row = rowsPrefix[Math.ceil((index + 1) / 12)];
            var col = (index + 1) % 12;
            return _.extend({}, primer, item, {
              PrimerIndex: index + 1,
              Well: row + ':' + (col || 12)
            });
          });
          this.loadData(result);
        }

        //纵向
        if (direction === sngr.plateDirection.VERTICAL) {
          var result = hot.getSourceData().map(function(item, index) {
            var row = rowsPrefix[(index + 1) % 8 || 8]
            var col = Math.ceil((index + 1) / 8)
            return _.extend({}, primer, item, {
              PrimerIndex: index + 1,
              Well: row + ':' + col
            });
          });
          this.loadData(result);
        }
      }.bind(this), 0);
    },
    loadData: function(result) {
      hot.loadData(result);
    },
    //添加注释
    setCommentAtCell: function(row, col, comment) {
      this.commentsPlugin.setCommentAtCell(row, col, comment);
    },
    bindEvent: function() {
      plateCtrler.proxy.$switchType.bind('switch:type', this.showOrhidenColumn);
      plateCtrler.proxy.$switchDirection.bind('switch:direction', this.fillData.bind(this));
      addTip();
    },
    insert: function(count) {
      hot.alter('insert_row', hot.countRows(), count);
      this.fillData(count);
    }
  }

  module.exports = hotCtrler;
});