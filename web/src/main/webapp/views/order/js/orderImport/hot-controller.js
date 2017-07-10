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

      this.$fillAll = $('#J-fillAll');
      this.$clearAll = $('#J-clearAll');

      this.bindEvent();
    },
    dataList: [],
    //初始化数据
    initDataList: function(total) {
      for (var i = 0; i < total; i++) {
        this.dataList.push(_.clone(primer));
      }
    },
    //显示或隐藏列
    showOrhidenColumn: function() {
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
    },
    //选择指定的cell
    selectCell: function(event, data) {
      var direction = plateCtrler.getDirection();
      var tag = data.tag.split(':');
      var row = tag[0];
      var col = +tag[1];

      if (direction === sngr.plateDirection.VERTICAL) {
        var base = sngr.plateRows.indexOf(row);
        //前面cell的总数
        var preAllLen = (col - 1) * 8;
        hot.selectCell(preAllLen + base - 1, 0);
      }

      if (direction === sngr.plateDirection.HORIZONTAL) {
        var base = sngr.plateRows.indexOf(row);
        //前面cell的总数
        var preAllLen = (base - 1) * 12;
        hot.selectCell(preAllLen + col - 1, 0);
      }
    },
    //1.创建空行表格后，填充默认数据
    //2.更改方向后
    fillData: function(amount, plateId) {
      var isPlateMode = plateCtrler.isPlateMode();
      var direction = plateCtrler.getDirection();
      var rowsPrefix = sngr.plateRows;

      plateId = plateId || this.plateId || 1;
      this.amount = amount || this.amount || 0;

      //横向
      if (direction == sngr.plateDirection.HORIZONTAL) {
        var start = (plateId - 1) * 96;
        var end = start + this.amount;

        _.each(this.dataList.slice(start, end), function(item, index) {
          var row = rowsPrefix[Math.ceil((index + 1) / 12)];
          var col = (index + 1) % 12;

          _.extend(item, {
            PrimerIndex: index + 1,
            Well: row + ':' + (col || 12),
            CustPlateID: plateId
          });
        });
        this.loadData(this.dataList.slice(start, end));
      }

      //纵向
      if (direction === sngr.plateDirection.VERTICAL) {
        var start = (plateId - 1) * 96;
        var end = start + this.amount;

        _.each(this.dataList.slice(start, end), function(item, index) {
          var row = rowsPrefix[(index + 1) % 8 || 8];
          var col = Math.ceil((index + 1) / 8);

          _.extend(item, {
            PrimerIndex: index + 1,
            Well: row + ':' + col,
            CustPlateID: plateId
          });
        });
        this.loadData(this.dataList.slice(start, end));
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
      plateCtrler.proxy.$switchType.bind('changed:type', this.showOrhidenColumn);
      plateCtrler.proxy.$switchDirection.bind('changed:direction', function() {
        this.fillData();
      }.bind(this));
      plateCtrler.proxy.$platePreview.bind('click:circle', this.selectCell);
      plateCtrler.proxy.$plateChoice.bind('change:choice', this.onChangePlateChoice.bind(this));
      addTip();

      this.$fillAll.on('click', this.fillAll.bind(this));
      this.$clearAll.on('click', this.clearAll.bind(this));
    },
    //按照第一行填充表格
    fillAll: function() {
      var firstRowData = hot.getSourceDataAtRow(0);

      _.each(this.dataList, function(item, index) {
        //板号、柱号、编号、primer名称不被复制
        _.extend(item, firstRowData, {
          CustPlateID: item.CustPlateID,
          Well: item.Well,
          PrimerIndex: item.PrimerIndex,
          PrimerName: item.PrimerName
        });
      });

      this.fillData();
    },
    //清除引物信息
    clearAll: function() {
      _.each(this.dataList, function(item, index) {
        //只保留板号、柱号、编号
        _.extend(item, primer, {
          CustPlateID: item.CustPlateID,
          Well: item.Well,
          PrimerIndex: item.PrimerIndex,
          Tubes: null
        });
      });

      this.fillData();
    },
    onChangePlateChoice: function(event, data) {
      var range = data.range;
      var plateId = data.plateId;

      this.plateId = plateId;
      //填充表格中对应板的数据
      this.fillData(range.length, +plateId);
    },
    //初始化引物数量
    insert: function(total) {
      this.initDataList(total);
      //单个表格最多只显示96条数据
      this.fillData(Math.min(total, 96), 1);
    }
  }

  module.exports = hotCtrler;
});