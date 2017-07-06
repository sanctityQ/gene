'use strict';

//tb.js
//http://docs.handsontable.com/0.32.0/tutorial-introduction.html
var tb = {
  mock: {
    purigyType: ["DSL", "HPLC", "tPAGE", "hPAGE"],
    fiveModying: ["", "5'-AminolinkerC6", "5'-Phosphate", "5'-Thiol-C6 S-S", "5'-Biotin", "5'-Digoxigenin", "5'-FAM", "5'-HEX", "5'-TET", "5'-JOE", "5'-ROX", "5'-TAMRA", "5'-CY-3", "5'-CY-5", "5'-Aminolinker C12", "5'-Biotin TEG"],
    threeModying: ["", "3'-AminolinkerC7", "3'-Phosphate", "3'-Thiol-C3 S-S", "3'-Biotin", "3'-Digoxigenin", "3'-BHQ1", "3'-BHQ2", "3'-CY-3", "3'-CY-5", "3'-Dabcyl", "3'-JOE", "3'-ROX", "3'-FAM", "3'-TAMRA", "3'-Biotin TEG", "3'-Eclipse", "3'-MGB", "3'-Cholesteryl"],
    otherModying: ["", "dU", "dI", "Phosphorothioate", "dT-Aminolinker", "dU+dI", "dU+Phosphorothioate", "dU+dT-Aminolinker", "dI+Phosphorothioate", "dI+dT-Aminolinker", "Phosphorothioate+dT-Aminolinker"],
  },
  //默认字段
  primer: {
    Basenumber: 0,
    CustPlateID: 1,
    CustWellColumn: 0,
    CustWellRow: 0,
    GC: null,
    IsLibrarySeq: false,
    IsMarkedEmpty: false,
    MFive: '',
    MOther: '',
    MThree: '',
    Map: false,
    OD: null,
    PlateName: '',
    OrderID: "46aec588-28b3-4916-bb60-37ea5be21367",
    PrimerID: "5e34d060-342e-4f92-8f53-e4fccece09a1",
    PrimerIndex: 1,
    PrimerName: null,
    PrimerStatus: 0,
    PurityType: "HPLC",
    Quantity: null,
    Sequence: "SASD",
    SequenceBak: '',
    SequenceID: null,
    TM: null,
    Tubes: null,
    mec: null,
    mol_wt: null,
    Status: 2
  },
  emptyRowRenderer: function(n, t, i, r, u, f) {
    Handsontable.renderers.TextRenderer.apply(this, arguments);
    f == 5 && $(t).parent().children("td").css("background-color", "#c0c0c0");
  },
  yellowRenderer: function(n, t) {
    Handsontable.renderers.TextRenderer.apply(this, arguments);
    t.style.backgroundColor = "yellow";
  },
  init: function() {
    this.dataList = [];
    this.container = document.getElementById('tableWrapper');
    this.$createRow = $('#J-createRow');

    this.create();
    this.updateSettings();
    this.commentsPlugin = this.hot.getPlugin('comments');
    this.bindEvent();
  },
  //添加注释
  setCommentAtCell: function(row, col, comment) {
    this.commentsPlugin.setCommentAtCell(row, col, comment);
  },
  bindEvent: function() {
    var self = this;
    this.$createRow.on('click', function() {
      var amount = $('#J-rowAmount').val() || 0;
      self.insert(amount);
    });
    this.addTip();
  },
  insert: function(count) {
    this.hot.alter('insert_row', this.hot.countRows(), count);
  },
  //加载表格数据
  loadData: function() {
    return this.dataList;
    // return Handsontable.helper.createSpreadsheetData(6, 14);
  },
  initData: function() {
    this.dataList.push(this.primer);
  },
  updateSettings: function() {
    var that = this;
    this.hot.updateSettings({
      // colWidths: [50, 50, 60, 100, 420, 60, 60, 60, 140, 120, 1],
      colWidths: [1, 1, 40, 90, 338, 60, 60, 70, 80, 140, 140, 190, 1],
      fixedColumnsLeft: 3,
      fillHandle: "vertical",
      height: 300,
      hiddenColumns: {},
      contextMenu: {
        callback: function (key, options) {
          if (key === 'about') {
            setTimeout(function () {
              // timeout is used to make sure the menu collapsed before alert is shown
              alert('This is a context menu with default and custom options mixed');
            }, 100);
          }
        },
        items: {
          'row_above': {
            name: DataPageInfo.HandsonTableInfo.UI_OligoInsertAbove,
            disabled: function () {
              if (that.hot.getSelected() == undefined)
                return true;
              var n = that.hot.getSelected()[0]
                , t = that.hot.getSourceData();
              return n === 0 || t.length === 96 ? true : t[n - 1] !== undefined
                && t[n - 1] !== null && t[n - 1].Status === 5 ? true : void 0
            }
          },
          'row_below': {
            name: DataPageInfo.HandsonTableInfo.UI_OligoInsertBelow,
            disabled: function() {
              if (that.hot.getSelected() == undefined)
                return true;
              var n = that.hot.getSelected()[0]
                , t = that.hot.getSourceData();
              return n === 96 || t.length === 96 ? true : t[n + 1] !== undefined
                && t[n + 1] !== null && t[n + 1].Status === 5 ? true : void 0
            }
          },
          'remove_row': {
            name: DataPageInfo.HandsonTableInfo.UI_OligoRemoverow,
            disabled: function() {
              if (that.hot.getSelected() == undefined)
                return true;
              var n = that.hot.getSelected()[0]
                , t = that.hot.getSourceData();
              if (t[n] !== undefined && t[n] !== null && t[n].Status === 5)
                return true
            }
          },
          'add': {
            name: DataPageInfo.HandsonTableInfo.UI_OligoAddToLibrary,
            disabled: function() {
              if (that.hot.getSelected() == undefined || that.hot.getSelected()[1] !== 4
                && that.hot.getSelected()[1] !== 3 || that.hot.getSelected()[3] != 4
                && that.hot.getSelected()[3] != 3)
                return true;
              var n = that.hot.getSelected()[0]
                , t = that.hot.getSourceData();
              if (t[n] !== undefined && t[n] !== null && t[n].Status === 5)
                return true
            }
          },
          'select': {
            name: DataPageInfo.HandsonTableInfo.UI_OligoSelectFromLibrary,
            disabled: function() {
              if (that.hot.getSelected() == undefined || that.hot.getSelected()[1] !== 4
                && that.hot.getSelected()[1] !== 3 || that.hot.getSelected()[3] != 4
                && that.hot.getSelected()[3] != 3)
                return true;
              var n = that.hot.getSelected()[0]
                , t = that.hot.getSourceData();
              if (t[n] !== undefined && t[n] !== null && t[n].Status === 5)
                return true
            }
          }
        }
      }
    })
  },
  //创建表头
  buildHeader: function(colIndex) {
    var template = Handlebars.compile($("#hot-headercol-template").html());
    var data = {
      colName: null, //列名
      isTooltip: false, //是否包括提示
      isRequired: false, //是否必须
      prop: '', //属性名称
      placeholder: '', //默认填充
      hasAction: false, //是否可操作
      fillseq: false, //是否按照顺序填充
      fillcopy: false, //是否复制填充，
      colid: '' //列的id
    };

    switch (colIndex) {
      case 0:
        data.colid = "";
        data.prop = "Plate";
        data.colName = OLIORDER_Plate;
        data.placeholder = "1";
        break;
      case 1:
        data.colid = "";
        data.prop = "Well";
        data.colName = UI_OLI_ORDER_WELL;
        data.placeholder = "A:1";
        break;
      case 2:
        data.colid = "";
        data.prop = "Primer #";
        data.colName = OLIORDER_TablePrimer;
        data.placeholder = "1";
        break;
      case 3:
        data.colid = "olgOligoName";
        data.isRequired = true;
        data.fillseq = true;
        data.prop = "PrimerName";
        data.colName = UI_OLI_ORDER_PRIMERNAME;
        data.placeholder = "Mine";
        data.isTooltip = true;
        data.tooltipText = UI_OLIGOOrder_Pname;
        break;
      case 4:
        data.colid = "olgSequence";
        data.isRequired = true;
        data.fillcopy = true;
        data.prop = "Sequence";
        data.colName = UI_OLI_ORDER_SEQ;
        data.placeholder = "ATGCMRWSYKVHDBN";
        data.isTooltip = true;
        data.tooltipText = UI_OLIGOOrder_Seq;
        break;
      case 5:
        data.colid = "";
        data.prop = "Basenumber";
        data.colName = UI_OLI_ORDER_SEQLENGTH;
        data.placeholder = "15";
        break;
      case 6:
        data.colid = "olgQuantity";
        data.prop = "Quantity";
        data.fillcopy = true;
        data.colName = UI_OLI_ORDER_DEMANDQTY;
        data.placeholder = "3.0";
        data.isTooltip = true;
        data.tooltipText = UI_OLIGOOrder_Int;
        break;
      case 7:
        data.colid = "olgTubes";
        data.prop = "Tubes";
        data.fillcopy = true;
        data.colName = UI_OLI_ORDER_TUBE;
        data.placeholder = "2";
        data.isTooltip = true;
        data.tooltipText = UI_OLIGOOrder_Int;
        break;
      case 8:
        data.colid = "olgPurityType";
        data.prop = "PurityType";
        data.fillcopy = true;
        data.colName = UI_OLI_ORDER_PURIFICATION;
        data.placeholder = "DSL";
        break;
      case 9:
        data.colid = "olgMFive";
        data.prop = "MFive";
        data.fillcopy = true;
        data.colName = UI_OLI_ORDER_5MODIFICATION;
        data.placeholder = "5'-HEX";
        break;
      case 10:
        data.colid = "olgMThree";
        data.prop = "MThree";
        data.fillcopy = true;
        data.colName = UI_OLI_ORDER_5MODIFICATION;
        data.placeholder = "3'-Phosphate";
        break;
      case 11:
        data.colid = "olgMOther";
        data.prop = "MOther";
        data.fillcopy = true;
        data.colName = UI_OLI_ORDER_OTHERMODIFICATION;
        data.placeholder = "dI"
    }

    return template(data);
  },
  create: function() {
    var that = this;
    this.hot = new Handsontable(this.container, {
      data: this.loadData(),
      rowHeaders: true,
      allowEmpty: true,
      colHeaders: this.buildHeader,
      autoColumnSize: true,
      currentRowClassName: "hot-current-row",
      comments: true,
      colWidths: ["5%", "5%", "5%", "5%", "20%", "5%", "5%", "5%", "5%", "10%", "10%", "15%"],
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
        validator: function (value, callback) {
          callback(!isNaN(value));
          that.setCommentAtCell(2,2);
        }
      }, {
        data: "Tubes"
      }, {
        data: "PurityType",
        type: "dropdown",
        source: this.mock.purigyType,
        allowInvalid: true
      }, {
        data: "MFive",
        type: "dropdown",
        source: this.mock.fiveModying
      }, {
        data: "MThree",
        type: "dropdown",
        source: this.mock.threeModying
      }, {
        data: "MOther",
        type: "dropdown",
        source: this.mock.otherModying
      }, {
        data: "Status",
        readOnly: true,
        renderer: this.emptyRowRenderer
      }],
      afterRender: function() {
        that.addTip();
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
      beforeChange: function(n, t) {
        // that.handsonTableBeforeChange(n, t)
      },
      beforeChangeRender: function(n, t) {
        // that.handsonTableBeforeChangeRender(n, t)
      }
    });
  },
  handsonTableBeforeChange: function(n, t) {
    for (var r, i = n.length - 1; i >= 0; i--)
      n[i][1] === "Sequence" && t !== "paste" && (n[i][3] = n[i][3].replace(/\ +/g, "").replace(/[\r\n]/g, "").toUpperCase(),
        r = n[i][3].replace(/\*/g, "").length,
        dataList[(currentPlate.plateId - 1) * 96 + n[i][0]].Basenumber = r,
      site != CLIMSCNCustomer && site != CLIMSCNInternal && calGCAndTM(n[i][3].replace(/\*/g, ""), r, (currentPlate.plateId - 1) * 96 + n[i][0]))
  },
  handsonTableBeforeChangeRender: function(n, t) {
    for (var i, u, f, r = n.length - 1; r >= 0; r--)
      i = (currentPlate.plateId - 1) * 96 + n[r][0],
        n[r][1] === "Sequence" && t === "paste" ? (u = dataList[i].Sequence.replace(/\s/ig, "").replace(/\ +/g, "").replace(/[\r\n]/g, "").toUpperCase(),
          dataList[i].Basenumber = u.replace(/\*/g, "").length,
          dataList[i].Sequence = u,
        site != CLIMSCNCustomer && site != CLIMSCNInternal && (calGCAndTM(n[r][3].replace(/\*/g, "").toUpperCase(), u.replace(/\*/g, "").length, i),
          dataList[i].PurityType = "Desalted")) : n[r][1] === "SequenceBak" && t === "paste" && (f = dataList[i].SequenceBak,
            dataList[i].SequenceBak = f == "是" || f == "yes" ? "yes" : "")
  },
  //校验表格字段
  validationForDraft: function() {
    var cells = this.hot.getSourceData();
    var maxPrimeNameLen = 30;
    var comment = '';
    var updater = [];
    var self = this;

    $.each(cells, function(index, cell) {
      if (self.hot.isEmptyRow(cell) && cell.Status != 5) {
        var primerName = cell.PrimerName || '';
        var sequence = cell.Sequence ? cell.Sequence.toUpperCase() : '';
        var baseNumber = cell.Basenumber;
        var quantity = cell.Quantity;
        var tubes = cell.Tubes;
        var purityType = cell.PurityType || '';
        var col = 0;

        //验证不为空
        if (primerName.length === 0) {
          col = 3;
          comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Validation_PrimerName;
          updater.push({
            row: index,
            col: col,
            renderer: self.yellowRenderer,
            comment: { value: comment }
          });
        }

        //验证不超过最大长度
        if (primerName.length > maxPrimeNameLen) {
          col = 3;
          comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Validation_PrimerNameLength;
          updater.push({
            row: index,
            col: col,
            renderer: self.yellowRenderer,
            comment: { value: comment }
          });
        }
      }
    });

    this.hot.updateSettings({
      cell: updater
    })
  },
  //表头增加tips
  addTip: function() {
    $('.easyui-tooltip').tooltip({
      position: 'right'
    });
  }
};

tb.init();