
define(function(require, exports, module) {

  exports.validationForDraft = function() {
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
  }
});