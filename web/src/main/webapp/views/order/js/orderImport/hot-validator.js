
define(function(require, exports, module) {
  var hot = require('./hot-init');
  var renderer = require('./hot-renderer');

  //**引物名称***//
  var primerNameValidator = function(primerName, index, cell, updater) {
    var maxPrimeNameLen = 30;
    var col = 3;

    //不为空
    if (primerName.length === 0) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Validation_PrimerName;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment }
      });
    }

    //验证不超过最大长度
    if (primerName.length > maxPrimeNameLen) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Validation_PrimerNameLength;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment }
      });
    }
  };

  //**序列***//
  var seqValidator = function(primerName, index, cell, updater) {
    var maxPrimeNameLen = 250;
    var col = 4;

    //不为空
    if (primerName.length === 0) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Validation_Blank;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment }
      });
    }

    //验证不超过最大长度
    if (primerName.length > maxPrimeNameLen) {
      comment = DataPageInfo.HandsonTableValidation.UI_OLIGOOrder_Validation_SequenceLength;
      updater.push({
        row: index,
        col: col,
        renderer: renderer.yellowRenderer,
        comment: { value: comment }
      });
    }
  };

  //**需求量***//

  //**分管总数***//

  //**纯化方式***//

  //**5'修饰***//

  //**3'修饰***//

  //**中间修饰***//


  exports.validationForDraft = function() {
    var cells = hot.getSourceData();
    var maxPrimeNameLen = 30;
    var comment = '';
    var updater = [];

    $.each(cells, function(index, cell) {
      if (!hot.isEmptyRow(cell) && cell.Status != 5) {
        var primerName = cell.PrimerName || '';
        var sequence = cell.Sequence ? cell.Sequence.toUpperCase() : '';
        var baseNumber = cell.Basenumber;
        var quantity = cell.Quantity;
        var tubes = cell.Tubes;
        var purityType = cell.PurityType || '';
        var col = 0;

        primerNameValidator(primerName, index, cell, updater);
        seqValidator(sequence, index, cell, updater);
      }
    });

    hot.updateSettings({
      cell: updater
    });
  }
});