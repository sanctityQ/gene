//tb.js
//http://docs.handsontable.com/0.32.0/tutorial-introduction.html

'use strict';

define(function(require, exports, module) {
  var primer = require('./hot-primer');
  var renderer = require('./hot-renderer');
  var buildHeader = require('./hot-buildHeader');
  var addTip = require('./hot-addTip');

  //模拟的表格下拉框数据
  var mock = {
    purigyType: ["DSL", "HPLC", "tPAGE", "hPAGE"],
    fiveModying: ["", "5'-AminolinkerC6", "5'-Phosphate", "5'-Thiol-C6 S-S", "5'-Biotin", "5'-Digoxigenin", "5'-FAM", "5'-HEX", "5'-TET", "5'-JOE", "5'-ROX", "5'-TAMRA", "5'-CY-3", "5'-CY-5", "5'-Aminolinker C12", "5'-Biotin TEG"],
    threeModying: ["", "3'-AminolinkerC7", "3'-Phosphate", "3'-Thiol-C3 S-S", "3'-Biotin", "3'-Digoxigenin", "3'-BHQ1", "3'-BHQ2", "3'-CY-3", "3'-CY-5", "3'-Dabcyl", "3'-JOE", "3'-ROX", "3'-FAM", "3'-TAMRA", "3'-Biotin TEG", "3'-Eclipse", "3'-MGB", "3'-Cholesteryl"],
    otherModying: ["", "dU", "dI", "Phosphorothioate", "dT-Aminolinker", "dU+dI", "dU+Phosphorothioate", "dU+dT-Aminolinker", "dI+Phosphorothioate", "dI+dT-Aminolinker", "Phosphorothioate+dT-Aminolinker"],
  };

  var tb = {
    init: function() {
      this.dataList = [];
      this.container = document.getElementById('tableWrapper');
      this.$createRow = $('#J-createRow');

      this.create();
      this.commentsPlugin = this.hot.getPlugin('comments');
    },
    //加载表格数据
    loadData: function() {
      return this.dataList;
    },
    //初始化数据
    initData: function() {
      this.dataList.push(primer);
    },
    //添加注释
    setCommentAtCell: function(row, col, comment) {
      this.commentsPlugin.setCommentAtCell(row, col, comment);
    },
    create: function() {
      var that = this;
      this.hot = new Handsontable(this.container, {
        data: this.loadData(),
        rowHeaders: false,
        allowEmpty: true,
        colHeaders: buildHeader,
        autoColumnSize: true,
        hiddenColumns: {},
        fillHandle: "vertical",
        height: 300,
        currentRowClassName: "hot-current-row",
        comments: true,
        stretchH: 'all',
        // colWidths: [1, 1, 40, 90, 338, 60, 60, 70, 80, 140, 140, 190, 1],
        colWidths: [1, 1, 40, 90, 310, 60, 60, 70, 80, 140, 140, 190, 1],
        columns: [{
          data: "CustPlateID",
          readOnly: true
        }, {
          data: "Well",
          readOnly: true
        }, {
          data: "PrimerIndex",
          readOnly: true
        }, {
          data: "PrimerName"
        }, {
          data: "Sequence"
        }, {
          data: "Basenumber",
          readOnly: true
        }, {
          data: "Quantity",
          // validator: function (value, callback) {
          //   callback(!isNaN(value));
          //   that.setCommentAtCell(2,2);
          //   return value;
          // }
        }, {
          data: "Tubes"
        }, {
          data: "PurityType",
          type: "dropdown",
          source: mock.purigyType,
          allowInvalid: true
        }, {
          data: "MFive",
          type: "dropdown",
          source: mock.fiveModying
        }, {
          data: "MThree",
          type: "dropdown",
          source: mock.threeModying
        }, {
          data: "MOther",
          type: "dropdown",
          source: mock.otherModying
        }, {
          data: "Status",
          readOnly: true,
          renderer: renderer.emptyRowRenderer
        }],
        afterRender: function() {
          // setTimeout(function() {
          //   that.hot.dataList = that.dataList;
          // }, 0);
          addTip();
        },
        afterSelectionEnd: function(r, c, r2, c2) {
          // console.log(r, c, r2, c2)
        },
        afterChange: function(changes, source) {
          if (!changes) return;

          if (source === 'edit') {
            var startCol = 4;
            var endCol = 6;
            for (var i = 0; i < changes.length; i++) {
              var row = changes[i][0];
              var col = changes[i][1];

              var y = changes[i][3];
              var x = this.getDataAtCell(row, col === startCol ? startCol + 1 : startCol);

              if (changes[i][1] === startCol || changes[i][1] === startCol + 1) {
                this.setDataAtCell(row, endCol, x * y);
              }
            }
          }
        },
        afterRemoveRow: function(n, t) {
          // handsonTableAfterRemoveRow(n, t)
        },
        afterCreateRow: function(n, t) {
          // handsonTableAfterCreateRow(n, t)
        },
        beforeChange: function(changes, source) {
          $(that.hot).trigger('hot:beforeChange', [changes, source]);
        },
        beforeChangeRender: function(n, t) {
          // that.handsonTableBeforeChangeRender(n, t)
        }
      });
    },
  };

  tb.init();

  module.exports = tb.hot;
});