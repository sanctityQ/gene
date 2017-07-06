
define(function(require, exports, module) {
  var addTip = require('./hot-addTip');

  module.exports = function(colIndex) {
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
  };
});